package com.scheideggergroup.core.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.scheideggergroup.core.event.AirQualityReportArgs;
import com.scheideggergroup.core.event.AirQualityReportPublisher;
import com.scheideggergroup.core.model.AirQualityMeasure;
import com.scheideggergroup.core.model.AirQualityReport;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;
import com.scheideggergroup.core.service.AirQualityService;
import com.scheideggergroup.core.service.CoordinateService;

@Service(value="airQualityService")
public class AirQualityServiceImpl implements AirQualityService {
    
    private static Logger logger = LoggerFactory.getLogger(AirQualityServiceImpl.class);

    @Autowired
    @Qualifier("coordinateService")
    CoordinateService coordService;
    
    int decimalDigits = 1;

    @Override
    public double getAirQualityReading() {
        
        int multiplier = (int) Math.pow(10, decimalDigits);
        
        int randomValue = ThreadLocalRandom.current().nextInt(
                (int)(AirQualityMeasure.PM25_MIN_MEASURED_VALUE) * multiplier,
                (int)(AirQualityMeasure.PM25_MAX_MEASURED_VALUE) * multiplier + 1);

        return (double)randomValue / multiplier;
    }

    @Override
    public String getQualityLevelStringFromPm25Value(double pm25) {
        String airQuality = null;
        if(pm25 < 0) {
            throw new IllegalArgumentException();
        } else if (pm25 >= 0 &&  pm25 <= 50 ) {
            airQuality = "Good";
        } else if (pm25 > 50 &&  pm25 <= 100 ) {
            airQuality = "Moderate";
        } else if (pm25 > 100 &&  pm25 <= 150 ) {
            airQuality = "USG";
        } else if (pm25 > 150) {
            airQuality = "Unhealthy";
        }
        return airQuality;
    }

    @Override
    public Runnable publishAirQualityAverageLevel(Robot robot, List<AirQualityMeasure> airQualityMeasurements) {
        Runnable anounceAirQualityAverageLevel = () -> {
            Instant timestamp = Instant.now();
            logger.debug("Calculating readings average.");
            String qualityLevel = null;
            
            if(airQualityMeasurements.size() > 0) {
                double sumOfValues = 0;
                int collectedQty = 0;
                
                for(AirQualityMeasure singleMeasure : airQualityMeasurements) {
                    boolean wasAlreadyCollected = singleMeasure.isCollectedForAverage();
                    if(!wasAlreadyCollected) {
                        sumOfValues += singleMeasure.getPm25();
                        collectedQty++;
                        singleMeasure.setCollectedForAverage(true);
                        logger.trace("Reading collected: " + singleMeasure);
                    }
                }
                double readingsAvg = sumOfValues / (double) collectedQty;
                qualityLevel = this.getQualityLevelStringFromPm25Value(readingsAvg);

                logger.debug("Calculated average = " + readingsAvg );
            } else {
                qualityLevel = this.getQualityLevelStringFromPm25Value(this.getAirQualityReading());
            }
            AirQualityReport report = new AirQualityReport(timestamp, robot, qualityLevel);
            
            this.publishReportFromMeasuringSource(robot, report);
        };
        return anounceAirQualityAverageLevel;
    };
    
    @Override
    public Runnable readParticleLevel(final Robot workRobot, final Route workRoute, final List<Step> workSteps,
            final List<AirQualityMeasure> airQualityMeasurements, double distanceBetweenMeasures) {
        Runnable readParticleLevel = () -> {
            
            Instant timestamp = Instant.now();

            double traveledDistance = distanceBetweenMeasures * airQualityMeasurements.size();
            Coordinate measuredPoint = coordService.getFinalPositionAfterMove(workRoute, traveledDistance);
            
            double pm25Reading = this.getAirQualityReading();
            AirQualityMeasure airQuality = new AirQualityMeasure(timestamp, measuredPoint, workRobot, pm25Reading);
            
            logger.debug("ANOUNCING THE READING AT " + traveledDistance + " METERS = " + airQuality);

            airQualityMeasurements.add(airQuality);
            
        };
        return readParticleLevel;
    }

    @Override
    public void publishReportFromAllStationsAndRobot(List<MonitoringStation> stations, Robot robot) {

        Instant timestamp = Instant.now();
        
        // Publishing from all stations
        stations.forEach((station) -> {
            double measureFromStation = this.getAirQualityReading();
            String qualityLevel = this.getQualityLevelStringFromPm25Value(measureFromStation);
            AirQualityReport report = new AirQualityReport(timestamp, station, qualityLevel);
            this.publishReportFromMeasuringSource(station, report);
         });
        
        // Publishing from the robot
        double measureFromRobot = this.getAirQualityReading();
        String qualityLevel = this.getQualityLevelStringFromPm25Value(measureFromRobot);
        AirQualityReport report = new AirQualityReport(timestamp, robot, qualityLevel);
        this.publishReportFromMeasuringSource(robot, report);
        
    }
    
    private void publishReportFromMeasuringSource(AirQualityReportPublisher reportSource, AirQualityReport report) {
        logger.debug("Publishing Report from " + reportSource.getSourceName() + " " + report);
        reportSource.getAirQualityReportListeners().forEach((listener) -> {
            AirQualityReportArgs args = new AirQualityReportArgs(report);
            listener.onAirQualityReported(this, args);
        });
    }

}
