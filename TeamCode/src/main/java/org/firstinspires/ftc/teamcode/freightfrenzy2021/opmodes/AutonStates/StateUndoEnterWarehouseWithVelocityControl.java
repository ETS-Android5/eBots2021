package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines.FreightDetector;
import org.openftc.easyopencv.OpenCvCamera;

public class StateUndoEnterWarehouseWithVelocityControl extends EbotsAutonStateVelConBase{

    public StateUndoEnterWarehouseWithVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define
        targetClicks = autonOpMode.getForwardClicksEnterWarehouse();

        travelDistance = targetClicks / motionController.getClicksPerInch();
        travelFieldHeadingDeg = 180.0;
        targetHeadingDeg = 0.0;

        initAutonState();
        setDriveTarget();

        Log.d(logTag, "Constructor complete");
    }


    @Override
    public boolean shouldExit() {
        return super.shouldExit();
    }

    @Override
    public void performStateActions() {
        super.performStateActions();
    }

    @Override
    public void performTransitionalActions() {
        super.performTransitionalActions();

    }
}
