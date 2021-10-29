package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveAndSpin;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.FieldOrientedDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.MecanumDrive;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

@TeleOp
public class HotSwapMotionController extends LinearOpMode {

    private EbotsMotionController motionController;
    private StopWatch lockoutStopWatch = new StopWatch();
    private StopWatch endGameStopWatch = new StopWatch();
    private Telemetry.Item zeroHeadingItem = null;
    private Intake intake;
    private Carousel carousel;
    private DistanceSensor distanceSensor;
    private boolean endGameRumbleIssued;


    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        endGameRumbleIssued = false;
        intake = new Intake(hardwareMap);
        carousel = new Carousel(hardwareMap);

        motionController = EbotsMotionController.get(MecanumDrive.class, hardwareMap);
        distanceSensor = hardwareMap.get(DistanceSensor.class, "backDistanceSensor");

        while (! this.isStarted()){
            handleUserInput(gamepad1);
            telemetry.addData("Motion Controller", motionController.getName());
            telemetry.addData("Current Distance", distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }

        waitForStart();
        endGameStopWatch.reset();

        while (opModeIsActive()){

            rumbleIfEndGame();

            this.handleUserInput(gamepad1);
            motionController.handleUserInput(gamepad1);
            intake.handleUserInput(gamepad2);
            carousel.handleUserInput(gamepad2);

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

    private void updateTelemetry() {
        String twoDecimals = "%.2f";
        Telemetry.Item zeroHeadingLine = null;
        telemetry.addData("Motion Controller", motionController.getName());
        telemetry.addData("Current Distance", distanceSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Carousel Speed (fmt)", String.format(twoDecimals, carousel.getSpeed()));
        telemetry.addData("Intake Speed", String.format(twoDecimals, intake.getSpeed()));
        if (motionController instanceof FieldOrientedDrive){
            zeroHeadingItem = telemetry.addData("Initial Heading Offset", ((FieldOrientedDrive) motionController).getZeroHeadingDeg());
        } else {
            if (zeroHeadingItem != null) telemetry.removeItem(zeroHeadingItem);
        }
        telemetry.update();
    }

    private void handleUserInput(Gamepad gamepad){
        boolean lockoutActive = lockoutStopWatch.getElapsedTimeMillis() < 600;

        if (lockoutActive){
            return;
        }

        if(gamepad.left_bumper && gamepad.right_stick_button){
//            if(motionController instanceof DriveAndSpin){
//                motionController = EbotsMotionController.get(TankDrive.class, hardwareMap);
//            } else if (motionController instanceof TankDrive){
//                motionController = EbotsMotionController.get(SeanMode.class, hardwareMap);
//            } else if (motionController instanceof SeanMode){
//                motionController = EbotsMotionController.get(MecanumDrive.class, hardwareMap);
            if (motionController instanceof MecanumDrive){
                motionController = EbotsMotionController.get(FieldOrientedDrive.class, hardwareMap);
            } else if (motionController instanceof FieldOrientedDrive){
                motionController = EbotsMotionController.get(MecanumDrive.class, hardwareMap);
            }
            gamepad.rumble(0.9, 0, 200);  // 200 mSec burst on left motor.
        } else if (gamepad.back && motionController instanceof FieldOrientedDrive){
            ((FieldOrientedDrive) motionController).setZeroHeadingDeg();

        }

        lockoutStopWatch.reset();
    }
}

