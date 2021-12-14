package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.FieldPosition;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveToEncoderTarget;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateEnterWarehouseForCollect extends EbotsAutonStateVelConBase{


    public StateEnterWarehouseForCollect(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define

        travelDistance = 24.0;
        travelFieldHeadingDeg = 0.0;
        targetHeadingDeg = 0.0;

        initAutonState();
        setDriveTarget();

        Bucket bucket = Bucket.getInstance(autonOpMode);
        bucket.setState(BucketState.COLLECT);

        Log.d(logTag, "Constructor complete");

    }

    @Override
    public boolean shouldExit() {
        return shouldExit();
    }

    @Override
    public void performStateActions() {
        performStateActions();
    }

    @Override
    public void performTransitionalActions() {
        super.performTransitionalActions();

        // Now pass the strafe clicks to the opmode for processing
        int avgClicksTraveled = motionController.getAverageClicks();
        autonOpMode.setForwardClicksEnterWarehouse(avgClicksTraveled);
        Log.d(logTag, "Setting forwardClicksEnterWarehouse to " + String.format("%d", avgClicksTraveled));

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }
}
