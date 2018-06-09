package com.scheideggergroup.core.model;

import java.time.LocalDateTime;

public class RobotMovementArgs {

    private Robot robot;
    private Coordinate robotCoordinate;
    private LocalDateTime eventDatetime;
    
    public RobotMovementArgs(Robot robot, Coordinate robotCoordinate, LocalDateTime eventDatetime) {
        super();
        this.robot = robot;
        this.robotCoordinate = robotCoordinate;
        this.eventDatetime = eventDatetime;
    }

    public Robot getRobot() {
        return robot;
    }
    public Coordinate getRobotCoordinate() {
        return robotCoordinate;
    }
    public LocalDateTime getEventDatetime() {
        return eventDatetime;
    }
}
