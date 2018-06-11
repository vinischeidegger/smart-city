package com.scheideggergroup.core.event;

import java.time.Instant;

import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Robot;

public class RobotMovementArgs {

    private Robot robot;
    private Coordinate robotCoordinate;
    private Instant eventDatetime;
    
    public RobotMovementArgs(Robot robot, Coordinate robotCoordinate, Instant eventDatetime) {
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
    public Instant getEventDatetime() {
        return eventDatetime;
    }
}
