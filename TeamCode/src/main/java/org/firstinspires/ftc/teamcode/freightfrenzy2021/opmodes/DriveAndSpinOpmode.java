package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveAndSpin;

@TeleOp
public class DriveAndSpinOpmode extends LinearOpMode {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {

        Intake intake2021 = new Intake(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);

        DriveAndSpin driveAndSpin = new DriveAndSpin(hardwareMap);

        waitForStart();

        while (opModeIsActive()){
            driveAndSpin.handleUserInput(gamepad1);
            intake2021.handleUserInput(gamepad2);
            carousel.handleUserInput(gamepad2);

            String twoDecimals = "%.2f";
            telemetry.addData("Motion Controller", driveAndSpin.getName());
            telemetry.addData("Spin Input: ", String.format(twoDecimals, driveAndSpin.getSpinInput()));
            telemetry.addData("Forward Input: ", String.format(twoDecimals, driveAndSpin.getForwardInput()));
            telemetry.addData("Carousel Speed (raw)", carousel.getSpeed());
            telemetry.addData("Carousel Speed (fmt)", String.format(twoDecimals, carousel.getSpeed()));
            telemetry.addData("Intake Speed", String.format(twoDecimals, intake2021.getSpeed()));
            telemetry.update();
        }
    }
}

