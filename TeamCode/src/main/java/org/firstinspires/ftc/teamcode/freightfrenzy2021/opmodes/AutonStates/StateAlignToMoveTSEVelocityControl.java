package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateAlignToMoveTSEVelocityControl extends EbotsAutonStateVelConBase{

    /**
     * Within this constructor the following variables must be set:
     * @implSpec travelDistance
     * @implSpec travelFieldHeading
     * @implSpec targetHeadingDeg

     * @param autonOpMode
     */
    public StateAlignToMoveTSEVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define

        BarCodePosition barCodePosition = autonOpMode.getBarCodePosition();
        double multiplier = 0.0;
        if (barCodePosition == BarCodePosition.MIDDLE){
            multiplier = 1.0;
        } else if( barCodePosition == BarCodePosition.RIGHT){
            multiplier = 2.0;
        }
        travelDistance = 8.4 * multiplier;
        travelFieldHeadingDeg = 0.0;
        targetHeadingDeg = 90.0;

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
