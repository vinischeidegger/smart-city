package com.scheideggergroup.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.service.CoordinateService;

public class CoordinateServiceImpl implements CoordinateService {

    private static Logger logger = LoggerFactory.getLogger(CoordinateServiceImpl.class);

    private static final double EARTH_RADIUS = 6371000;

	@Override
    public double getDistanceBetweenCoordinates(Coordinate startCoordinate, Coordinate finalCoordinate) {
		
		double lat1 = startCoordinate.getLatitude();
		double lon1 = startCoordinate.getLongitude();
		double lat2 = finalCoordinate.getLatitude();
		double lon2 = finalCoordinate.getLongitude();

		logger.debug(String.format("Calculating distance between [%1$+10.5f, %2$+10.5f] and [%3$+10.5f, %4$+10.5f]", lat1, lon1, lat2, lon2));

		double dLat = degreesToRadians(lat2-lat1);
		double dLon = degreesToRadians(lon2-lon1);
		
		lat1 = degreesToRadians(lat1);
		lat2 = degreesToRadians(lat2);
		
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = EARTH_RADIUS  * c;
		
		logger.debug(String.format("Distance is %1$.1f meters", dist));

		return dist;
    }

    @Override
    public Coordinate getFinalPositionAfterMove(Coordinate startCoordinate, double bearing, double distance) {

		double lat1 = startCoordinate.getLatitude();
		double lon1 = startCoordinate.getLongitude();

    	double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/EARTH_RADIUS) +
                Math.cos(lat1)*Math.sin(distance/EARTH_RADIUS)*Math.cos(bearing) );
    	
    	double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/EARTH_RADIUS)*Math.cos(lat1),
                     Math.cos(distance/EARTH_RADIUS)-Math.sin(lat1)*Math.sin(lat2));
    	
    	return new Coordinate(lat2, lon2);
    }

	public double getBearing(Coordinate startCoordinate, Coordinate finalCoordinate) {
		
		double lat1 = startCoordinate.getLatitude();
		double lon1 = startCoordinate.getLongitude();
		double lat2 = finalCoordinate.getLatitude();
		double lon2 = finalCoordinate.getLongitude();

		logger.debug(String.format("Calculating distance between [%1$+10.5f, %2$+10.5f] and [%3$+10.5f, %4$+10.5f]", lat1, lon1, lat2, lon2));

		double y = Math.sin(lon2-lon1) * Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) -
		        Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1);
		double bearing = radiansToDegrees(Math.atan2(y, x));

		logger.debug(String.format("Bearing is %1$.1f degrees", bearing));

		return bearing;
	}
	
	private double degreesToRadians(double degrees) {
		  return degrees * Math.PI / 180;
	}
	
	double radiansToDegrees(double radian) {
		  return radian / (Math.PI / 180);
	}


    
}
