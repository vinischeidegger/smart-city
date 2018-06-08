package com.scheideggergroup.test.core.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.impl.CoordinateServiceImpl;

public class CoordinateServiceImplTests {

    @Test
    public void distanceBetweenSameCoordinatesShouldBeZeroTest() {
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 0);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertEquals("Distance between same coordinates should be zero", 0, dist, 0.000001);
    }
    
    @Test
    public void distanceBetweenDiffCoordinatesShouldNotBeZeroTest() {
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 180);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertTrue(String.format("Distance between different coordinates should not be zero = [%1$f]",dist), dist > 0);
    }
    
    @Test
    public void BearingShouldBeZeroForDistancesOnEquatorTest() {
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 180);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double bearing = coordServ.getBearing(startCoordinate, finalCoordinate);
        Assert.assertTrue(String.format("Bearing for distances on equator should be 90 = [%1$f]",bearing), bearing != 0);
    }

    @Test
    public void BearingShouldBeMultipleOf90ForDistancesOnEquatorTest() {
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
    public void getFinalPositionAfterMove() {
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        double bearing = 90;
        double distance = 10000;
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        Coordinate finalCoordinate = coordServ.getFinalPositionAfterMove(startCoordinate, bearing, distance);
        
        Assert.assertTrue("The end point cannot be 0,0", finalCoordinate.getLatitude() != 0 && finalCoordinate.getLongitude() != 0 );
    }
    
}
