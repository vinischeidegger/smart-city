package com.scheideggergroup.test.core.service.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.impl.CoordinateServiceImpl;

public class CoordinateServiceImplTests {

    private static Logger logger = LoggerFactory.getLogger(CoordinateServiceImplTests.class);

    @Test
    public void distanceBetweenSameCoordinatesShouldBeZeroTest() {
        
        logger.debug("Running test method distanceBetweenSameCoordinatesShouldBeZeroTest...");
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 0);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertEquals("Distance between same coordinates should be zero", 0, dist, 0.000001);
    }
    
    @Test
    public void distanceBetweenAntipodesShouldHalfTheCircumferenceOfTheEarth() {
        
        logger.debug("Running test method distanceBetweenAntipodesShouldHalfTheCircumferenceOfTheEarth...");
        
        final double halfCircumferenceOfTheEarth = 20015086.8;
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 180);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertEquals(String.format("Distance between Antipodes should be [%1$f] and not = [%2$f]",halfCircumferenceOfTheEarth, dist), halfCircumferenceOfTheEarth, dist, 0.1);
    }
    
    @Test
    public void distanceBetweenDiffCoordinatesShouldNotBeZeroTest() {
        
        logger.debug("Running test method distanceBetweenDiffCoordinatesShouldNotBeZeroTest...");
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 180);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertTrue(String.format("Distance between different coordinates should not be zero = [%1$f]",dist), dist > 0);
    }

    @Test
    public void bearingShouldMatchExpected() {
        
        logger.debug("Running test method bearingShouldMatchExpected...");
        
        Coordinate startCoordinate = new Coordinate(39.099912, -94.581213);
        Coordinate finalCoordinate = new Coordinate(38.627089, -90.200203);
        CoordinateService coordServ = new CoordinateServiceImpl();
        double expectedBearing = 96.51;
        
        double bearing = coordServ.getBearing(startCoordinate, finalCoordinate);
        Assert.assertEquals(String.format("Bearing should match [%1$f] and not = [%2$f]",expectedBearing, bearing), expectedBearing, bearing, 0.01);
    }
    
    @Test
    public void moduleBearingShouldBe90ForDistancesAlongTheEquatorTest() {
        
        logger.debug("Running test method moduleBearingShouldBe90ForDistancesAlongTheEquatorTest...");
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 180);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double bearing = Math.abs(coordServ.getBearing(startCoordinate, finalCoordinate));
        Assert.assertEquals(String.format("Bearing for distances on equator should be 90 = [%1$f]",bearing), 90, bearing, 0.01);
    }

    @Test
    public void bearingShouldBeMultipleOf90ForDistancesOnEquatorTest() {
        
        logger.debug("Running test method bearingShouldBeMultipleOf90ForDistancesOnEquatorTest...");
        
        Coordinate startCoordinate = new Coordinate(0, 180);
        Coordinate finalCoordinate = new Coordinate(0, 0);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double bearing = coordServ.getBearing(startCoordinate, finalCoordinate);
        Assert.assertTrue(String.format("Bearing for distances on equator should be 90 = [%1$f]",bearing), bearing != 0);

        startCoordinate = new Coordinate(0, 0);
        finalCoordinate = new Coordinate(90, 0);
        
        double bearing2 = coordServ.getBearing(startCoordinate, finalCoordinate);
        Assert.assertTrue(String.format("Bearing for distances on equator should be 90 = [%1$f]",bearing), bearing2 == 0);
    }
    
    @Test
    public void finalPositionAfterMoveShouldBeDifferentThanStartingPositionTest() {
        
        logger.debug("Running test method finalPositionAfterMoveShouldBeDifferentThanStartingPositionTest...");
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        double bearing = 90;
        double distance = 10000;
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        Coordinate finalCoordinate = coordServ.getFinalPositionAfterMove(startCoordinate, bearing, distance);
        
        Assert.assertTrue("The end point cannot be 0,0", finalCoordinate.getLatitude() != 0 && finalCoordinate.getLongitude() != 0 );
    }
    
    @Test
    public void positionAfterTravellingThroughAStepShouldBeStepFinalPositionTest() {
        
        logger.debug("Running test method positionAfterTravellingThroughAStepShouldBeStepFinalPositionTest...");
        
        Coordinate startCoordinate = new Coordinate(41.42341, 2.13987);
        Coordinate finalCoordinate = new Coordinate(41.38093, 2.18562);
        
        CoordinateService coordinateService = new CoordinateServiceImpl();
        Step step = coordinateService.calculateStep(startCoordinate, finalCoordinate);
        
        Coordinate realFinalCoordinate = coordinateService.getFinalPositionAfterMove(startCoordinate, step.getBearing(), step.getDistance());
        
        Assert.assertEquals("Final latitude should match end point latitude", finalCoordinate.getLatitude(), realFinalCoordinate.getLatitude(), 0.00001);
        Assert.assertEquals("Final longitude should match end point longitude", finalCoordinate.getLongitude(), realFinalCoordinate.getLongitude(), 0.00001);
    }
    
    @Test
    public void totalDistanceOfPathShouldMatchSeparateDistances() {

        logger.debug("Running test method totalDistanceOfPathShouldMatchSeparateDistances...");
        
        // My house
        Coordinate startCoordinate = new Coordinate(41.42341, 2.13987);
        // Metropolis Lab
        Coordinate firstStop = new Coordinate(41.38093, 2.18562);
        // Audi Factory on Germany
        Coordinate endCoordinate = new Coordinate(48.7853987, 11.4103501);
        
        CoordinateService coordinateService = new CoordinateServiceImpl();
        
        double dist1 = coordinateService.getDistanceBetweenCoordinates(startCoordinate, firstStop);
        double dist2 = coordinateService.getDistanceBetweenCoordinates(firstStop, endCoordinate);
        double sumOfDistances = dist1 + dist2;
        
        Route route = new Route();

        List<Coordinate> path = Arrays.asList(startCoordinate, firstStop, endCoordinate);
        route.setPath(path);
        
        route = coordinateService.calculateStepsOnRoute(route);
        
        Assert.assertNotNull("Route should not be null after calculation.", route);
        Assert.assertNotNull("Route path should not be null after calculation.", route.getPath());
        Assert.assertNotNull("Route steps should not be null after calculation.", route.getSteps());
        Assert.assertTrue("Distance should be grater than zero.", route.getTotalDistance() > 0);
        Assert.assertEquals("Sum of distances should be equal to the route total distance.", sumOfDistances, route.getTotalDistance(), 0.1);
    }
}
