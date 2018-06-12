package com.scheideggergroup.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.scheideggergroup.core.model.Polyline;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.service.MovementService;

@Controller("airQualityRobotController")
public class AirQualityRobotController {
    
    @Autowired
    @Qualifier("movementService")
    MovementService movementService;
    
    public SimpMessagingTemplate template;
    
    @Autowired
    public AirQualityRobotController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/polyline")
    public Robot moveNewAirQualityRobotAlongPolyline(Polyline polyline) throws Exception {
        return movementService.moveNewRobotAlongPolyline(polyline);
    }

    @MessageMapping("/stop")
    public void StopRobot(Robot robot) throws Exception {
        movementService.stopRobot(robot.getId());
    }
}
