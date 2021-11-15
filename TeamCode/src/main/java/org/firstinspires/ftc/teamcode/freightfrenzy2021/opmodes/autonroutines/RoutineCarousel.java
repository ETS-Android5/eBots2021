package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateCollectFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarouselWithDeadReckoningNav;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubXWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateNavigateToWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingImage;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForHubDump;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesV2;

public class RoutineCarousel extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineCarousel(){
//        itinerary.add(StatePushOff.class);
//        itinerary.add(StateRotateToZeroDegreesV2.class);
//        itinerary.add(StateDriveToCarousel.class);
//        itinerary.add(StateDeliverDuck.class);
//        itinerary.add(StatePushOff.class);
//        itinerary.add(StateRotateToZeroDegreesV2.class);
//        itinerary.add(StateMoveToHubXWithEncoders.class);
//        itinerary.add(StateRotateForHubDump.class);
//        itinerary.add(StateReverseToHubUsingImage.class);
//        itinerary.add(StateDumpFreight.class);
//        itinerary.add(StatePushOff.class);
//        itinerary.add(StateRotateToZeroDegreesV2.class);
//        itinerary.add(StateNavigateToWarehouse.class);
//        itinerary.add(StateRotateToCollect.class);
//        itinerary.add(StateCollectFreight.class);

        itinerary.add(StatePushOffWithEncoders.class);
        itinerary.add(StateRotateForDeliverDuck.class);
        itinerary.add(StateDriveToCarouselWithDeadReckoningNav.class);
        itinerary.add(StateDeliverDuck.class);
        itinerary.add(StatePushOffWithEncoders.class);
        itinerary.add(StateRotateToZeroDegreesV2.class);
        itinerary.add(StateMoveToHubXWithEncoders.class);
        itinerary.add(StateRotateForHubDump.class);
        itinerary.add(StateReverseToHubUsingImage.class);
        itinerary.add(StateDumpFreight.class);
        itinerary.add(StatePushOffWithEncoders.class);
        itinerary.add(StateRotateToZeroDegreesV2.class);
        itinerary.add(StateNavigateToWarehouse.class);
//        itinerary.add(StateRotateToCollect.class);
//        itinerary.add(StateCollectFreight.class);
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
