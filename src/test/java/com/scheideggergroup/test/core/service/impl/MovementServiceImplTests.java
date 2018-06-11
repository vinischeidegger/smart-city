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
import com.scheideggergroup.core.event.AirQualityReportArgs;
import com.scheideggergroup.core.event.RobotMovementArgs;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.MovementService;
import com.scheideggergroup.core.service.impl.CoordinateServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SmartCityCoreApiWebApp.class})
public class MovementServiceImplTests {

    private static Logger logger = LoggerFactory.getLogger(CoordinateServiceImpl.class);
    
    @Autowired
    @Qualifier("movementService")
    MovementService movementService;
    
    @Test
    public void robotMoveTest() {
        
        List<Coordinate> path = Arrays.asList(new Coordinate(41.42341, 2.13987), new Coordinate(41.38093, 2.18562));
        
        int speed = 3;
        int refreshRate = 10; // readings per second
        double distanceBetweenAirQualityMeasures = 100;
        double monitoringStationWarningRange = 100;
        double averageReadingsInterval = 1;

        Route route = new Route();
        route.setPath(path);
        
        Robot robot = new Robot();
        robot.setCurrentRoute(route);
        robot.setSpeed(speed);
        robot.setAverageMeasureInterval(averageReadingsInterval);
        robot.setRefreshRate(refreshRate);
        robot.setDistanceBetweenMeasures(distanceBetweenAirQualityMeasures);
        robot.setMonitoringStationDistanceRange(monitoringStationWarningRange);
        
        List<RobotMovementArgs> triggeredEvents = new ArrayList<RobotMovementArgs>();
        robot.registerRobotMovementListener((sender, args) -> {
            triggeredEvents.add(args);
        });
        
        List<AirQualityReportArgs> triggeredReports = new ArrayList<AirQualityReportArgs>();
        robot.registerAirQualityReportListener((sender, args) -> {
            triggeredReports.add(args);
        });
        
        MonitoringStation station1 = new MonitoringStation("Buckingham Palace", new Coordinate(51.501299, -0.141935));
        MonitoringStation station2 = new MonitoringStation("Temple Station", new Coordinate(51.510852, -0.114165));
        MonitoringStation station3 = new MonitoringStation("Test", new Coordinate(41.42341, 2.13987));
        
        List<MonitoringStation> measuringStations = new ArrayList<MonitoringStation>();
        measuringStations.add(station1);
        measuringStations.add(station2);
        measuringStations.add(station3);
        
        movementService.setMeasuringStations(measuringStations);
        movementService.moveRobotAlongRoute(robot, route);
        
        for(RobotMovementArgs arg : triggeredEvents) {
            logger.debug("Robot position at: " + arg.getEventDatetime() + " coordinate = " + arg.getRobotCoordinate());
        }
    }

}
