package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateCollectFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubX;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToWarehouseY;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateNavigateToWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingImage;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForHubDump;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateNinetyDegrees;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegrees;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesV2;

public class RoutineWarehouse extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineWarehouse(){
        itinerary.add(StatePushOff.class);
        itinerary.add(StateRotateToZeroDegreesV2.class);
        itinerary.add(StateMoveToHubX.class);
        itinerary.add(StateRotateForHubDump.class);
        itinerary.add(StateReverseToHubUsingImage.class);
        itinerary.add(StateDumpFreight.class);
        itinerary.add(StatePushOff.class);
        itinerary.add(StateRotateToZeroDegreesV2.class);
        itinerary.add(StateNavigateToWarehouse.class);
        itinerary.add(StateRotateToCollect.class);
        itinerary.add(StateCollectFreight.class);
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
