package com.scheideggergroup.core.event;

import java.util.ArrayList;

import com.scheideggergroup.core.model.Coordinate;

public abstract class AirQualityReportPublisher {

    private String name;
    private ArrayList<AirQualityReportListener> airQualityReportListeners;

    /**
     * Creates a new instance of the AirQualityReportPublisher class.
     */
    public AirQualityReportPublisher() {
        this.airQualityReportListeners = new ArrayList<AirQualityReportListener>();
    }

    /**
     * Gets the name of the source of the report.
     * @return
     */
    public String getSourceName() {
        return this.name;
    }
    
    /**
     * Sets the name of the source of the report.
     * @param name
     */
    public void setSourceName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the publisher Geographical Location.
     * @return
     */
    public abstract Coordinate getGeoLocation();

    /**
     * Gets the list of air quality report listeners.
     * @return
     */
    public ArrayList<AirQualityReportListener> getAirQualityReportListeners() {
        return airQualityReportListeners;
    }

    /**
     * Register a Robot Air Quality Report Listener.
     * @param listener
     */
    public void registerAirQualityReportListener(AirQualityReportListener listener) {
        this.airQualityReportListeners.add(listener);
    };

    /**
     * Unregister a Robot Air Quality Report Listener.
     * @param listener
     */
    public void unregisterAirQualityReportListener(AirQualityReportListener listener) {
        this.airQualityReportListeners.remove(listener);
    }


}
