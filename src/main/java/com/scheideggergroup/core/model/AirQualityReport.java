package com.scheideggergroup.core.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scheideggergroup.core.event.AirQualityReportPublisher;

public class AirQualityReport {
    
    private Instant timestamp;
    private Coordinate location;
    private String qualityLevel;
    private AirQualityReportPublisher source;
    
    public AirQualityReport(Instant timestamp, AirQualityReportPublisher source, String qualityLevel) {
        super();
        this.timestamp = timestamp;
        this.location = source.getGeoLocation();
        this.qualityLevel = qualityLevel;
        this.source = source;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public Coordinate getLocation() {
        return location;
    }
    public void setLocation(Coordinate location) {
        this.location = location;
    }
    @JsonProperty("level")
    public String getQualityLevel() {
        return qualityLevel;
    }
    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }
    public String getSource() {
        return source.getSourceName();
    }
    public void setSource(AirQualityReportPublisher source) {
        this.source = source;
    }
    
    @Override
    public String toString() {
        return "AirQualityReport [timestamp=" + timestamp + ", location=" + location + ", level=" + qualityLevel + ", source=" + source.getSourceName() + "]";
    }

}
