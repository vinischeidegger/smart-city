package com.scheideggergroup.core.model;

public class Step {
    
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private double distance;
    private double bearing;
    
    public Step(Coordinate startCoordinate, Coordinate endCoordinate, double distance, double bearing) {
        super();
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.distance = distance;
        this.bearing = bearing;
    }
    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }
    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }
    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }
    public void setEndCoordinate(Coordinate endCoordinate) {
        this.endCoordinate = endCoordinate;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getBearing() {
        return bearing;
    }
    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
    
}
