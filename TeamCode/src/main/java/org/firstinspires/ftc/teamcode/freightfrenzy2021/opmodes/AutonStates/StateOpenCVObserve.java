package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines.BarCodeScanner;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsColorSensor;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class StateOpenCVObserve implements EbotsAutonState{

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private EbotsAutonOpMode autonOpMode;
    private Telemetry telemetry;
    StopWatch stopWatch = new StopWatch();
    HardwareMap hardwareMap;
    BarCodeScanner barCodeScanner;
    OpenCvCamera camera;
    String rightColor;
    String leftColor;
    private BarCodePosition observation;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateOpenCVObserve(EbotsAutonOpMode autonOpMode){
        String logTag = "EBOTS";
        this.autonOpMode = autonOpMode;
        this.telemetry = autonOpMode.telemetry;

        BarCodeObservation.resetObservations();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        // With live preview
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                Log.d(logTag, "The camera is now open...");
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(barCodeScanner);

            }
            @Override
            public void onError(int errorCode)
            {
                Log.d(logTag, "There was an error");
            }
        });

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String getColor(int hue){
        String color = "unknown";

        if (hue < 45 | hue > 150){
            color = "Red";
        } else if (hue > 60 && hue < 85){
            color = "Green";
        }
        return color;
    }


    @Override
    public boolean shouldExit() {
        //User Request Exit not implemented
        return autonOpMode.isStopRequested() | autonOpMode.isStarted();
    }

    @Override
    public void performStateActions() {
        // This accesses video stream processing occurring on a concurrent OpenCV pipeline
        // The pipeline process loops independent of the state machine
        // New observations are processed only if the pipeline reading has not been consumed
        // The pipeline process manages the consumption state of the reading in readingConsumed
        if (!barCodeScanner.isReadingConsumed()){
            int leftHue = barCodeScanner.getLeftHue();
            leftColor = getColor(leftHue);
            int rightHue = barCodeScanner.getRightHue();
            rightColor= getColor(rightHue);

            observation = BarCodePosition.RIGHT;
            if (leftColor.equals("Red")){
                observation = BarCodePosition.LEFT;
            }else if (rightColor.equals("Red")){
                observation = BarCodePosition.MIDDLE;
            }else{
                observation = BarCodePosition.RIGHT;
            }

            new BarCodeObservation(observation);

            barCodeScanner.markReadingAsConsumed();
        }

        observation = BarCodePosition.RIGHT;
        if (leftColor.equals("Red")){
            observation = BarCodePosition.LEFT;
        }else if (rightColor.equals("Red")){
            observation = BarCodePosition.MIDDLE;
        }else{
            observation = BarCodePosition.RIGHT;
        }

        new BarCodeObservation(observation);
        telemetry.addData("Observation Count", BarCodeObservation.getObservationCount());
        telemetry.addData("Camera Readings", barCodeScanner.getReadingCount());
        telemetry.addData("LeftColor", leftColor);
        telemetry.addData("RightColor", rightColor);
        telemetry.addData("Current Observation", observation.name());
        // TODO: next line should be removed after debugging
        telemetry.addData("Barcode Position ", BarCodeObservation.giveBarCodePosition().name());
    }

    @Override
    public void performTransitionalActions() {
        BarCodePosition barCodePosition = BarCodeObservation.giveBarCodePosition();
        autonOpMode.setBarCodePosition(barCodePosition);
        telemetry.addData("Barcode Position ", barCodePosition);
        camera.stopStreaming();
    }

}
