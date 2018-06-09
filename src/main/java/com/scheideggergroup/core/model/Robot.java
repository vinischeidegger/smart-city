package com.scheideggergroup.core.model;

public class Robot {

    private int speed;
    private Coordinate currentCoordinate;
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
     * Checks if the Robot is being used (if there is route assigned to it).
     * @return
     */
    public boolean isBeingUsed() {
        return currentRoute != null;
    }

    /**
     * Gets robot current coordinate.
     * @return
     */
    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }
    
    /**
     * Sets the robot current coordinate. (No thread safety).
     * @param currentCoordinate
     */
    public void setCurrentCoordinate(Coordinate currentCoordinate) {
        this.currentCoordinate = currentCoordinate;
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
