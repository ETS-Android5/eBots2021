package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface EbotsAutonState {
    public boolean shouldExit();
    public void performStateActions();
    public void performTransitionalActions();
    public static EbotsAutonState get(Class targetState, HardwareMap hardwareMap){
        EbotsAutonState newState = null;
        if (targetState == StateDriveToCarousel.class){
            newState = new StateDriveToCarousel(hardwareMap);
        } else if (targetState == StateDeliverDuck.class){
            newState = new StateDeliverDuck(hardwareMap);
        }
        return newState;
    }


}

