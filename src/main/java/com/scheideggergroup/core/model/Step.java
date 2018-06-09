package com.scheideggergroup.core.model;

/**
 * A step is a part of a path, composed of a straight line between two points.
 * @author scheidv1
 *
 */
public class Step {
    
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private double distance;
    private double bearing;
    private double initialDistanceOnPath;
    private double finalDistanceOnPath;
    
    /**
     * Creates a new instance of a step.
     * @param startCoordinate - The start coordinate of the step.
     * @param endCoordinate - The end coordinate of the step.
     * @param distance - The linear distance between the step initial coordinate and its final coordinate.
     * @param bearing - The number of degrees, having the north as 0 to the final coordinate.
     */
    public Step(Coordinate startCoordinate, Coordinate endCoordinate, double distance, double bearing) {
        super();
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.distance = distance;
        this.bearing = bearing;
    }

    /**
     * Gets the step start coordinate.
     * @return
     */
    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }
    
    /**
     * Sets the step start coordinate.
     * @return
     */
    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }
    
    /**
     * Gets the end coordinate.
     * @return
     */
    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }

    /**
     * Sets the end coordinate.
     * @return
     */
    public void setEndCoordinate(Coordinate endCoordinate) {
        this.endCoordinate = endCoordinate;
    }
    
    /**
     * Gets the dintance from the start point to the end point.
     * @return
     */
    public double getDistance() {
        return distance;
    }
    
    /**
     * Sets the dintance from the start point to the end point.
     * @return
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    /**
     * Gets the bearing.
     * @return
     */
    public double getBearing() {
        return bearing;
    }

    /**
     * Sets the bearing.
     * @return
     */
    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
    
    /**
     * Gets the the amount of meters already traveled in the route previous steps.
     * @return
     */
    public double getInitialDistanceOnPath() {
        return initialDistanceOnPath;
    }

    /**
     * Sets the the amount of meters already traveled in the route previous steps.
     * @return
     */
    public void setInitialDistanceOnPath(double startDistanceOnPath) {
        this.initialDistanceOnPath = startDistanceOnPath;
    }
    
    /**
     * Gets the the amount of meters traveled in the route when the step is completed.
     * @return
     */
    public double getFinalDistanceOnPath() {
        return finalDistanceOnPath;
    }

    /**
     * Gets the the amount of meters traveled in the route when the step is completed.
     * @return
     */
    public void setFinalDistanceOnPath(double finalDistanceOnPath) {
        this.finalDistanceOnPath = finalDistanceOnPath;
    }
    
}
