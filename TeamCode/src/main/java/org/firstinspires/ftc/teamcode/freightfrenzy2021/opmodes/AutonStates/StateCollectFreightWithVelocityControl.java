package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsenums.RobotSide;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsBlinkin;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsWebcam;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveToEncoderTarget;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines.FreightDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class StateCollectFreightWithVelocityControl extends EbotsAutonStateVelConBase{
    FreightDetector freightDetector;
    boolean freightPresent = false;
    OpenCvCamera camera;



    public StateCollectFreightWithVelocityControl(EbotsAutonOpMode autonOpMode){
        super(autonOpMode);
        Log.d(logTag, "Entering " + this.getClass().getSimpleName() + " constructor");

        // Make sure the bucket is ready for collect
        Bucket bucket = Bucket.getInstance(autonOpMode);
        if (bucket.getBucketState() != BucketState.COLLECT) {
            bucket.setState(BucketState.COLLECT);
        }

        motionController.setSpeed(Speed.SLOW);

        // Must define

        travelDistance = 8.0;
        travelFieldHeadingDeg = 0.0;
        targetHeadingDeg = 0.0;

        initAutonState();
        setDriveTarget();

        Log.d(logTag, "Constructor complete");


        stateTimeLimit = 3500;


        Intake.getInstance(autonOpMode.hardwareMap).fullPower();
        freightDetector = new FreightDetector();

        // turn on the lights
        EbotsBlinkin.getInstance(autonOpMode.hardwareMap).lightsOn();

        // receive a reference to Freight detector instead of this code.
        EbotsWebcam bucketWebCam = new EbotsWebcam(autonOpMode.hardwareMap, "bucketCam", RobotSide.FRONT, 0,-3.25f, 9.0f);
        WebcamName webcamName = bucketWebCam.getWebcamName();
        // With live preview
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        Log.d(logTag, "camera instantiated");
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                Log.d(logTag, "The camera is now open..." + stopWatchState.toString());
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(freightDetector);

            }
            @Override
            public void onError(int errorCode)
            {
                Log.d(logTag, "There was an error");
            }
        });
        Log.d(logTag, "Camera for Freight Detector Instantiated");

        Log.d(logTag, "Constructor complete");

    }

    private void updateFreightPresent(){
        if (!freightDetector.isReadingConsumed()) {
            freightPresent = freightDetector.getIsBall() | freightDetector.getIsBox();
            freightDetector.markReadingAsConsumed();
            if (freightDetector.getIsBall()) Log.d(logTag, "!!! Ball Detected !!!");
            if (freightDetector.getIsBox()) Log.d(logTag, "!!! Box Detected !!!");
        }
        telemetry.addData("Box Present", freightDetector.getIsBox());
        telemetry.addData("Ball Present", freightDetector.getIsBall());
    }


    @Override
    public boolean shouldExit() {
        // standardExitConditions include opMode inactivated, travel complete, state timed out.
        boolean standardExitConditions = super.shouldExit();
        updateFreightPresent();

        if(freightPresent) Log.d(logTag, "Exiting because freight detected during " + this.getClass().getSimpleName());

        return freightPresent | standardExitConditions;
    }

    @Override
    public void performStateActions() {
        super.performStateActions();
    }

    @Override
    public void performTransitionalActions() {
        super.performTransitionalActions();
        EbotsBlinkin.getInstance(autonOpMode.hardwareMap).lightsOff();

        // Now pass the strafe clicks to the opmode for processing
        int avgClicksTraveled = motionController.getAverageClicks();
        autonOpMode.setForwardClicksCollect(avgClicksTraveled);
        Log.d(logTag, "Setting forwardClicksCollect to " + String.format("%d", avgClicksTraveled));

        // move the bucket to travel position
        Bucket.getInstance(autonOpMode). setState(BucketState.TRAVEL);

        telemetry.addLine("Exiting " + this.getClass().getSimpleName());
        telemetry.update();
    }
}
