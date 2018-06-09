package com.scheideggergroup.core.service.impl;

import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.AirQualityMeasure;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.RobotMovementArgs;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.GeoMoveService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * GeoMoveService implementation in accordance to the current requirements.
 * @author scheidv1
 *
 */
@Service(value="geoMoveService")
public class GeoMoveServiceImpl implements GeoMoveService {

    private static Logger logger = LoggerFactory.getLogger(GeoMoveServiceImpl.class);

    @Autowired
    @Qualifier("coordinateService")
    CoordinateService coordService;
    
    @Autowired
    ThreadPoolTaskScheduler taskSched;
    
    private List<RobotMovementListener> robotMovementListeners = new ArrayList<RobotMovementListener>();
    
    private int refreshRate;

    private double distanceBetweenAirQualityMeasures;

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
        final double monitoringPeriodMilliSec = 1000 / refreshRate;
        final Robot workRobot = robot;
        final Semaphore semaphore = new Semaphore(0);
        final Route workRoute = workRobot.getCurrentRoute();
        final List<Step> workSteps = workRoute.getSteps();
        
        logger.debug("Setting the robot starting point.");
        robot.setLastMeasuredCoordinate(workSteps.get(0).getStartCoordinate());

        Runnable monitorRobotMovement = () -> {
            
            LocalDateTime timestamp = LocalDateTime.now();
            
            double workSpeed = workRobot.getSpeed();
            double metersTraveledOnPeriod = workSpeed * (monitoringPeriodMilliSec/1000);
            double workTraveledDistance = workRobot.getTraveledDistance() + metersTraveledOnPeriod;
            
            Step currentStep = null;
            double initialDistanceOnStep = 0;

            for(Step routeStep : workSteps) {
                initialDistanceOnStep = routeStep.getInitialDistanceOnPath();
                if(workTraveledDistance > routeStep.getInitialDistanceOnPath() && workTraveledDistance <= routeStep.getFinalDistanceOnPath()) {
                    currentStep = routeStep;
                    break;
                }
            }
            
            // Current step will be null if traveled distance surpasses the last point
            if(currentStep == null) {
                currentStep = workSteps.get(workSteps.size()-1);
                workTraveledDistance = workRoute.getTotalDistance();
            }
            
            robot.setTraveledDistance(workTraveledDistance);
            logger.debug("Traveled Distance = " + workTraveledDistance);

            Coordinate stateCoordinate = coordService.getFinalPositionAfterMove(
                                                        currentStep.getStartCoordinate(),
                                                        currentStep.getBearing(),
                                                        workTraveledDistance - initialDistanceOnStep);
            
            workRobot.setLastMeasuredTimestamp(timestamp);
            
            Robot stateRobot = new Robot();
            stateRobot.setCurrentRoute(workRoute);
            stateRobot.setLastMeasuredTimestamp(timestamp);
            stateRobot.setLastMeasuredCoordinate(stateCoordinate);
            stateRobot.setSpeed((int)workSpeed);
            stateRobot.setTraveledDistance(workTraveledDistance);
            
            RobotMovementArgs args = new RobotMovementArgs(workRobot, stateCoordinate, timestamp);
            
            for(RobotMovementListener listener : this.robotMovementListeners) {
                listener.onRobotMovement(this, args);
            }
            
            if (workTraveledDistance >= workRoute.getTotalDistance()) {
            //if (workTraveledDistance >= 15) {
                semaphore.release();
            }
        };
        
        final List<AirQualityMeasure> airQualityMeasurements = new ArrayList<AirQualityMeasure>();        
        final LocalDateTime travelStartTime = LocalDateTime.now();
        route.setTravelStartTime(travelStartTime);
        
        Runnable readParticleLevel = () -> {
            
            LocalDateTime timestamp = LocalDateTime.now();
            Coordinate measurementCoord = null;
            synchronized (workRobot) {
                Coordinate lastRobotLocation = workRobot.getLastMeasuredCoordinate();
                measurementCoord = new Coordinate(lastRobotLocation.getLatitude(), lastRobotLocation.getLongitude());
            }
            
            // distance as measured by the movement watcher
            // double dist = workRobot.getTraveledDistance();
            
            double measurementDistance = distanceBetweenAirQualityMeasures * airQualityMeasurements.size();
            int randomValue = ThreadLocalRandom.current().nextInt(
                                        (int)(AirQualityMeasure.PM25_MIN_MEASURED_VALUE) * 10,
                                        (int)(AirQualityMeasure.PM25_MIN_MEASURED_VALUE) * 10 + 1);
            logger.debug("ANOUNCING THE READING AT " + measurementDistance + " METERS");

            airQualityMeasurements.add(new AirQualityMeasure(timestamp, measurementCoord, workRobot, randomValue));
            
        };
        
        // In order for the read to occur at every X meters we calculate the Task Scheduler Execution rate.
        double particleReadingPeriodMilliSec = (distanceBetweenAirQualityMeasures / robot.getSpeed()) * 1000;

        logger.debug("Starting the robot journey.");
        taskSched.scheduleAtFixedRate(monitorRobotMovement, (long) monitoringPeriodMilliSec);

        taskSched.scheduleAtFixedRate(readParticleLevel, (long) particleReadingPeriodMilliSec);

        try {
            semaphore.tryAcquire(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Calculate all
    }
    
    protected void notifyRobotMovementListeners(RobotMovementArgs args) {
        this.robotMovementListeners.forEach(listener -> listener.onRobotMovement(this, args));
    }

    @Override
    public void registerRobotMovementListener(RobotMovementListener listener) {
        this.robotMovementListeners.add(listener);
    }

    @Override
    public void unregisterRobotMovementListener(RobotMovementListener listener) {
        this.robotMovementListeners.remove(listener);
    }

    @Override
    public void setRefreshRate(int refreshRate) {
        
        this.refreshRate = refreshRate;
    }

    public void setDistanceBetweenAirQualityMeasures(double distanceBetweenAirQualityMeasures) {
        this.distanceBetweenAirQualityMeasures = distanceBetweenAirQualityMeasures;
    }

}
