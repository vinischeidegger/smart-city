package com.scheideggergroup.core.event;

import com.scheideggergroup.core.model.AirQualityReport;

public class AirQualityReportArgs {
    
    private AirQualityReport airQualityReport;

    public AirQualityReportArgs(AirQualityReport airQualityReport) {
        super();
        this.airQualityReport = airQualityReport;
    }

    public AirQualityReport getAirQualityReport() {
        return airQualityReport;
    }

}
