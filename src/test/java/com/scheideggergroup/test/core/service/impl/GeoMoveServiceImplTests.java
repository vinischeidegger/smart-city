package com.scheideggergroup.test.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.scheideggergroup.core.app.SmartCityCoreApiWebApp;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.RobotMovementArgs;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.GeoMoveService;
import com.scheideggergroup.core.service.impl.CoordinateServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SmartCityCoreApiWebApp.class})
public class GeoMoveServiceImplTests {

    private static Logger logger = LoggerFactory.getLogger(CoordinateServiceImpl.class);
    
    @Autowired
    @Qualifier("geoMoveService")
    GeoMoveService geoMoveService;
    
    @Test
    public void robotMoveTest() {
        
        List<Coordinate> path = Arrays.asList(new Coordinate(41.42341, 2.13987), new Coordinate(41.38093, 2.18562));
        
        Route route = new Route();
        route.setPath(path);
        
        Robot robot = new Robot();
        robot.setCurrentRoute(route);
        
        int speed = 3;
        robot.setSpeed(speed);
        
        int refreshRate = 10;
        double distancefromMeasureToMeasure = 100;

        geoMoveService.setRefreshRate(refreshRate);
        geoMoveService.setDistanceBetweenAirQualityMeasures(distancefromMeasureToMeasure);
        List<RobotMovementArgs> triggeredEvents = new ArrayList<RobotMovementArgs>();
        geoMoveService.registerRobotMovementListener((sender, args) -> {
            triggeredEvents.add(args);
        });
        
        geoMoveService.moveRobotAlongRoute(robot, route);
        
        for(RobotMovementArgs arg : triggeredEvents) {
            logger.debug("Robot position at: " + arg.getEventDatetime() + " coordinate = " + arg.getRobotCoordinate());
        }
    }

}
