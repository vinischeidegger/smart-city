package com.scheideggergroup.core.model;

import java.util.List;

public class Route {

	private List<Coordinate> path;
    private List<Step> steps;
    private double totalDistance;

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
}
