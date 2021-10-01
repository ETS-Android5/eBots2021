package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.manips.Carousel;

@TeleOp
public class CarouselTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Carousel carousel = new Carousel(hardwareMap);

        waitForStart();

        while (opModeIsActive()){
            carousel.handleUserInput(gamepad1);

            telemetry.addData("speed", carousel.getSpeed());
            telemetry.update();
        }
    }
}
