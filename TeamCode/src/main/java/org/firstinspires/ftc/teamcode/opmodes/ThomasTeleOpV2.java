package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.manips.Carousel;
import org.firstinspires.ftc.teamcode.manips.Intake2021;

@TeleOp
public class ThomasTeleOpV2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Intake2021 intake2021 = new Intake2021(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()){
            double yInput = -gamepad1.left_stick_y;
            double xInput = gamepad1.left_stick_x;
            double spinInput = gamepad1.right_stick_x;

            frontLeft.setPower(yInput + spinInput);
            frontRight.setPower(yInput - spinInput);
            backLeft.setPower(yInput + spinInput);
            backRight.setPower(yInput - spinInput);
            intake2021.handleUserInput(gamepad1);
            carousel.handleUserInput(gamepad1);

            telemetry.addData("yInput", yInput);
            telemetry.addData("xInput", xInput);

            telemetry.addData("carouselSpeed", carousel.getSpeed());

            telemetry.addData("intakeSpeed", intake2021.getSpeed());

            telemetry.update();
        }
    }
}

