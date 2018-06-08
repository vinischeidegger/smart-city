package com.scheideggergroup.test.core.model;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.scheideggergroup.core.model.Coordinate;

public class CoordinateTests {

    private static final double MIN_LATITUDE_VALUE = -90;
    private static final double MAX_LATITUDE_VALUE = 90;
    
    private static final double MIN_LONGITUDE_VALUE = -180;
    private static final double MAX_LONGITUDE_VALUE = 180;
    
    private static final double PRECISION = 0.00001;
    
	@Test
	public void ValidLatitudeAndLongitudeTest(){
	    
	    double decimalDigits = Math.log10(PRECISION);
	    double multiplier = Math.pow(10, -decimalDigits);
	    
	    int minIntLatitude = Double.valueOf(MIN_LATITUDE_VALUE * multiplier).intValue();
	    int maxIntLatitude = Double.valueOf(MAX_LATITUDE_VALUE * multiplier).intValue();
        double randomLatitude = Double.valueOf(ThreadLocalRandom.current().nextInt(minIntLatitude, maxIntLatitude + 1)) / multiplier;

        int minIntLongitude = Double.valueOf(MIN_LONGITUDE_VALUE * multiplier).intValue();
        int maxIntLongitude = Double.valueOf(MAX_LONGITUDE_VALUE * multiplier).intValue();
        double randomLongitude = Double.valueOf(ThreadLocalRandom.current().nextInt(minIntLongitude, maxIntLongitude + 1)) / multiplier;
		
		Coordinate coordMin = new Coordinate(MIN_LATITUDE_VALUE, MIN_LONGITUDE_VALUE);
        Coordinate coordMax = new Coordinate(MAX_LATITUDE_VALUE, MAX_LONGITUDE_VALUE);
        Coordinate coordRdm = new Coordinate(randomLatitude, randomLongitude);

		Assert.assertTrue("Random Latitude is greater than 90", randomLatitude <= MAX_LATITUDE_VALUE);
        Assert.assertTrue("Random Latitude is less than -90", randomLatitude >= MIN_LATITUDE_VALUE);
        Assert.assertTrue("Random Longitude is greater than 180", randomLongitude <= MAX_LONGITUDE_VALUE);
        Assert.assertTrue("Random Longitude is less than -180", randomLongitude >= MIN_LONGITUDE_VALUE);
        
        Assert.assertNotNull("Problems creating minimum possible coordinate", coordMin);
        Assert.assertNotNull("Problems creating maximum possible coordinate", coordMax);
        Assert.assertNotNull("Problems creating random coordinate", coordRdm);
	}

   @Test
   public void InvalidLatitudeAndLongitudeShouldThrowErrorTest(){
        
        double randomValidLatitude = ThreadLocalRandom.current().nextDouble(MIN_LATITUDE_VALUE, MAX_LATITUDE_VALUE + 1);

        double randomValidLongitude = ThreadLocalRandom.current().nextDouble(MIN_LONGITUDE_VALUE, MAX_LONGITUDE_VALUE + 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coordinate(MIN_LATITUDE_VALUE - PRECISION, randomValidLongitude));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coordinate(randomValidLatitude, MIN_LONGITUDE_VALUE - PRECISION));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coordinate(MAX_LATITUDE_VALUE + PRECISION, randomValidLongitude));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coordinate(randomValidLatitude, MAX_LONGITUDE_VALUE + PRECISION));
    }

}
