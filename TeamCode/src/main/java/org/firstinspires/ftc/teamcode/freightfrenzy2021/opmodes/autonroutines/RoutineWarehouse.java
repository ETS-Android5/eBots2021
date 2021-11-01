package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubX;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToWarehouseY;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateNavigateToWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateNinetyDegrees;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegrees;

public class RoutineWarehouse extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineWarehouse(){
        routine.add(StatePushOff.class);
        routine.add(StateRotateToZeroDegrees.class);
        routine.add(StateMoveToHubX.class);
        routine.add(StateRotateNinetyDegrees.class);
        routine.add(StateReverseToHub.class);
        routine.add(StateRotateToZeroDegrees.class);
        routine.add(StateMoveToWarehouseY.class);
        routine.add(StateRotateToZeroDegrees.class);
        routine.add(StateNavigateToWarehouse.class);
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

}
