package com.scheideggergroup.core.service.impl;

import com.scheideggergroup.core.model.Robot;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.CoordinateService;
import com.scheideggergroup.core.service.GeoMoveService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="geoMoveService")
public class GeoMoveServiceImpl implements GeoMoveService {

    @Autowired
    CoordinateService coordService;

    @Override
    public void MoveRobotAlongRoute(Robot robot, Route route) {
        
        Validate.notNull(robot, "The robot has not been specified.");
        Validate.notNull(route, "The route has not been specified.");
        
        robot.setCurrentRoute(route);
        if(route.getSteps() == null) {
            
        }
        
        
        
        //Calculate all
    }

}
