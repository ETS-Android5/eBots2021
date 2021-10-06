package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    private DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor(hardwareMap);
    }

    private void intakeMotor(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
    }

    public double getSpeed(){
        return intakeMotor.getPower();
    }


    public void handleUserInput(Gamepad gamepad) {

        if (gamepad.right_trigger > 0.3) {
            intakeMotor.setPower(gamepad.right_trigger);

        } else if (gamepad.left_trigger > 0.3) {
            intakeMotor.setPower(-gamepad.left_trigger);

        } else  {
            intakeMotor.setPower(0);
        }

    }
}