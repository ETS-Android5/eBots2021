package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateCollectFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarouselWithDeadReckoningNav;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToWall;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubXWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubY;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateNavigateToWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateParkInStorageUnit;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToCarouselWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingImage;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotate180;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForHubDump;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToFieldCenter;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesV2;

public class RoutineCarousel extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineCarousel(){

        itinerary.add(StatePushOffWithEncoders.class);
//        if (AllianceSingleton.isBlue()) {
//            itinerary.add(StatePushOffWithEncoders.class);
//        }
        itinerary.add(StateRotateForDeliverDuck.class);
        itinerary.add(StateReverseToCarouselWithEncoders.class);
        itinerary.add(StateDeliverDuck.class);
        itinerary.add(StatePushOffWithEncoders.class);
        itinerary.add(StateRotateToFieldCenter.class);
        itinerary.add(StateMoveToHubY.class);
        // can be removed if manage field heading during travel
        itinerary.add(StateRotate180.class);
        itinerary.add(StateReverseToHubUsingEncoders.class);
        itinerary.add(StateDumpFreight.class);
        itinerary.add(StatePushOffWithEncoders.class);
        // can be removed if manage field heading during travel
        itinerary.add(StateRotate180.class);
        itinerary.add(StateDriveToWall.class);
        itinerary.add(StateParkInStorageUnit.class);

//        itinerary.add(StateRotateToZeroDegreesV2.class);
//        itinerary.add(StateNavigateToWarehouse.class);
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
