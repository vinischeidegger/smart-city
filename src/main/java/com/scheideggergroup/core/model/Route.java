package com.scheideggergroup.core.model;

import java.time.LocalDateTime;
import java.util.List;

public class Route {

	private List<Coordinate> path;
    private List<Step> steps;
    private double totalDistance;
    //This may be moved to a "Travel" class in the future to separate behavior.
    private LocalDateTime travelStartTime;

	public List<Coordinate> getPath() {
		return path;
	}

	public void setPath(List<Coordinate> path) {
	    this.steps = null;
	    this.totalDistance = 0;
		this.path = path;
	}

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public LocalDateTime getTravelStartTime() {
        return travelStartTime;
    }

    public void setTravelStartTime(LocalDateTime travelStartTime) {
        this.travelStartTime = travelStartTime;
    }
}
