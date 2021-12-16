package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Arm;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateMoveToHubYCarouselVelocityControl extends EbotsAutonStateVelConBase{

    public StateMoveToHubYCarouselVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        boolean debugOn = true;
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Must define
        motionController.setSpeed(Speed.MEDIUM);
        boolean isBlue = AllianceSingleton.isBlue();

        travelDistance = isBlue ? 19.43 : 28.02;

        if (debugOn){
            Log.d(logTag, "travelDistance: " + String.format(twoDec, travelDistance));
        }
        // travel direction and
        travelDirectionDeg = AllianceSingleton.getDriverFieldHeadingDeg();
        targetHeadingDeg = AllianceSingleton.getDriverFieldHeadingDeg();

        initAutonState();
        setDriveTarget();
        moveArmToTargetLevel();

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

    private void moveArmToTargetLevel() {
        BarCodePosition barCodePosition = autonOpMode.getBarCodePosition();
        Arm.Level targetLevel = Arm.Level.ONE;
        if (barCodePosition == BarCodePosition.MIDDLE) {
            targetLevel = Arm.Level.TWO;
        } else if (barCodePosition == BarCodePosition.RIGHT) {
            targetLevel = Arm.Level.THREE;
        }
        Arm arm = Arm.getInstance(autonOpMode);
        arm.moveToLevel(targetLevel);
    }
}
