package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateRotateForDeliverDuck implements EbotsAutonState{


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private EbotsAutonOpMode autonOpMode;
    private Telemetry telemetry;
    private AutonDrive motionController;
    // State used for updating telemetry
    private double targetHeadingDeg;

    long stateTimeLimit = 2000;
    StopWatch stopWatch = new StopWatch();

    private double headingErrorDeg;
    private boolean wasTargetPoseAchieved;
    private StopWatch stopWatchPoseAchieved = new StopWatch();
    private long targetDurationMillis = 250;

    private String logTag = "EBOTS";
    private boolean firstPass = true;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateRotateForDeliverDuck(EbotsAutonOpMode autonOpMode){
        Log.d(logTag, "In Constructor for StateRotateForDeliverDuck");
        this.autonOpMode = autonOpMode;
        telemetry = autonOpMode.telemetry;
        this.motionController = autonOpMode.getMotionController();
//        this.motionController = new AutonDrive(autonOpMode);
        this.motionController.setSpeed(Speed.MEDIUM);
        targetHeadingDeg = (AllianceSingleton.isBlue() ? -30 : 0);
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
    public boolean shouldExit() {
        headingErrorDeg = UtilFuncs.applyAngleBounds(targetHeadingDeg - EbotsImu.getCurrentFieldHeadingDeg(true));

        if (firstPass){
            Log.d(logTag, "heading error is " + String.format("%.2f", headingErrorDeg));
            firstPass = false;
        }
        double acceptableError = 3;

        boolean isTargetHeadingAchieved = Math.abs(headingErrorDeg) < acceptableError;

        boolean isTargetHeadingSustained = isTargetHeadingSustained(isTargetHeadingAchieved);

        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;

        updateTelemetry();

        return isTargetHeadingSustained | stateTimedOut | autonOpMode.isStopRequested();
    }

    @Override
    public void performStateActions() {
        motionController.rotateToFieldHeadingFromError(headingErrorDeg);
    }

    @Override
    public void performTransitionalActions() {
        Log.d(logTag, "Performing transitional actions for StateDeliverDuck");
        Log.d(logTag, "Current heading is: " + String.format("%.2f", EbotsImu.getCurrentFieldHeadingDeg(false)) +
                " Error is: " + (String.format("%.2f", EbotsImu.getCurrentFieldHeadingDeg(false) - targetHeadingDeg)));
        motionController.stop();

        // update robot's pose in autonOpMode
        autonOpMode.getCurrentPose().setHeadingDeg(EbotsImu.getCurrentFieldHeadingDeg(true));
        telemetry.addLine("Exiting RotateForDeliverDuck");
        telemetry.update();
        Log.d(logTag, "Exiting RotateForDeliverDuck, robot's pose: " + autonOpMode.getCurrentPose().toString());
    }

    private boolean isTargetHeadingSustained(boolean isTargetPoseAchieved){
        boolean isTargetPoseSustained = false;
        if (isTargetPoseAchieved && !wasTargetPoseAchieved){
            // if pose newly achieved
            stopWatchPoseAchieved.reset();
            wasTargetPoseAchieved = true;
        } else if (isTargetPoseAchieved && wasTargetPoseAchieved){
            // pose is achieved was correct the previous loop
            if (stopWatchPoseAchieved.getElapsedTimeMillis() >= targetDurationMillis){
                // see how long the pose has been achieved
                isTargetPoseSustained = true;
            }
        } else{
            // target pose is not achieved
            wasTargetPoseAchieved = false;
        }

        return isTargetPoseSustained;
    }

    private void updateTelemetry(){
        telemetry.addData("Current Heading", String.format("%.2f", EbotsImu.getCurrentFieldHeadingDeg(false)));
        telemetry.addData("Target Heading", targetHeadingDeg);

    }
}