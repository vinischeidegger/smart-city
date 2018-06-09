package com.scheideggergroup.core.service.impl;

import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.RobotMovementArgs;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.GeoMoveService;

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

    @Override
    public void moveRobotAlongRoute(Robot robot, Route route) {
        
        Validate.notNull(robot, "The robot has not been specified.");
        Validate.notNull(route, "The route has not been specified.");
        Validate.notNull(route.getPath(), "The route has no path specified.");
        
        robot.setCurrentRoute(route);
        if(route.getSteps() == null) {
            route = coordService.calculateStepsOnRoute(route);
        }
        
        final double periodInMilliSecond = 1000 / refreshRate;
        final Robot workRobot = robot;
        final Semaphore semaphore = new Semaphore(0);
        final double max = 15.0;
        
        Runnable subtractDistance = () -> {
            LocalDateTime timestamp = LocalDateTime.now();
            Route workRoute = workRobot.getCurrentRoute();
            double workSpeed = workRobot.getSpeed();
            logger.debug("Speed = " + workSpeed);
            double metersTraveled = workSpeed * (periodInMilliSecond/1000);
            logger.debug("Meters = " + metersTraveled);
            double workTraveledDistance = workRobot.getTraveledDistance() + metersTraveled;
            robot.setTraveledDistance(workTraveledDistance);
            logger.debug("Traveled Distance = " + workTraveledDistance);
            //if (workTraveledDistance >= workRoute.getTotalDistance()) {
            if (workTraveledDistance >= max) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        
        
        taskSched.scheduleAtFixedRate(subtractDistance, (long) periodInMilliSecond); 
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

}
