package com.scheideggergroup.core.model;

import java.time.LocalDateTime;

/**
 * The representation of the moving robot.
 * @author scheidv1
 *
 */
public class Robot {

    private int speed;
    private LocalDateTime lastMeasuredTimestamp;
    private Coordinate lastMeasuredCoordinate;
    private Route currentRoute;
    private double traveledDistance;
    
    /**
     * Gets Robot Speed in meters per second.
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets Robot Speed in meters per second.
     * @return
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets robot last measured time stamp.
     * @return
     */
    public LocalDateTime getLastMeasuredTimestamp() {
        return lastMeasuredTimestamp;
    }

    /**
     * Sets robot last measured time stamp.
     * @return
     */
    public void setLastMeasuredTimestamp(LocalDateTime lastMeasuredTimestamp) {
        this.lastMeasuredTimestamp = lastMeasuredTimestamp;
    }

    /**
     * Checks if the Robot is being used (if there is route assigned to it).
     * @return
     */
    public boolean isBeingUsed() {
        return currentRoute != null;
    }

    /**
     * Gets robot last measured coordinate.
     * @return
     */
    public Coordinate getLastMeasuredCoordinate() {
        return lastMeasuredCoordinate;
    }
    
    /**
     * Sets the robot last measured coordinate.
     * @param lastMeasuredCoordinate
     */
    public void setLastMeasuredCoordinate(Coordinate lastMeasuredCoordinate) {
        this.lastMeasuredCoordinate = lastMeasuredCoordinate;
    }
    
    /**
     * Gets robot current route.
     * @return
     */
    public Route getCurrentRoute() {
        return currentRoute;
    }
    
    /**
     * Sets robot current route.
     * @param currentRoute
     */
    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }

    /**
     * Gets robot traveled distance on current route.
     * @return
     */
    public double getTraveledDistance() {
        return traveledDistance;
    }

    /**
     * Sets robot traveled distance on current route.
     * @return
     */
    public void setTraveledDistance(double traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

}
