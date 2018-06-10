package com.scheideggergroup.core.service.impl;

import com.scheideggergroup.core.event.AirQualityReportListener;
import com.scheideggergroup.core.event.RobotMovementArgs;
import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.AirQualityMeasure;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.AirQualityService;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.MovementService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * MovementService implementation in accordance to the current requirements.
 * @author scheidv1
 *
 */
@Service(value="movementService")
public class MovementServiceImpl implements MovementService {

    private static Logger logger = LoggerFactory.getLogger(MovementServiceImpl.class);

    @Autowired
    @Qualifier("coordinateService")
    CoordinateService coordService;
    
    @Autowired
    @Qualifier("taskScheduler")
    ThreadPoolTaskScheduler taskSched;

    @Autowired
    @Qualifier("airQualityService")
    AirQualityService airQualityService;

    private List<MonitoringStation> measuringStations;
    
    @Override
    public void setMeasuringStations(List<MonitoringStation> measuringStations) {
        this.measuringStations = measuringStations;
    }

    @Override
    public void moveRobotAlongRoute(Robot robot, Route route) {
        
        Validate.notNull(robot, "The robot has not been specified.");
        Validate.notNull(route, "The route has not been specified.");
        Validate.notNull(route.getPath(), "The route has no path specified.");
        
        logger.debug("Setting the robot route.");
        robot.setCurrentRoute(route);
        if(route.getSteps() == null) {
            route = coordService.calculateStepsOnRoute(route);
        }
        
        logger.debug("Getting the travel parameters.");
        double refreshRate = robot.getRefreshRate();
        final double monitoringPeriodMilliSec = 1000 / refreshRate;
        final Robot workRobot = robot;
        final Semaphore semaphore = new Semaphore(0);
        final Route workRoute = workRobot.getCurrentRoute();
        final List<Step> workSteps = workRoute.getSteps();
        final double distanceBetweenMeasures = robot.getDistanceBetweenMeasures();
        
        logger.debug("Setting the robot starting point.");
        robot.setLastMeasuredCoordinate(workSteps.get(0).getStartCoordinate());

        Runnable monitorRobotMovement = this.monitorRobotMovement(monitoringPeriodMilliSec, workRobot, semaphore, workRoute,
                workSteps, workRobot.getRobotMovementListeners());
        
        final List<AirQualityMeasure> airQualityMeasurements = new ArrayList<AirQualityMeasure>();        
        Runnable readParticleLevel = airQualityService.readParticleLevel(workRobot, workRoute, workSteps, airQualityMeasurements, distanceBetweenMeasures);
        
        Runnable anounceAirQualityAverageLevel = airQualityService.publishAirQualityAverageLevel(workRobot, airQualityMeasurements);

        final LocalDateTime travelStartTime = LocalDateTime.now();
        route.setTravelStartTime(travelStartTime);

        logger.debug("Starting the robot journey.");
        logger.debug("Checking stations up to " + robot.getMonitoringStationDistanceRange() + " meters away.");
        taskSched.scheduleAtFixedRate(monitorRobotMovement, (long) monitoringPeriodMilliSec);
        
        // In order for the read to occur at every X meters we calculate the Task Scheduler Execution rate.
        double particleReadingPeriodMilliSec = (distanceBetweenMeasures / robot.getSpeed()) * 1000;
        taskSched.scheduleAtFixedRate(readParticleLevel, (long) particleReadingPeriodMilliSec);

        taskSched.scheduleAtFixedRate(anounceAirQualityAverageLevel, (long) 1000 * 60 * 1);

        try {
            semaphore.tryAcquire(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public Runnable monitorRobotMovement(final double monitoringPeriodMilliSec, final Robot workRobot,
            final Semaphore semaphore, final Route workRoute, final List<Step> workSteps, final List<RobotMovementListener> listeners) {
        Runnable monitorRobotMovement = () -> {
            
            LocalDateTime timestamp = LocalDateTime.now();
            
            double workSpeed = workRobot.getSpeed();
            double metersTraveledOnPeriod = workSpeed * (monitoringPeriodMilliSec/1000);
            double totalTraveledDistance = workRobot.getTraveledDistance() + metersTraveledOnPeriod;
            double routeTotalDistance = workRoute.getTotalDistance();
            double warningDistance = workRobot.getMonitoringStationDistanceRange();
            List<MonitoringStation> oldStations = workRobot.getStationsWithinRange();
            List<AirQualityReportListener> robotReportListeners = workRobot.getAirQualityReportListeners();
            
            if(totalTraveledDistance > routeTotalDistance) {
                totalTraveledDistance = routeTotalDistance;
            }
            
            Coordinate measuredPoint = coordService.getFinalPositionAfterMove(workRoute, totalTraveledDistance);
            
            List<MonitoringStation> stationsWithinRange = this.getStationsWithinRange(oldStations, warningDistance, measuredPoint, robotReportListeners, workRobot);

            workRobot.setLastMeasuredCoordinate(measuredPoint);
            workRobot.setTraveledDistance(totalTraveledDistance);
            workRobot.setStationsWithinRange(stationsWithinRange);
            workRobot.setLastMeasuredTimestamp(timestamp);

            Robot stateRobot = new Robot();
            stateRobot.setCurrentRoute(workRoute);
            stateRobot.setSpeed((int)workSpeed);
            stateRobot.setLastMeasuredCoordinate(measuredPoint);
            stateRobot.setTraveledDistance(totalTraveledDistance);
            stateRobot.setStationsWithinRange(stationsWithinRange);
            stateRobot.setLastMeasuredTimestamp(timestamp);

            RobotMovementArgs args = new RobotMovementArgs(stateRobot, measuredPoint, timestamp);
            
            for(RobotMovementListener listener : listeners) {
                listener.onRobotMovement(this, args);
            }
            
            if (totalTraveledDistance >= workRoute.getTotalDistance()) {
                semaphore.release();
            }
        };
        return monitorRobotMovement;
    }

    private List<MonitoringStation> getStationsWithinRange(List<MonitoringStation> oldList, double warningDistance, Coordinate measuredPoint, List<AirQualityReportListener> listenersFromRobot, Robot robot) {
        
        List<MonitoringStation> stationsWithinRange = new ArrayList<MonitoringStation>();
        
        for (MonitoringStation monitoringStation : measuringStations) {
            double dist = coordService.getDistanceBetweenCoordinates(measuredPoint, monitoringStation.getGeoLocation());
            if (dist <= warningDistance) {
                if(!oldList.contains(monitoringStation)) {
                    logger.debug("Station " + monitoringStation.getSourceName() + " is " + dist + " meters away. Within the configured range of: " + warningDistance);
                    airQualityService.publishReportFromAllStationsAndRobot(measuringStations, robot);
                }
                stationsWithinRange.add(monitoringStation);
            } else if(oldList.contains(monitoringStation)) {
                logger.debug("Station " + monitoringStation.getSourceName() + " is now at " + dist + " out of the configured range.");
            }
        }
        
        return stationsWithinRange;
    }

}
