package com.scheideggergroup.core.service;

import org.springframework.stereotype.Service;
import com.scheideggergroup.core.model.Coordinate;

@Service("coordinateService")
public interface CoordinateService {

    public double getDistanceBetweenCoordinates(Coordinate startCoordinate, Coordinate finalCoordinate);
    
    public double getBearing(Coordinate startCoordinate, Coordinate finalCoordinate);
    
    public Coordinate getFinalPositionAfterMove(Coordinate startCoordinate, double bearing, double distance);
    
}
