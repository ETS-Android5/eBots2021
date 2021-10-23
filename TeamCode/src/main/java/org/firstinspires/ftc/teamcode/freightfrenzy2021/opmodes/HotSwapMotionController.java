package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveAndSpin;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.MecanumDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.TankDrive;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

@TeleOp
public class HotSwapMotionController extends LinearOpMode {

    EbotsMotionController motionController;
    StopWatch stopWatch = new StopWatch();
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {

        Intake intake2021 = new Intake(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);

        motionController = EbotsMotionController.get(DriveAndSpin.class, hardwareMap);
        DistanceSensor distanceSensor = hardwareMap.get(DistanceSensor.class, "backDistanceSensor");

        while (! this.isStarted()){
            handleUserInput(gamepad1);
            telemetry.addData("Motion Controller", motionController.getName());
            telemetry.addData("Current Distance", distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()){
            this.handleUserInput(gamepad1);
            motionController.handleUserInput(gamepad1);
            intake2021.handleUserInput(gamepad2);
            carousel.handleUserInput(gamepad2);

            String twoDecimals = "%.2f";
            telemetry.addData("Motion Controller", motionController.getName());
            telemetry.addData("Current Distance", distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.addData("Carousel Speed (fmt)", String.format(twoDecimals, carousel.getSpeed()));
            telemetry.addData("Intake Speed", String.format(twoDecimals, intake2021.getSpeed()));
            telemetry.update();
        }
    }

    private void handleUserInput(Gamepad gamepad){
        boolean lockoutActive = stopWatch.getElapsedTimeMillis() < 600;
        if (lockoutActive){
            return;
        }

        if(gamepad.left_bumper && gamepad.right_stick_button){
            if(motionController instanceof DriveAndSpin){
                motionController = EbotsMotionController.get(TankDrive.class, hardwareMap);
            } else if (motionController instanceof TankDrive){
                motionController = EbotsMotionController.get(MecanumDrive.class, hardwareMap);
            } else if (motionController instanceof MecanumDrive){
                motionController = EbotsMotionController.get(DriveAndSpin.class, hardwareMap);
            }
        }
        stopWatch.reset();
    }
}

