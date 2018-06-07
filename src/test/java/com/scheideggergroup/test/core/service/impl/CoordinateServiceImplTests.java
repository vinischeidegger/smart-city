package com.scheideggergroup.test.core.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.impl.CoordinateServiceImpl;

public class CoordinateServiceImplTests {

    @Test
    public void distanceCalculatorTest() {
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate finalCoordinate = new Coordinate(0, 0);
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        double dist = coordServ.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate);
        Assert.assertEquals("Distance between same coordinates should be zero", 0, dist, 0.000001);
    }
    
    @Test
    public void getFinalPositionAfterMove() {
        
        Coordinate startCoordinate = new Coordinate(0, 0);
        double bearing = 90;
        double distance = 100;
        CoordinateService coordServ = new CoordinateServiceImpl();
        
        Coordinate finalCoordinate = coordServ.getFinalPositionAfterMove(startCoordinate, bearing, distance);
        
    }
    
}
