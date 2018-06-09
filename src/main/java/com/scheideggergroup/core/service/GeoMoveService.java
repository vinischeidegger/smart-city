package com.scheideggergroup.core.service;

import com.scheideggergroup.core.event.RobotMovementListener;
import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;

public interface GeoMoveService {

    /**
     * Moves the specified robot along the specified route.
     * @param robot
     * @param route
     */
    public void moveRobotAlongRoute(Robot robot, Route route);
    
    /**
     * Register a Robot Movement Listener.
     * @param listener
     */
    public void registerRobotMovementListener(RobotMovementListener listener);

    /**
     * Unregister a Robot Movement Listener.
     * @param listener
     */
    public void unregisterRobotMovementListener(RobotMovementListener listener);

    /**
     * Sets the refresh rate in Hz
     */
public void setRefreshRate(int refreshRate);

}
