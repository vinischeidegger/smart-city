package com.scheideggergroup.core.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.scheideggergroup.core.event.AirQualityReportPublisher;
import com.scheideggergroup.core.event.RobotMovementListener;

/**
 * The representation of the moving and measuring robot.
 * @author scheidv1
 *
 */
public class Robot extends AirQualityReportPublisher {

    private final static String DEFAULT_ROBOT_NAME = "robot";
    
    private String robotName;
    private int speed;
    private LocalDateTime lastMeasuredTimestamp;
    private Coordinate lastMeasuredCoordinate;
    private Route currentRoute;
    private double traveledDistance;
    private double refreshRate;
    private double distanceBetweenMeasures;
    private double monitoringStationDistanceRange;
    private List<MonitoringStation> stationsWithinRange;
    
    private List<RobotMovementListener> robotMovementListeners;
    
    public Robot() {
        super();
        this.robotName = DEFAULT_ROBOT_NAME;
        this.robotMovementListeners = new ArrayList<RobotMovementListener>();
        this.stationsWithinRange = new ArrayList<MonitoringStation>();
    }

    /**
     * Gets Robot Speed in meters per second.
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets Robot Speed in meters per second.
     * @param speed
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
     * @param lastMeasuredTimestamp
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
    public Coordinate getGeoLocation() {
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
     * @param traveledDistance
     */
    public void setTraveledDistance(double traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

    /**
     * Gets robot name. If no name was set it simply returns "robot".
     * @return
     */
    public String getRobotName() {
        return robotName;
    }

    /**
     * Sets robot name.
     * @param robotName
     */
    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    /**
     * Gets the measuring source name. For the robot it returns the robot name. 
     * @return
     */
    @Override
    public String getSourceName() {
        return this.robotName;
    }

    /**
     * Gets the the robot movement listeners. 
     * @return
     */
    public List<RobotMovementListener> getRobotMovementListeners() {
        return robotMovementListeners;
    }

    /**
     * Gets the refresh rate in Hz
     */
    public double getRefreshRate() {
        return refreshRate;
    }

    /**
     * Sets the refresh rate in Hz
     * @param refreshRate
     */
    public void setRefreshRate(double refreshRate) {
        this.refreshRate = refreshRate;
    }

    /**
     * Gets the distance between measures
     * @return
     */
    public double getDistanceBetweenMeasures() {
        return distanceBetweenMeasures;
    }

    /**
     * Sets the distance between measures
     * @param distanceBetweenMeasures
     */
    public void setDistanceBetweenMeasures(double distanceBetweenMeasures) {
        this.distanceBetweenMeasures = distanceBetweenMeasures;
    }

    /**
     * Gets the distance range alarm from the monitoring stations.
     * @return
     */
    public double getMonitoringStationDistanceRange() {
        return monitoringStationDistanceRange;
    }

    /**
     * Sets the distance range alarm from the monitoring stations.
     * @param monitoringStationDistanceRange
     */
    public void setMonitoringStationDistanceRange(double monitoringStationDistanceRange) {
        this.monitoringStationDistanceRange = monitoringStationDistanceRange;
    }

    /**
     * Gets the list of stations within the Robot range.
     * @return
     */
    public List<MonitoringStation> getStationsWithinRange() {
        return stationsWithinRange;
    }

    /**
     * Sets the list of stations within the Robot range.
     * @param stationsWithinRange
     */
    public void setStationsWithinRange(List<MonitoringStation> stationsWithinRange) {
        this.stationsWithinRange = stationsWithinRange;
    }

    /**
     * Register a Robot Movement Listener.
     * @param listener
     */
    public void registerRobotMovementListener(RobotMovementListener listener) {
        this.robotMovementListeners.add(listener);
    };

    /**
     * Unregister a Robot Movement Listener.
     * @param listener
     */
    public void unregisterRobotMovementListener(RobotMovementListener listener) {
        this.robotMovementListeners.remove(listener);
    }

}
