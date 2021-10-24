package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public interface EbotsAutonState {
    public boolean shouldExit();
    public void performStateActions();
    public void performTransitionalActions();
    public static EbotsAutonState get(Class targetState, HardwareMap hardwareMap, LinearOpMode opMode){
        EbotsAutonState newState = null;
        if (targetState == StateDriveToCarousel.class){
            newState = new StateDriveToCarousel(hardwareMap);
        } else if (targetState == StateDeliverDuck.class){
            newState = new StateDeliverDuck(hardwareMap);
        } else if (targetState == StateObserveBarCode.class){
            newState = new StateObserveBarCode(hardwareMap, opMode);
        }
        return newState;
    }


}

