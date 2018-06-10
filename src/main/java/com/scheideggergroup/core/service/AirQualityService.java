package com.scheideggergroup.core.service;

import java.util.List;

import com.scheideggergroup.core.model.AirQualityMeasure;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;

/**
 * Interface of the Air Quality Service layer
 * @author scheidv1
 *
 */
public interface AirQualityService {
    
    /**
     * Gets the pm25 reading for the air quality.
     * @return
     */
    public double getAirQualityReading();
    
    /**
     * Transforms the pm25 reading for the air quality into a string represent the air quality level.
     * @return
     */
    public String getQualityLevelStringFromPm25Value(double pm25);

    /**
     * Returns a Runnable to read the particle level.
     * @param workRobot The robot reading the particles
     * @param workRoute The route, as the robot is not thread safe.
     * @param workSteps The steps from the route, as the route is not thread safe.
     * @param airQualityMeasurements The list of air quality measurements to update.
     * @param distanceBetweenMeasures The distance in meters between measurments.
     * @return
     */
    public Runnable readParticleLevel(Robot workRobot, Route workRoute, List<Step> workSteps,
            List<AirQualityMeasure> airQualityMeasurements, double distanceBetweenMeasures);

    /**
     * Returns a Runnable to publish the Air Quality Average Level for a given robot. 
     * @param robot The robot that collected the readings and that will publish the announce.
     * @param airQualityMeasurements The readings to be analyzed.
     * @return
     */
    public Runnable publishAirQualityAverageLevel(Robot robot, List<AirQualityMeasure> airQualityMeasurements);

    public void publishReportFromAllStationsAndRobot(List<MonitoringStation> stations, Robot robot);

}
