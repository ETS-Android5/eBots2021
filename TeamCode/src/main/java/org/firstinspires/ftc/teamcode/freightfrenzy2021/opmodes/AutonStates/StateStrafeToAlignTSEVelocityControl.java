package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateStrafeToAlignTSEVelocityControl extends EbotsAutonStateVelConBase{

    // this static class must be created specific for this state, otherwise will fall to abstract class
    private static double stateUndoTravelDistance = 0.0;

    /**
     * Within this constructor the following variables must be set:
     * @implSpec travelDistance
     * @implSpec travelFieldHeading
     * @implSpec targetHeadingDeg

     * @param autonOpMode
     */
    public StateStrafeToAlignTSEVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define

        BarCodePosition barCodePosition = autonOpMode.getBarCodePosition();
        double multiplier = 0.0;
        if (barCodePosition == BarCodePosition.MIDDLE){
            multiplier = 1.0;
        }
        travelDistance = 4.2 * multiplier;

        stateUndoTravelDistance = 4.2 - travelDistance;
        travelFieldHeadingDeg = 0.0;
        targetHeadingDeg = AllianceSingleton.getDriverFieldHeadingDeg();


        initAutonState();
        setDriveTarget();

        Log.d(logTag, "Constructor complete");

    }

    // Must be created specific for this class to not invoke abstract class method
    public static double getStateUndoTravelDistance() {
        return stateUndoTravelDistance;
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
