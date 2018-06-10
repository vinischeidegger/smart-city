package com.scheideggergroup.core.model;

import com.scheideggergroup.core.event.AirQualityReportPublisher;

public class MonitoringStation extends AirQualityReportPublisher {
    
    private String stationName;
    private Coordinate stationCoordinate;

    public MonitoringStation(String sourceName, Coordinate stationCoordinate) {
        super();
        this.stationName = sourceName;
        this.stationCoordinate = stationCoordinate;
    }

    public Coordinate getGeoLocation() {
        return stationCoordinate;
    }

    public String getSourceName() {
        return stationName;
    }
}
