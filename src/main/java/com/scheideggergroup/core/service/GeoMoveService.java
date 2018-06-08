package com.scheideggergroup.core.service;

import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;

public interface GeoMoveService {

    public void MoveRobotAlongRoute(Robot robot, Route route);
    
}
