package com.scheideggergroup.core.service;

import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.model.Step;

/**
 * An interface of services used to calculate distance, directions and routes 
 * @author scheidv1
 *
 */
public interface CoordinateService {

    /**
     * Calculates the distance between Coordinates.
     * @param startCoordinate the starting coordinate.
     * @param finalCoordinate the final coordinate.
     * @return the distance.
     */
    public double getDistanceBetweenCoordinates(Coordinate startCoordinate, Coordinate finalCoordinate);
    
    /**
     * Calculates the bearing between Coordinates.
     * @param startCoordinate the starting coordinate.
     * @param finalCoordinate the final coordinate.
     * @return the bearing.
     */
    public double getBearing(Coordinate startCoordinate, Coordinate finalCoordinate);
    
    /**
     * Calculates the final position after traveling a certain distance heading in a direction (bearing).
     * @param startCoordinate the starting coordinate.
     * @param bearing the bearing.
     * @param distance the distance.
     * @return the resulting coordinate.
     */
    public Coordinate getFinalPositionAfterMove(Coordinate startCoordinate, double bearing, double distance);
    
    /**
     * Calculates the final position after traveling a certain distance on a specified route.
     * @param route The route to be traveled.
     * @param totalTraveledDistance The distance to be traveled.
     * @return The coordinate after traveling the distance. If the distance received as a parameter is 
     * greater than the the route total distance the last coordinate on the route will be used
     */
    public Coordinate getFinalPositionAfterMove(Route route, double totalTraveledDistance);
    
    /**
     * Calculates the info needed (distance and direction) for traveling from the start coordinate to the final coordinate.
     * @param startCoordinate the starting coordinate.
     * @param finalCoordinate the final coordinate.
     * @return the step object with travel info.
     */
    public Step calculateStep(Coordinate startCoordinate, Coordinate finalCoordinate);
    
    /**
     * Calculates all the info needed (distance and direction) of every step for traveling on a defined route.
     * @param route the route, containing a path.
     * @return the route with all steps calculated.
     */
    public Route calculateStepsOnRoute(Route route);

}
