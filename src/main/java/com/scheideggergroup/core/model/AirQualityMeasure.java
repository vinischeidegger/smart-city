package com.scheideggergroup.core.model;

import java.time.LocalDateTime;

public class AirQualityMeasure {
    
    final public static double PM25_MIN_MEASURED_VALUE = 0;
    final public static double PM25_MAX_MEASURED_VALUE = 200;
    
    public enum QUALITY_LEVELS {
        Good,
        Moderate,
        USG,
        Unhealthy
    }

    private LocalDateTime measureDateTime;
    private Coordinate location;
    private boolean collectedForAverage;
    private Robot source;
    private double pm25;
    
    public AirQualityMeasure(LocalDateTime measureDateTime, Coordinate location, Robot source, double pm25) {
        super();
        this.measureDateTime = measureDateTime;
        this.location = location;
        this.source = source;
        this.pm25 = pm25;
    }

    public boolean isCollectedForAverage() {
        return collectedForAverage;
    }

    public void setCollectedForAverage(boolean collectedForAverage) {
        this.collectedForAverage = collectedForAverage;
    }

    public LocalDateTime getMeasureDateTime() {
        return measureDateTime;
    }

    public Coordinate getLocation() {
        return location;
    }

    public Robot getSource() {
        return source;
    }

    public double getPm25() {
        return pm25;
    }
    
    @Override
    public String toString() {
        return "AirQualityMeasure [measureDateTime=" + measureDateTime + ", location=" + location + ", pm25=" + pm25 + "]";
    }
}
