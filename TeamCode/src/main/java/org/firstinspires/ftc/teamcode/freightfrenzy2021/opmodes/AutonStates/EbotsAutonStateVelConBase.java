package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsutil.FieldPosition;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.ebotsutil.PoseError;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDriveVelocityControl;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public abstract class EbotsAutonStateVelConBase implements EbotsAutonState{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    protected EbotsAutonOpMode autonOpMode;
    protected Telemetry telemetry;

    protected int targetClicks;
    protected long stateTimeLimit;
    protected StopWatch stopWatchState;
    protected StopWatch stopWatchLoop;
    protected long loopDuration = 0L;

    protected AutonDrive motionController;

    protected final String logTag = "EBOTS";
    protected final String intFmt = "%d";
    protected final String oneDec = "%.1f";
    protected final String twoDec = "%.2f";

    protected boolean firstPass = true;
    protected double travelDistance = 0.0;
    protected double travelFieldHeadingDeg = 0;
    protected double targetHeadingDeg = 0;

    protected Pose currentPose;
    protected Pose targetPose;
    protected PoseError poseError;

    protected int lastAvgClicks = 0;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public EbotsAutonStateVelConBase(EbotsAutonOpMode autonOpMode){
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;
        motionController = new AutonDrive(autonOpMode);

        currentPose = autonOpMode.getCurrentPose();
//        travelDistance = 0.0;
//        travelFieldHeadingDeg = 0;
//        targetHeadingDeg = currentPose.getHeadingDeg();

        stopWatchState = new StopWatch();
        stopWatchLoop = new StopWatch();

        Log.d(logTag, "Constructor complete");

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    @Override
    public boolean shouldExit(){
        if(firstPass){
            Log.d(logTag, "Inside shouldExit of abstract class...");
            firstPass = false;
        }
        updateLocationAndError();
        return testStandardExitConditions();
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
        Log.d(logTag, "Inside transitional Actions of abstract class..." + this.getClass().getSimpleName());
        motionController.stop();
        motionController.logAllEncoderClicks();
        Log.d(logTag, "Elapsed time at exit: " + String.format(intFmt, stopWatchState.getElapsedTimeMillis()) +
                " of stateTimeLimit: " + String.format(intFmt, stateTimeLimit));
        updateLocationAndError();

        Log.d(logTag, "Average Error: " + String.format(intFmt, motionController.getAverageError()));
        Log.d(logTag, "Pose when exiting " + this.getClass().getSimpleName() + ": "
                + autonOpMode.getCurrentPose().toString());

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }

    protected void initAutonState(){
        calculateTravel();
        updateError();
        calculateTimeLimit();
    }

    protected void calculateTravel(){
        double xTravel = Math.cos(Math.toRadians(travelFieldHeadingDeg)) * travelDistance;
        double yTravel = Math.sin(Math.toRadians(travelFieldHeadingDeg)) * travelDistance;
        FieldPosition requestedTravel = new FieldPosition(xTravel, yTravel);
        FieldPosition targetFieldPosition = currentPose.getFieldPosition().offsetFunc(requestedTravel);
        targetPose = new Pose(targetFieldPosition, targetHeadingDeg);
    }

    protected void updateError(){
        poseError = new PoseError(currentPose, targetPose, autonOpMode);
    }

    protected void calculateTimeLimit(){
        stateTimeLimit = motionController.calculateTimeLimitMillis(poseError);
    }

    protected void setDriveTarget(){
        Log.d(logTag, "Setting drive target with travelDistance: " +
                String.format(twoDec, travelDistance) + " and travelFieldHeading: " +
                String.format(twoDec, travelFieldHeadingDeg));
        // Note, the auton motion controller is limited to travel in 4 cardinal directions
        motionController.setEncoderTarget(travelDistance, travelFieldHeadingDeg);
    }

    protected boolean testStandardExitConditions(){
        boolean stateTimedOut = stopWatchState.getElapsedTimeMillis() > stateTimeLimit;
        boolean targetTravelCompleted = motionController.isTargetReached();
        if (stateTimedOut) Log.d(logTag, "Exited because timed out. ");
        if (targetTravelCompleted) Log.d(logTag, "Exited because travel completed");
        if (!autonOpMode.opModeIsActive()) Log.d(logTag, "Exited because opMode is no longer active");
        return stateTimedOut | targetTravelCompleted | !autonOpMode.opModeIsActive();
    }

    protected void updateLocationAndError(){
        boolean debugOn = false;

        // Manage the loop duration for integrator error term
        loopDuration = stopWatchLoop.getElapsedTimeMillis();
        stopWatchLoop.reset();

        // get the current avg clicks and figure out translation distance
        int currentAvgClicks = motionController.getAverageClicks();
        double translationInches = (currentAvgClicks - lastAvgClicks) / motionController.getClicksPerInch();

        // calculate change in field position
        // this is a simplified calculation and only works in the 4 cardinal directions
        double xTravel = Math.cos(Math.toRadians(travelFieldHeadingDeg)) * translationInches;
        double yTravel = Math.sin(Math.toRadians(travelFieldHeadingDeg)) * translationInches;
        FieldPosition currentTravel = new FieldPosition(xTravel, yTravel);

        // offset currentPose field position
        currentPose.getFieldPosition().offsetInPlace(currentTravel);
        poseError.calculateError(currentPose, targetPose, loopDuration);

        // update last average clicks
        lastAvgClicks = currentAvgClicks;

        if (debugOn){
            Log.d(logTag, currentPose.toString());
            Log.d(logTag, poseError.toString());
        }
    }
}
