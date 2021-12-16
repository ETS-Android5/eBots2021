package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines;

import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateCollectFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarouselWithDeadReckoningNav;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToWall;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToWallVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreight;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDumpFreightV2;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubXWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubY;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubYCarouselVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateNavigateToWarehouse;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateParkInStorageUnit;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOffWithVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToCarouselUsingVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToCarouselWithEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingEncoders;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHubUsingImage;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotate180;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateForHubDump;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateTo180VelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToCollect;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToFieldCenter;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToFieldCenterVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesV2;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegreesVelocityControl;

public class RoutineCarousel extends EbotsAutonRoutine{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoutineCarousel(){

        itinerary.add(StatePushOffWithVelocityControl.class);
        itinerary.add(StateRotateToZeroDegreesVelocityControl.class);
        itinerary.add(StateReverseToCarouselUsingVelocityControl.class);
        itinerary.add(StateDeliverDuck.class);
        itinerary.add(StatePushOffWithVelocityControl.class);
        itinerary.add(StateRotateToFieldCenterVelocityControl.class);

        // raises are while moving
        itinerary.add(StateMoveToHubYCarouselVelocityControl.class);
        // can be removed if manage field heading during travel
        itinerary.add(StateRotateTo180VelocityControl.class);
        itinerary.add(StateReverseToCarouselUsingVelocityControl.class);
        itinerary.add(StateDumpFreightV2.class);
        itinerary.add(StatePushOffWithEncoders.class);
        // can be removed if manage field heading during travel
        itinerary.add(StateRotate180.class);
        itinerary.add(StateDriveToWallVelocityControl.class);
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
