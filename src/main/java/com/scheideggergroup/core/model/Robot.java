package com.scheideggergroup.core.model;

public class Robot {

    private int speed;
    private int refreshRate;
    private Coordinate currentCoordinate;
    private Route currentRoute;
    
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getRefreshRate() {
        return refreshRate;
    }
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }
    public boolean isBeingUsed() {
        return currentRoute != null;
    }
    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }
    public void setCurrentCoordinate(Coordinate currentCoordinate) {
        this.currentCoordinate = currentCoordinate;
    }
    public Route getCurrentRoute() {
        return currentRoute;
    }
    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }
    
    /*
     * InMetersPerSecond
     */
}
