package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;
// Sean is god
//pray fo meh
import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.SeanMode;

@TeleOp


public class VideoGameMode extends LinearOpMode {

        @SuppressLint("DefaultLocale")
        @Override
        public void runOpMode() throws InterruptedException {

                SeanMode seanMode = new SeanMode(hardwareMap);

                waitForStart();

                while(opModeIsActive()){
                        seanMode.handleUserInput(gamepad1);

                        String twoDecimals = "%.2f";
                        telemetry.addData("Motion Controller", seanMode.getName());
                        telemetry.addData("Right input", String.format(twoDecimals, seanMode.getForwardInput()));
                        telemetry.addData("Left input", String.format(twoDecimals, seanMode.getSpinInput()));
                        telemetry.update();
                }
        }
}
//I thank u ;)