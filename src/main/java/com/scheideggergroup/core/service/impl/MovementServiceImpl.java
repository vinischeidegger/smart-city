package com.scheideggergroup.core.service.impl;

import com.scheideggergroup.core.event.AirQualityReportListener;
import com.scheideggergroup.core.event.RobotMovementArgs;
import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.AirQualityMeasure;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Polyline;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.AirQualityService;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.MovementService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
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
    @Qualifier("taskScheduler")
    ThreadPoolTaskScheduler taskSched;

    @Autowired
    @Qualifier("coordinateService")
    CoordinateService coordService;
    
    @Autowired
    @Qualifier("airQualityService")
    AirQualityService airQualityService;

    @Autowired
    @Qualifier("webSocketService")
    WebSocketService webSocketService;

    private List<MonitoringStation> measuringStations;
    private List<Robot> movingRobots = new ArrayList<Robot>();
    
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
        final double averageReadingInterval = robot.getAverageMeasureInterval();
        
        logger.debug("Setting the robot starting point.");
        robot.setLastMeasuredCoordinate(workSteps.get(0).getStartCoordinate());

        Runnable monitorRobotMovement = this.monitorRobotMovement(monitoringPeriodMilliSec, workRobot, semaphore, workRoute,
                workSteps, workRobot.getRobotMovementListeners());
        
        final List<AirQualityMeasure> airQualityMeasurements = new ArrayList<AirQualityMeasure>();        
        Runnable readParticleLevel = airQualityService.readParticleLevel(workRobot, workRoute, workSteps, airQualityMeasurements, distanceBetweenMeasures);
        
        Runnable anounceAirQualityAverageLevel = airQualityService.publishAirQualityAverageLevel(workRobot, airQualityMeasurements);

        final Instant travelStartTime = Instant.now();
        route.setTravelStartTime(travelStartTime);

        logger.debug("Starting the robot journey.");
        logger.debug("Checking stations up to " + robot.getMonitoringStationDistanceRange() + " meters away.");
        final ScheduledFuture<?> monitorMovement = taskSched.scheduleAtFixedRate(monitorRobotMovement, (long) monitoringPeriodMilliSec);
        
        // In order for the read to occur at every X meters we calculate the Task Scheduler Execution rate.
        double particleReadingPeriodMilliSec = (distanceBetweenMeasures / robot.getSpeed()) * 1000;
        final ScheduledFuture<?> particleReading = taskSched.scheduleAtFixedRate(readParticleLevel, (long) particleReadingPeriodMilliSec);

        final ScheduledFuture<?> averageReading = taskSched.scheduleAtFixedRate(anounceAirQualityAverageLevel, (long) (1000.0 * 60.0 * averageReadingInterval));

        try {
            semaphore.tryAcquire(5, TimeUnit.DAYS);
            monitorMovement.cancel(true);
            particleReading.cancel(true);
            averageReading.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public Runnable monitorRobotMovement(final double monitoringPeriodMilliSec, final Robot workRobot,
            final Semaphore semaphore, final Route workRoute, final List<Step> workSteps, final List<RobotMovementListener> listeners) {
        Runnable monitorRobotMovement = () -> {
            
            Instant timestamp = Instant.now();
            
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

            Robot stateRobot = new Robot(workRobot.getId(), workRobot.getRobotName());
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
            
            if (workRobot.isSignalToStop() || totalTraveledDistance >= workRoute.getTotalDistance()) {
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

    @Override
    public Robot moveNewRobotAlongPolyline(Polyline polyline) {
        
        int speed = 3;
        int refreshRate = 10; // readings per second
        double distanceBetweenAirQualityMeasures = 100;
        double monitoringStationWarningRange = 100;
        double averageReadingInterval = 15;

        Route route = coordService.calculateRouteFromPolyline(polyline);
        
        MonitoringStation station1 = new MonitoringStation("Buckingham Palace", new Coordinate(51.501299, -0.141935));
        MonitoringStation station2 = new MonitoringStation("Temple Station", new Coordinate(51.510852, -0.114165));
        
        List<MonitoringStation> measuringStations = new ArrayList<MonitoringStation>();
        measuringStations.add(station1);
        measuringStations.add(station2);
        
        this.setMeasuringStations(measuringStations);
        
        Robot robot = new Robot();
        robot.setCurrentRoute(route);
        robot.setSpeed(speed);
        robot.setAverageMeasureInterval(averageReadingInterval);
        robot.setRefreshRate(refreshRate);
        robot.setDistanceBetweenMeasures(distanceBetweenAirQualityMeasures);
        robot.setMonitoringStationDistanceRange(monitoringStationWarningRange);
        
        this.movingRobots.add(robot);
        
        robot.registerRobotMovementListener((sender, args) -> {
            webSocketService.updateMovementTopic(robot, args);
        });
        
        robot.registerAirQualityReportListener((sender, args) -> {
            webSocketService.updateReportsTopic(robot, args);
        });
        
        taskSched.execute(() -> {
            this.moveRobotAlongRoute(robot, route);
        });
        
        return robot;
    }
    
    @Override
    public void stopRobot(String id) {
        this.movingRobots.forEach((robot) -> {
            if(robot.getId().equals(id)) {
                robot.setSignalToStop(true);
            }
        });
    }

}
