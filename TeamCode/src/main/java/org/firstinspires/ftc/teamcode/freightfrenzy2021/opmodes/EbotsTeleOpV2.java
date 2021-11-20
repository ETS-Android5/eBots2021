package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.annotation.SuppressLint;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsenums.RobotSide;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsColorSensor;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsWebcam;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Arm;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.FieldOrientedDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.MecanumDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines.FreightDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp
public class EbotsTeleOpV2 extends LinearOpMode {

    private EbotsMotionController motionController;
    private StopWatch lockoutStopWatch = new StopWatch();
    private StopWatch endGameStopWatch = new StopWatch();
    private Telemetry.Item zeroHeadingItem = null;
    private Intake intake;
    private Carousel carousel;
    public Bucket bucket;
    private Arm arm;
    private boolean endGameRumbleIssued;
    private boolean justDumped = false;
    private OpenCvCamera camera;
    private String logTag = "EBOTS";
    private FreightDetector freightDetector;
    private boolean freightLoaded = false;
    private StopWatch stopWatchFreightRumble = new StopWatch();
    private long freightRumbleTimeLimit = 1500L;



    public void setJustDumped(boolean justDumped) {
        this.justDumped = justDumped;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        endGameRumbleIssued = false;
        intake = new Intake(hardwareMap);
        carousel = new Carousel(hardwareMap);
        bucket = new Bucket(this);
        arm = new Arm(this);

        motionController = EbotsMotionController.get(MecanumDrive.class, this);
        EbotsWebcam bucketWebCam = new EbotsWebcam(hardwareMap, "bucketCam", RobotSide.FRONT, 0,-3.25f, 9.0f);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        Log.d(logTag, "cameraMonitorViewId set");

        WebcamName webcamName = bucketWebCam.getWebcamName();
        // With live preview
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        Log.d(logTag, "camera instantiated");
        freightDetector = new FreightDetector();
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                Log.d(logTag, "The camera is now open...");
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


        while (! this.isStarted()){
            this.handleUserInput(gamepad1);
            rumbleIfFreightPresent();
            updateTelemetry();
        }

        waitForStart();
        endGameStopWatch.reset();

        while (opModeIsActive()){

            rumbleIfEndGame();
            rumbleIfFreightPresent();
            rotateBucketIfArmAtBottom();

            this.handleUserInput(gamepad1);
            motionController.handleUserInput(gamepad1);
            intake.handleUserInput(gamepad2);
            carousel.handleUserInput(gamepad2);
            bucket.handleUserInput(gamepad2);
            if(this.justDumped) {
                arm.moveToLevel(Arm.Level.ONE);
                justDumped = false;
            }
            arm.handleUserInput(gamepad2);

            updateTelemetry();
        }
    }

    private void rumbleIfEndGame() {
        if (endGameStopWatch.getElapsedTimeSeconds() >= 89 && !endGameRumbleIssued){
            gamepad1.rumble(1000);
            gamepad2.rumble(1000);
            endGameRumbleIssued = true;
        }
    }

    private void rumbleIfFreightPresent(){
        //Log.d(logTag, "Inside rumbleIfFreightPresent....");
        if(bucket.getBucketState() == BucketState.COLLECT){
//            freightLoaded = freightDetector.getIsBox() | freightDetector.getIsBall();
            freightLoaded = freightDetector.getIsBox();
            boolean freightRumbleLockedOut = stopWatchFreightRumble.getElapsedTimeMillis() < freightRumbleTimeLimit;
            //Log.d(logTag, "BucketState: " + bucket.getBucketState() + " freightLoaded: " + freightLoaded +
            //        " freightRumbleLockedOut: " + freightRumbleLockedOut);
            if(freightLoaded && !freightRumbleLockedOut){
                gamepad1.rumble(250);
                gamepad2.rumble(250);
                freightDetector.markReadingAsConsumed();
                freightLoaded = false;
                stopWatchFreightRumble.reset();
            }
        }
    }

    private void updateTelemetry() {
        String twoDecimals = "%.2f";
        Telemetry.Item zeroHeadingLine = null;
        telemetry.addData("Motion Controller", motionController.getName());
        telemetry.addData("Carousel Speed (fmt)", String.format(twoDecimals, carousel.getSpeed()));
        telemetry.addData("Intake Speed", String.format(twoDecimals, intake.getSpeed()));
        telemetry.addData("Arm isAtBottom", arm.isAtBottom());
        telemetry.addData("Arm position", arm.getPosition());
        telemetry.addData("Arm is zeroed ", arm.getIsZeroed());
        telemetry.addData("Freight Detected ", freightDetector.getFrameConfidenceNoRed());
        telemetry.addData("Bucket Hue ", freightDetector.getAverageHue());
        telemetry.addData("Is Box ", freightDetector.getIsBox());
        telemetry.addData("Is Ball ", freightDetector.getIsBall());

        if (motionController instanceof FieldOrientedDrive){
            telemetry.addData("Field Heading", String.format(twoDecimals, ((FieldOrientedDrive) motionController).getCurrentHeadingDeg()));
            telemetry.addData("Initial Heading", String.format(twoDecimals, ((FieldOrientedDrive) motionController).getZeroHeadingDeg()));
        }
        telemetry.update();
    }

    private void handleUserInput(Gamepad gamepad){
        boolean lockoutActive = lockoutStopWatch.getElapsedTimeMillis() < 600;

        if (lockoutActive){
            return;
        }


        if(gamepad.left_bumper && gamepad.right_stick_button){
            if (motionController instanceof MecanumDrive){
                motionController = EbotsMotionController.get(FieldOrientedDrive.class, this);
            } else if (motionController instanceof FieldOrientedDrive){
                motionController = EbotsMotionController.get(MecanumDrive.class, this);
            }

            gamepad.rumble(1.0, 1.0, 400);  // 200 mSec burst on left motor.
            lockoutStopWatch.reset();
        }

    }

    private void rotateBucketIfArmAtBottom(){
        if (arm.shouldBucketCollect()) {
            bucket.setState(BucketState.COLLECT);
        }
    }
}

