package com.scheideggergroup.core.event;

import com.scheideggergroup.core.model.RobotMovementArgs;

public interface RobotMovementListener {
    
    public void onRobotMovement(Object sender, RobotMovementArgs args);

}
