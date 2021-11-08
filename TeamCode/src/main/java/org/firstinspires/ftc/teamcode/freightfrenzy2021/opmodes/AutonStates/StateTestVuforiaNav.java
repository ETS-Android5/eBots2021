package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.Accuracy;
import org.firstinspires.ftc.teamcode.ebotsenums.CsysDirection;
import org.firstinspires.ftc.teamcode.ebotsutil.FieldPosition;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.ebotsutil.PoseError;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateTestVuforiaNav implements EbotsAutonState{
    private final EbotsAutonOpMode autonOpMode;
    private final Telemetry telemetry;
    private Pose currentPose;
    private Pose targetPose;
    private PoseError poseError;

    private int bufferSizeTarget = 20;
    private int scanAttempts = 0;
    private int successfulScans = 0;
    private int loopCount = 0;
    private String poseString = "";

    private boolean lastScanSuccessful = false;

    private ArrayList<Pose> poseEstimates = new ArrayList<>();

    private StopWatch stopWatch = new StopWatch();
    private Accuracy accuracy = Accuracy.STANDARD;


    String logTag = "EBOTS";

    public StateTestVuforiaNav(EbotsAutonOpMode autonOpMode){
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;
        this.targetPose = new Pose(12.0, 12.0, 90);

    }
    @Override
    public boolean shouldExit() {
        loopCount++;
        boolean userRequestExit = (autonOpMode.gamepad1.left_bumper && autonOpMode.gamepad1.right_bumper);

        poseError = new PoseError(currentPose, targetPose, autonOpMode);
        boolean targetPoseAchieved;
        return autonOpMode.isStarted() | autonOpMode.isStopRequested() | userRequestExit;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void performStateActions() {
        // Actions a) drive the robot b)
        poseString = "Insufficient data";
        scanAttempts++;

        updatePose();


        updateTelemetry();
    }


    @Override
    public void performTransitionalActions() {
        // do nothing
        poseString = "jojo";
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updatePose() {
        dropOldReadings();
        // get new poseEstimate
        Pose poseEstimate = autonOpMode.getNavigatorVuforia().getPoseEstimate();
        recordPoseEstimate(poseEstimate);
        calculateCurrentPose();
    }

    private void dropOldReadings() {
        // remove if a) buffer full or b) last scan was not successful
        boolean shouldDropReading = (!lastScanSuccessful && poseEstimates.size() > 0);
        boolean bufferFull = (poseEstimates.size() >= bufferSizeTarget);

        if(shouldDropReading | bufferFull) poseEstimates.remove(0);
    }

    private void recordPoseEstimate(Pose poseEstimate) {
        // protect for null
        if (poseEstimate == null){
            poseString = "Pose Not Detected!";
            lastScanSuccessful = false;
        } else{
            // if pose detected, add it to the poseEstimates array and update other indicators
            poseEstimates.add(poseEstimate);
            successfulScans++;
            lastScanSuccessful = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calculateCurrentPose() {
        // check if buffer is full after new observation
        boolean bufferFull = (poseEstimates.size() >= bufferSizeTarget);
        if (bufferFull){
            // create a new pose with average of buffered poseEstimates
            FieldPosition fieldPosition = calculateAverageFieldPosition(poseEstimates);
            double headingAvg = calculateAverageHeading(poseEstimates);
            currentPose = new Pose (fieldPosition, headingAvg);
            poseString = currentPose.toString();
        }
    }


    /**
     * Processes the provided array of poses and averages X and Y components a field position
     * @param poses array of poses considered estimates of currentPose
     * @return fieldPosition
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private FieldPosition calculateAverageFieldPosition(ArrayList<Pose> poses){
        // Get avg values from array of Poses for X and Y coordinates
        ArrayList<CsysDirection> csysDirections = new ArrayList<>(Arrays.asList(CsysDirection.X, CsysDirection.Y));
        // Store them temporarily in a HashMap
        Map<CsysDirection, Double> avgCoordinates= new HashMap<>();

        // Loop through the coordinates and calculate the average value
        for (CsysDirection dir: csysDirections){
            double avgValue = poses.stream()
                    .mapToDouble(p->p.getCoordinate(dir))
                    .average()
                    .orElse(0.0);
            avgCoordinates.put(dir, avgValue);
        }
        // Create a field position
        FieldPosition fieldPosition = new FieldPosition(avgCoordinates.get(CsysDirection.X), avgCoordinates.get(CsysDirection.Y));

        return fieldPosition;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private double calculateAverageHeading(ArrayList<Pose> poses){
        // The wrap-around condition for angle causes issues for simple averaging of angle
        // Example:  Heading1 = 179.9, Heading2 = -179.9.  The mathematical average is 0 but avg heading should be 180
        // To handle this, must perform the following:
        // a) decompose each angle to unit vector X and Y coordinates
        // b) Add them together
        // c) Take atan2 to determine angle


        // a) decompose each angle to unit vector X and Y coordinates
        // b) Add them together
        double xComponentSums = poses.stream()
                .mapToDouble(h -> Math.sin(h.getHeadingRad()))
                .reduce(0, (subtotal, element) -> subtotal + element);
        double yComponentSums = poses.stream()
                .mapToDouble(h -> Math.cos(h.getHeadingRad()))
                .reduce(0, (subtotal, element) -> subtotal + element);

        // c) Take atan2
        double resultantAngle = Math.toDegrees(Math.atan2(xComponentSums, yComponentSums));
        return resultantAngle;

    }

//    /**
//     * Target pose is achieved if the position and heading are met and
//     * @return boolean: isTargetPoseAchieved
//     */
//    private boolean isTargetPoseAchieved(){
//        boolean isTargetPoseAchieved = true;
//        // check position and heading
//        boolean isPositionAchieved = poseError.getMagnitude() < accuracy.getPositionalAccuracy();
//        boolean isHeadingAchieved = Math.abs(poseError.getHeadingErrorDeg()) < accuracy.getHeadingAccuracyDeg();
//        isTargetPoseAchieved = isTargetPoseAchieved && isPositionAchieved && isHeadingAchieved;
//
//        // check the error sums if active
//        for (CsysDirection dir: CsysDirection.values()) {
//            if (dir == CsysDirection.Z) continue;  // skip if Z axis
//            if (motionController.getSpeed().isIntegratorOn(dir)) {
//                // only check if integrator is on
//                boolean isIntegratorUnwound = Math.abs(poseError.getErrorSumComponent(dir)) <= accuracy.getTargetIntegratorUnwind(dir);
//                isHeadingAchieved = isHeadingAchieved && isIntegratorUnwound;
//            }
//        }
//        return isTargetPoseAchieved;
//    }
//
//    private boolean isTargetPoseSustained(boolean isTargetPoseAchieved){
//        long targetDurationMillis = 750;
//        boolean isTargetPoseSustained = false;
//
//        if (isTargetPoseAchieved && !wasTargetPoseAchieved){
//            // if pose newly achieved
//            stopWatchPoseAchieved.reset();
//            wasTargetPoseAchieved = true;
//        } else if (isTargetPoseAchieved && wasTargetPoseAchieved){
//            if (stopWatchPoseAchieved.getElapsedTimeMillis() >= targetDurationMillis){
//                isTargetPoseSustained = true;
//            }
//        } else{
//            wasTargetPoseAchieved = false;
//        }
//        return isTargetPoseSustained;
//
//    }
//
//    private boolean isNewPoseReliable(Pose newPose){
//        boolean isNewPoseReliable;
//        // see how fast the robot would have had to travel to achieve new pose
//        PoseError detectedPoseChange = new PoseError(currentPose, newPose, autonOpMode);
//        double distanceTraveled = detectedPoseChange.getMagnitude();
//        double calculatedSpeed = distanceTraveled / stopWatchVuforia.getElapsedTimeSeconds();
//        // compare that to a reasonable speed
//        double speedMultiplier = 2.5;
//        double maxReasonableSpeed = autonOpMode.getMotionController().getSpeed().getMaxSpeed() * speedMultiplier;
//        isNewPoseReliable = calculatedSpeed <= maxReasonableSpeed;
//
//        return isNewPoseReliable;
//    }
//


    private void updateTelemetry(){
        telemetry.addLine("Push Left + Right Bumper to Exit");
        telemetry.addLine("Blue Alliance Location --> (12, 72, 90)");
        String scanSuccessString = "Scan success rate: " + String.format("%.1f%%", (((float) successfulScans)/scanAttempts)*100);

        telemetry.addData("poseEstimates.size()", poseEstimates.size());
        telemetry.addLine(poseString);
//        telemetry.addData("Imu Reading", EbotsImu.getCurrentFieldHeadingDeg(true));
        telemetry.addLine(scanSuccessString);
        telemetry.addLine(stopWatch.toString(loopCount));

        Log.d(logTag, poseString);
        Log.d(logTag, scanSuccessString +
                " |=| " + stopWatch.toString(loopCount));

    }
}
