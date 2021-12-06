package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsutil.FieldPosition;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.ebotsutil.PoseError;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDriveVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StatePushOffWithVelocityControl implements EbotsAutonState{
    private EbotsAutonOpMode autonOpMode;
    private Telemetry telemetry;

    private int targetClicks;
    private long stateTimeLimit;
    private StopWatch stopWatchState;
    private StopWatch stopWatchLoop;
    private long loopDuration = 0L;

    private AutonDriveVelocityControl motionController;

    private String logTag = "EBOTS";
    private boolean firstPass = true;
    private double travelDistance = 12.0;
    private double travelDirectionDeg = 0;
    private double targetHeadingDeg = 0;

    private Pose currentPose;
    private Pose targetPose;
    private PoseError poseError;

    private int lastAvgClicks = 0;


    public StatePushOffWithVelocityControl(EbotsAutonOpMode autonOpMode){
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;
        motionController = new AutonDriveVelocityControl(autonOpMode);

        currentPose = autonOpMode.getCurrentPose();
        travelDistance = 12.0;
        travelDirectionDeg = 0;
        targetHeadingDeg = currentPose.getHeadingDeg();

        double xTravel = Math.cos(Math.toRadians(travelDirectionDeg)) * travelDistance;
        double yTravel = Math.sin(Math.toRadians(travelDirectionDeg)) * travelDistance;
        FieldPosition requestedTravel = new FieldPosition(xTravel, yTravel);
        FieldPosition targetFieldPosition = currentPose.getFieldPosition().offsetFunc(requestedTravel);
        targetPose = new Pose(targetFieldPosition, targetHeadingDeg);
        poseError = new PoseError(currentPose, targetPose, autonOpMode);

        stateTimeLimit = (long) motionController.calculateTimeLimitMillis(poseError);
        stopWatchState = new StopWatch();
        stopWatchLoop = new StopWatch();
        // Note, the auton motion controller is limited to travel in 4 cardinal directions
        motionController.setEncoderTarget(travelDistance, travelDirectionDeg);
        Log.d(logTag, "Constructor complete");

    }

    @Override
    public boolean shouldExit() {
        if(firstPass){
            Log.d(logTag, "Inside shouldExit...");
            firstPass = false;
        }
        updateLocationAndError();

        boolean stateTimedOut = stopWatchState.getElapsedTimeMillis() > stateTimeLimit;
        boolean targetTravelCompleted = motionController.isTargetReached();
        if (stateTimedOut) Log.d(logTag, "Exited because timed out. ");
        if(targetTravelCompleted) Log.d(logTag, "Exited because travel completed");
        if(!autonOpMode.opModeIsActive()) Log.d(logTag, "Exited because opMode is no longer active");
        return stateTimedOut | targetTravelCompleted | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        motionController.calculateDriveFromError(poseError);
        telemetry.addData("Avg Clicks", motionController.getAverageClicks());
        telemetry.addData("Position Reached", motionController.isTargetReached());
        telemetry.addLine(stopWatchState.toString());
    }

    @Override
    public void performTransitionalActions() {
        Log.d(logTag, "Inside transitional Actions...");
        motionController.stop();
        motionController.logAllEncoderClicks();
        updateLocationAndError();
        Log.d(logTag, "Pose when exiting " + this.getClass().getSimpleName() + ": "
                + autonOpMode.getCurrentPose().toString());

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }

    private void updateLocationAndError(){
        boolean debugOn = true;

        // Manage the loop duration for integrator error term
        loopDuration = stopWatchLoop.getElapsedTimeMillis();
        stopWatchLoop.reset();

        // get the current avg clicks and figure out translation distance
        int currentAvgClicks = motionController.getAverageClicks();
        double translationInches = (currentAvgClicks - lastAvgClicks) / motionController.getClicksPerInch();

        // calculate change in field position
        double xTravel = Math.cos(Math.toRadians(travelDirectionDeg)) * translationInches;
        double yTravel = Math.sin(Math.toRadians(travelDirectionDeg)) * translationInches;
        FieldPosition currentTravel = new FieldPosition(xTravel, yTravel);

        // offset currentPose field position
        currentPose.getFieldPosition().offsetInPlace(currentTravel);
        poseError.calculateError(currentPose, targetPose, loopDuration);

        if (debugOn){
            Log.d(logTag, currentPose.toString());
            Log.d(logTag, poseError.toString());
        }
    }
}
