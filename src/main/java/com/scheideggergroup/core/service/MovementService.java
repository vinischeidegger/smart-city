package com.scheideggergroup.core.service;

import java.util.List;
import java.util.concurrent.Semaphore;

import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.MonitoringStation;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;

/**
 * Interface to represent a measuring robot movement services.
 * @author scheidv1
 *
 */
public interface MovementService {

    /**
     * Moves the specified robot along the specified route.
     * @param robot
     * @param route
     */
    public void moveRobotAlongRoute(Robot robot, Route route);

    public void setMeasuringStations(List<MonitoringStation> measuringStations);

    public Runnable monitorRobotMovement(double monitoringPeriodMilliSec, Robot workRobot, Semaphore semaphore,
            Route workRoute, List<Step> workSteps, List<RobotMovementListener> listeners);
    
}
