package com.scheideggergroup.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.CoordinateService;

/**
 * Implementation of the CoordinateService interface for the planet Earth as being a perfectly round sphere and without altitude difference.
 * Thanks to https://www.movable-type.co.uk/scripts/latlong.html
 * @author scheidv1
 *
 */
@Service("coordinateService")
public class CoordinateServiceImpl implements CoordinateService {

    private static Logger logger = LoggerFactory.getLogger(CoordinateServiceImpl.class);

    private static final double EARTH_RADIUS = 6371000;

    @Override
    public double getDistanceBetweenCoordinates(Coordinate startCoordinate, Coordinate finalCoordinate) {
		
		double lat1 = startCoordinate.getLatitude();
		double lon1 = startCoordinate.getLongitude();
		double lat2 = finalCoordinate.getLatitude();
		double lon2 = finalCoordinate.getLongitude();

		logger.trace(String.format("Calculating distance between [%1$+10.5f, %2$+10.5f] and [%3$+10.5f, %4$+10.5f]", lat1, lon1, lat2, lon2));

		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = EARTH_RADIUS  * c;
		
		logger.trace(String.format("Distance is %1$.1f meters", dist));

		return dist;
    }

    @Override
    public Coordinate getFinalPositionAfterMove(Coordinate startCoordinate, double bearing, double distance) {

		double lat1 = Math.toRadians(startCoordinate.getLatitude());
		double lon1 = Math.toRadians(startCoordinate.getLongitude());
		bearing = Math.toRadians(bearing);
		
    	double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/EARTH_RADIUS) +
                Math.cos(lat1)*Math.sin(distance/EARTH_RADIUS)*Math.cos(bearing) );
    	
    	double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/EARTH_RADIUS)*Math.cos(lat1),
                     Math.cos(distance/EARTH_RADIUS)-Math.sin(lat1)*Math.sin(lat2));
    	
    	return new Coordinate(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

	public double getBearing(Coordinate startCoordinate, Coordinate finalCoordinate) {
		
		double dLat1 = startCoordinate.getLatitude();
		double dLon1 = startCoordinate.getLongitude();
		double dLat2 = finalCoordinate.getLatitude();
		double dLon2 = finalCoordinate.getLongitude();

		logger.trace(String.format("Calculating bearing between [%1$+10.5f, %2$+10.5f] and [%3$+10.5f, %4$+10.5f]", dLat1, dLon1, dLat2, dLon2));

		double lat1 = Math.toRadians(dLat1);
        double lat2 = Math.toRadians(dLat2);
        double deltaLon = Math.toRadians(dLon2-dLon1);
		
		double y = Math.cos(lat2) * Math.sin(deltaLon);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
		double bearing = Math.toDegrees(Math.atan2(y, x));

		logger.trace(String.format("Bearing is %1$.3f degrees", bearing));

		return bearing;
	}
	
    @Override
    public Step calculateStep(Coordinate startCoordinate, Coordinate finalCoordinate) {

        Step step = new Step(
                          startCoordinate, 
                          finalCoordinate, 
                          this.getDistanceBetweenCoordinates(startCoordinate, finalCoordinate),
                          this.getBearing(startCoordinate, finalCoordinate));

        return step;
    }

    @Override
    public Route calculateStepsOnRoute(Route route) {
        
        List<Coordinate> path = route.getPath();
        List<Step> steps = new ArrayList<Step>();
        int stepQty = path.size() - 1;
        double totalDistance = 0;
        double startDistance = 0;
        
        for(int i = 0; i < stepQty; i++) {
            
            Coordinate startCoordinate = path.get(i);
            Coordinate endCoordinate = path.get(i + 1);
            
            double distance = this.getDistanceBetweenCoordinates(startCoordinate, endCoordinate);
            double bearing = this.getBearing(startCoordinate, endCoordinate);
            totalDistance += distance;
            
            Step step = new Step(startCoordinate, endCoordinate, distance, bearing);
            step.setInitialDistanceOnPath(startDistance);
            double finalDistance = startDistance + distance;
            step.setFinalDistanceOnPath(finalDistance);
            startDistance = finalDistance;
            steps.add(step);
        }
        
        route.setSteps(steps);
        route.setTotalDistance(totalDistance);
        
        return route;
    }
    
}
