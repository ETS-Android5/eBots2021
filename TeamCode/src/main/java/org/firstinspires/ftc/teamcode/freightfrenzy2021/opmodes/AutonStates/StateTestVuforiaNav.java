package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;
import java.util.OptionalDouble;

public class StateTestVuforiaNav implements EbotsAutonState{
    private final EbotsAutonOpMode autonOpMode;
    private final Telemetry telemetry;
    private Pose currentPose;

    private int scanAttempts = 0;
    private int successfulScans = 0;
    private int loopCount = 0;

    private boolean lastScanSuccessful = false;

    private ArrayList<Pose> poseEstimates = new ArrayList<>();

    private StopWatch stopWatch = new StopWatch();


    String logTag = "EBOTS";

    public StateTestVuforiaNav(EbotsAutonOpMode autonOpMode){
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;

    }
    @Override
    public boolean shouldExit() {
        loopCount++;
        boolean userRequestExit = (autonOpMode.gamepad1.left_bumper && autonOpMode.gamepad1.right_bumper);
        return autonOpMode.isStarted() | autonOpMode.isStopRequested() | userRequestExit;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void performStateActions() {
        telemetry.addLine("Push Left + Right Bumper to Exit");
        telemetry.addLine("Blue Alliance Location --> (12, 72, 90)");
        String poseString = "Insufficient data";
        scanAttempts++;

        // see if poseEstimate buffer is full
        int bufferSizeTarget = 10;

        // remove if buffer full or last scan was not successful (flushes one reading each time so need consecutive readings)
        boolean shouldDropReading = (!lastScanSuccessful && poseEstimates.size() > 0);
        boolean bufferFull = (poseEstimates.size() >= bufferSizeTarget);

        if(shouldDropReading | bufferFull) poseEstimates.remove(0);

        // get poseEstimate
        Pose poseEstimate = autonOpMode.getNavigatorVuforia().getPoseEstimate();
        // protect for null
        if (poseEstimate == null){
            poseString = "Pose Not Detected!";
            lastScanSuccessful = false;
        } else{
            successfulScans++;
            poseEstimates.add(poseEstimate);
            lastScanSuccessful = true;
        }

        // check if buffer is full after new observation
        bufferFull = (poseEstimates.size() >= bufferSizeTarget);
        if (bufferFull){
            double xAvg = poseEstimates.stream()
                    .mapToDouble(Pose::getX)
                    .average()
                    .orElse(0.0);


            double yAvg = poseEstimates.stream()
                    .mapToDouble(Pose::getY)
                    .average()
                    .orElse(0.0);

            double headingAvg = calculateAverageHeading(poseEstimates);

            currentPose = new Pose (xAvg, yAvg, headingAvg);
            // create a new pose with average of buffered poseEstimates
            poseString = currentPose.toString();
        }

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

    @Override
    public void performTransitionalActions() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private double calculateAverageHeading(ArrayList<Pose> poses){
        // The wrap-around condition for angle causes issues for simple averaging of angle
        // Example:  Heading1 = 179.9, Heading2 = -179.9.  The mathematical average is 0 but avg heading should be 180
        // To handle this, must perform the following:
        // a) decompose each angle to unit vector X and Y coordinates
        // b) Add them together
        // c) Take atan2


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
}
