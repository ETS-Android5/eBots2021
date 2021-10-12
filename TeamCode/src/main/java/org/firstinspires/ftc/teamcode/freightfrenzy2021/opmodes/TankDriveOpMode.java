package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.DriveAndSpin;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.TankDrive;
@TeleOp
public class TankDriveOpMode extends LinearOpMode {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {

        Intake intake2021 = new Intake(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);

        TankDrive tankDrive = new TankDrive(hardwareMap);

        waitForStart();

        while(opModeIsActive()){
            tankDrive.handleUserInput(gamepad1);
            intake2021.handleUserInput(gamepad2);
            carousel.handleUserInput(gamepad2);

            String twoDecimals = "%.2f";
            telemetry.addData("Motion Controller", tankDrive.getName());
            telemetry.addData("Right input", String.format(twoDecimals, tankDrive.getRightInput()));
            telemetry.addData("Left input", String.format(twoDecimals, tankDrive.getLeftInput()));
            telemetry.addData("Carousel Speed", String.format(twoDecimals, carousel.getSpeed()));
            telemetry.addData("intakeSpeed", String.format(twoDecimals, intake2021.getSpeed()));
            telemetry.update();
        }


    }
}
