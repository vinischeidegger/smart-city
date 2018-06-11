package com.scheideggergroup.core.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Route {

	private List<Coordinate> path;
    private List<Step> steps;
    private double totalDistance;
    //This may be moved to a "Travel" class in the future to separate behavior.
    private Instant travelStartTime;

    @JsonIgnore
	public List<Coordinate> getPath() {
		return path;
	}

	public void setPath(List<Coordinate> path) {
	    this.steps = null;
	    this.totalDistance = 0;
		this.path = path;
	}

    @JsonIgnore
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

    public Instant getTravelStartTime() {
        return travelStartTime;
    }

    public void setTravelStartTime(Instant travelStartTime) {
        this.travelStartTime = travelStartTime;
    }
}
