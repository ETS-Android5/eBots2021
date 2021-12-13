package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.RobotSize;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.FieldPosition;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveToEncoderTarget;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateStrafeToAllianceHubYWithOvertravel implements EbotsAutonState{
    private EbotsAutonOpMode autonOpMode;
    private Telemetry telemetry;

    private int targetClicks;
    private long stateTimeLimit;
    private StopWatch stopWatch;
    private DriveToEncoderTarget motionController;

    private String logTag = "EBOTS";
    private boolean firstPass = true;
    private double travelDistance;

    private final double HUB_DISTANCE_FROM_WALL = 48.0;
    private final double ROBOT_HALF_WIDTH = RobotSize.ySize.getSizeValue();
    private final double BUCKET_CENTER_OFFSET = 2.5;
    private final static double OVERTRAVEL_INCHES = 6.0;


    public StateStrafeToAllianceHubYWithOvertravel(EbotsAutonOpMode autonOpMode){
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;
        motionController = new DriveToEncoderTarget(autonOpMode);
        int allianceSign = (AllianceSingleton.isBlue()) ? 1 : -1;

        // because the bucket position is asymmetrical, the drive distance from the wall must
        // be adjusted based on alliance.  If red, subtract from travel distance.  add if blue

        travelDistance = HUB_DISTANCE_FROM_WALL - ROBOT_HALF_WIDTH +
                (BUCKET_CENTER_OFFSET * allianceSign) + OVERTRAVEL_INCHES;

        targetClicks = UtilFuncs.calculateTargetClicks(travelDistance);

        double maxTranslateSpeed = Speed.MEDIUM.getMeasuredTranslateSpeed();
        stateTimeLimit = (long) (travelDistance / maxTranslateSpeed + 2000);
        stopWatch = new StopWatch();

        motionController.strafe(-90 * allianceSign, targetClicks);
        Log.d(logTag, "Constructor complete");

    }

    public static double getOvertravelInches() {
        return OVERTRAVEL_INCHES;
    }

    @Override
    public boolean shouldExit() {
        if(firstPass){
            Log.d(logTag, "Inside shouldExit...");
            firstPass = false;
        }
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() > stateTimeLimit;
        boolean targetTravelCompleted = motionController.isTargetReached();
        if (stateTimedOut) Log.d(logTag, "Exited because timed out. ");
        return stateTimedOut | targetTravelCompleted | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        telemetry.addData("Avg Clicks", motionController.getAverageClicks());
        telemetry.addData("Position Reached", motionController.isTargetReached());
        telemetry.addLine(stopWatch.toString());
    }

    @Override
    public void performTransitionalActions() {
        Log.d(logTag, "Inside transitional Actions...");
        motionController.stop();
        motionController.logAllEncoderClicks();
        Log.d(logTag, "Pose before offset: " + autonOpMode.getCurrentPose().toString());

        // Update the robots pose in autonOpMode
        double currentHeadingRad = Math.toRadians(EbotsImu.getInstance(autonOpMode.hardwareMap).getCurrentFieldHeadingDeg(true));
        double xTravelDelta = travelDistance * Math.cos(currentHeadingRad);
        double yTravelDelta = travelDistance * Math.sin(currentHeadingRad);
        FieldPosition deltaFieldPosition = new FieldPosition(xTravelDelta, yTravelDelta);
        FieldPosition startingFieldPosition = autonOpMode.getCurrentPose().getFieldPosition();
        startingFieldPosition.offsetInPlace(deltaFieldPosition);
        Log.d(logTag, "Pose after offset: " + autonOpMode.getCurrentPose().toString());

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }
}
