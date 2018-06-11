package com.scheideggergroup.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scheideggergroup.core.controller.AirQualityRobotController;
import com.scheideggergroup.core.event.AirQualityReportArgs;
import com.scheideggergroup.core.event.RobotMovementArgs;

@Service("webSocketService")
public class WebSocketService {
    
    @Autowired
    AirQualityRobotController airQualityRobotController;

    public void updateMovementTopic(Object sender, RobotMovementArgs args) {
        airQualityRobotController.template.convertAndSend("/topic/robot", args);
    }

    public void updateReportsTopic(Object sender, AirQualityReportArgs args) {
        airQualityRobotController.template.convertAndSend("/topic/reports", args.getAirQualityReport());
    }
}
