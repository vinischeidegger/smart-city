package com.scheideggergroup.core.model;

import java.time.LocalDateTime;

import com.scheideggergroup.core.event.AirQualityReportPublisher;

public class AirQualityReport {
    
    private LocalDateTime timestamp;
    private Coordinate location;
    private String qualityLevel;
    private AirQualityReportPublisher source;
    
    public AirQualityReport(LocalDateTime timestamp, AirQualityReportPublisher source, String qualityLevel) {
        super();
        this.timestamp = timestamp;
        this.location = source.getGeoLocation();
        this.qualityLevel = qualityLevel;
        this.source = source;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public Coordinate getLocation() {
        return location;
    }
    public void setLocation(Coordinate location) {
        this.location = location;
    }
    public String getQualityLevel() {
        return qualityLevel;
    }
    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }
    public AirQualityReportPublisher getSource() {
        return source;
    }
    public void setSource(AirQualityReportPublisher source) {
        this.source = source;
    }
    
    @Override
    public String toString() {
        return "AirQualityReport [timestamp=" + timestamp + ", location=" + location + ", level=" + qualityLevel + ", source=" + source.getSourceName() + "]";
    }

}
