package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveToEncoderTarget;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateStrafeToTouchWallVelocityControl extends EbotsAutonStateVelConBase{
    private boolean firstPass = true;
    private boolean isSpeedSlow = false;

    private DigitalChannel frontRollerTouch;
    private DigitalChannel backRollerTouch;


    public StateStrafeToTouchWallVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        if (AllianceSingleton.isBlue()){
            frontRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "leftFrontTouch");
            backRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "leftBackTouch");
        } else {
            frontRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "rightFrontTouch");
            backRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "rightBackTouch");
        }

        // Must define

        travelDistance = 54.0;
        travelFieldHeadingDeg = AllianceSingleton.isBlue() ? 90.0 : -90.0;
        targetHeadingDeg = 0.0;

        initAutonState();
        setDriveTarget();

        stateTimeLimit += 2000L;    // add some extra time for slow travel and touch

        Log.d(logTag, "Constructor complete");
    }

    private boolean isPressed(DigitalChannel digitalChannel){
        return !digitalChannel.getState();
    }

    @Override
    public boolean shouldExit() {
        // standard conditions include opMode inactivated, travel complete, state timed out
        boolean standardExitConditions = super.shouldExit();
        boolean frontTouchPressed = isPressed(frontRollerTouch);
        boolean backTouchPressed = isPressed(backRollerTouch);
        boolean touchingWall = frontTouchPressed && backTouchPressed;
        if (frontTouchPressed) Log.d(logTag, "Front Roller is contacting wall. ");
        if (backTouchPressed) Log.d(logTag, "Back Roller is contacting wall. ");
        if (touchingWall) Log.d(logTag, "Exiting because both rollers touching ");

        return standardExitConditions| touchingWall;
    }

    @Override
    public void performStateActions() {
        super.performStateActions();
        if (!isSpeedSlow && motionController.getAverageClicks() > targetClicks*0.8) {
            motionController.setSpeed(Speed.SLOW);
            Log.d(logTag, "Shifted to slower speed for wall touch at " +
                    String.format(intFmt, motionController.getAverageClicks()) +
                    " of target travel " + String.format(intFmt, targetClicks));
            isSpeedSlow = true;
        }
    }

    @Override
    public void performTransitionalActions() {
        super.performTransitionalActions();
        Log.d(logTag, "Inside transitional Actions...");

        // Now pass the strafe clicks to the opmode for processing
        int avgClicksTraveled = motionController.getAverageClicks();
        autonOpMode.setStrafeClicksCollect(avgClicksTraveled);
        Log.d(logTag, "Setting strafe clicks to " + String.format(intFmt, avgClicksTraveled));

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }
}
