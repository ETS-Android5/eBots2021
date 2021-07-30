package org.firstinspires.ftc.teamcode.manips;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements EbotsManip{
    private DcMotorEx intake ;

    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void handleGamepadInput(Gamepad gamepad) {
        double inputThreshold = 0.3;
        double intakeInput = -gamepad.left_stick_y;

        // Condition the input signal to either be -1, 0, or 1
        double intakePower = (Math.abs(intakeInput) < inputThreshold) ? 0 : Math.signum(intakeInput) * 1;

        intake.setPower(intakePower);
    }

    @Override
    public void stop() {
        intake.setPower(0);
    }
}
