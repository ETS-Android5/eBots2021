package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.ebotsutil.PoseError;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveToEncoderTarget;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateReverseToHubUsingVelocityControl extends EbotsAutonStateVelConBase{


    public StateReverseToHubUsingVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define
        motionController.setSpeed(Speed.FAST);

        StartingSide startingSide = autonOpMode.getStartingSide();
        if(startingSide==StartingSide.CAROUSEL && !AllianceSingleton.isBlue()) {
            travelDistance = 20.14;
        } else if (startingSide==StartingSide.CAROUSEL && AllianceSingleton.isBlue()){
            travelDistance = 26.58;
        } else if(startingSide==StartingSide.WAREHOUSE && !AllianceSingleton.isBlue()) {
            // starting side is Warehouse
            double additionalTravel = -1.0;
            travelDistance = additionalTravel + StateStrafeToAlignTSEVelocityControl.getStateStrafeDistance();
        } else if(startingSide==StartingSide.WAREHOUSE && AllianceSingleton.isBlue()) {
            // starting side is Warehouse
            double additionalTravel = 1.0;
            travelDistance = additionalTravel + StateStrafeToAlignTSEVelocityControl.getStateStrafeDistance();
        }
        travelFieldHeadingDeg = startingSide==StartingSide.CAROUSEL ? 0.0 : 180.0;
        targetHeadingDeg = startingSide==StartingSide.CAROUSEL ? 180.0 : 0.0;

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
