package com.scheideggergroup.core.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible to store coordinates in signaled decimal degrees.
 * @author scheidv1
 *
 */
public class Coordinate {
	
    private static Logger logger = LoggerFactory.getLogger(Coordinate.class);

    public static final double MIN_LATITUDE_VALUE = -90;
    public static final double MAX_LATITUDE_VALUE = 90;
    
    public static final double MIN_LONGITUDE_VALUE = -180;
    public static final double MAX_LONGITUDE_VALUE = 180;
    
    public static final double PRECISION = 0.00001;
    
    @Min(value = -90, message= "Latitude in decimal degrees cannot be less than -90.")
    @Max(value = 90, message= "Latitude in decimal degrees cannot be greater than 90.")
    private double latitude;

    @Min(value = -180, message= "Longitude in decimal degrees cannot be less than -180.")
    @Max(value = 180, message= "Longitude in decimal degrees cannot be greater than 180.")
    private double longitude;
	
	/**
	 * Creates a new instance of the Coordinate class.
	 * @param latitude The latitude of the coordinate in signaled decimal degrees.
	 * @param longitude
	 */
	public Coordinate(double latitude, double longitude) {
		super();
		testLatitude(latitude);
        testLongitude(longitude);
        logger.trace(String.format("Creating a new coordinate with %1$+10.5f %2$+10.5f", latitude, longitude));
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Gets the latitude of the coordinate in signaled decimal degrees.
	 * @return a double with the latitude of the coordinate in signaled decimal degrees.
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude of the coordinate in signaled decimal degrees.
	 * @param latitude The latitude to be set of the coordinate in signaled decimal degrees.
	 */
	public void setLatitude(double latitude) {
		testLatitude(latitude);
		this.latitude = latitude;
	}
	/**
	 * Gets the longitude of the coordinate in signaled decimal degrees.
	 * @return a double with the longitude of the coordinate in signaled decimal degrees.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude of the coordinate in signaled decimal degrees.
	 * @param longitude The longitude to be set of the coordinate in signaled decimal degrees.
	 */
	public void setLongitude(double longitude) {
		testLongitude(longitude);
		this.longitude = longitude;
	}
	
	/**
	 * Checks whether the latitude is valid.
	 * @param latitude The latitude to be set of the coordinate in signaled decimal degrees.
	 * @return a boolean equals True when the latitude is valid.
	 * @throws IllegalArgumentException Exception thrown when the parameter is invalid.
	 */
	public boolean testLatitude(double latitude) throws IllegalArgumentException {
		if (latitude > MAX_LATITUDE_VALUE || latitude < MIN_LATITUDE_VALUE) {
	        logger.error(String.format("Error while setting the latitude %1$+10.5f on a coordinate.", latitude));
			throw new IllegalArgumentException("Latitude in decimal degrees cannot be greater than 90.");
		}
		return true;
	}

	/**
	 * Checks whether the longitude is valid.
	 * @param longitude The longitude to be set of the coordinate in signaled decimal degrees.
	 * @return a boolean equals True when the longitude is valid.
	 * @throws IllegalArgumentException Exception thrown when the parameter is invalid.
	 */
	public boolean testLongitude(double longitude) throws IllegalArgumentException {
        if (longitude > MAX_LONGITUDE_VALUE || longitude < MIN_LONGITUDE_VALUE) {
            logger.error(String.format("Error while setting the longitude %1$+10.5f on a coordinate.", longitude));
			throw new IllegalArgumentException("Longitude in decimal degrees cannot be greater than 180.");
		}
		return true;
	}
	
    @Override
    public String toString() {
        return String.format("[Coordinate = [[latitude = %1$+10.5f], [longitude = %2$+10.5f]]]", this.latitude, this.longitude);
    }

}
