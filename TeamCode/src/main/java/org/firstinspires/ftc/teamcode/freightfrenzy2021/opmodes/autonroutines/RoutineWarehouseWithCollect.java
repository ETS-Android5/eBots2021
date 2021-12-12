package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateUndoEnterWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateCollectFreightWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateEnterWarehouseForCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffAllianceHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesV2;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeAlignToWall;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeRight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeToAllianceHubAfterCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeToAllianceHubYWithOvertravel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeToTouchWall;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateStrafeToUndoOvertravel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateUndoCollectTravel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateUndoPushOff;

public class RoutineWarehouseWithCollect extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineWarehouseWithCollect(){

        itinerary.add(StatePushOffWithEncoders.class);
        itinerary.add(StateStrafeRight.class);
        // Rotate to zero and strafe to hub
        itinerary.add(StateRotateToZeroDegreesV2.class);

        // add alignment to wall here to square up
        itinerary.add(StateStrafeAlignToWall.class);

        // Over-travel a bit and reverse to make sure marker is out of the way
        itinerary.add(StateStrafeToAllianceHubYWithOvertravel.class);

        // Undo over-travel from previous state (get distance of over-travel from previous state)
        itinerary.add(StateStrafeToUndoOvertravel.class);

        // figure out why this times out
        itinerary.add(StateReverseToHubUsingEncoders.class);

        itinerary.add(StateDumpFreight.class);

        // actual clicks traveled needs to set forwardClicksPushOff during transitional actions
        itinerary.add(StatePushOffAllianceHub.class);

        // this is used to determine Y travel  set strafeClicksCollect during transitional actions
        itinerary.add(StateStrafeToTouchWall.class);

        // actual clicks traveled needs to set forwardClicksEnterWarehouse during transitional actions
        itinerary.add(StateEnterWarehouseForCollect.class);

        // alignment only, no need to pass click count to opMode
        itinerary.add(StateStrafeAlignToWall.class);

        // need heading control during this state to keep from veering off course set forwardClicksCollect during transitional actions
        itinerary.add(StateCollectFreightWithEncoders.class);

        // Must add state to back up to entry point, reverse travel during StateCollectFreightWithEncoders
        itinerary.add(StateUndoCollectTravel.class);

        // alignment only, no need for passing to opMode
        itinerary.add(StateStrafeAlignToWall.class);

        // Move back to alliance hub for strafing
        itinerary.add(StateUndoEnterWarehouse.class);

        // strafe back to the hub y position previously used
        itinerary.add(StateStrafeToAllianceHubAfterCollect.class);

        // Prepare to dump in alliance hub
        itinerary.add(StateUndoPushOff.class);

        itinerary.add(StateDumpFreight.class);

        itinerary.add(StatePushOffAllianceHub.class);
        itinerary.add(StateStrafeToTouchWall.class);
        itinerary.add(StateEnterWarehouseForCollect.class);
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
