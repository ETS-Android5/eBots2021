package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

public class SeanMode implements EbotsMotionController {
    private String name = this.getClass().getSimpleName();

    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();

    private double forwardInput;
    private double spinInput;

    public SeanMode(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx .class,"frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        motors.add(frontRight);
        motors.add(frontLeft);
        motors.add(backRight);
        motors.add(backLeft);
    }

    public double getForwardInput() {
        return forwardInput;
    }
    public double getSpinInput() {
        return spinInput;
    }
    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void stop(){
        for (DcMotorEx m : motors){
            m.setPower(0.0);
        }
    }

    @Override
    public void handleUserInput(Gamepad gamepad){
        double sloMo = 1-gamepad.left_trigger;
        sloMo = Math.max(sloMo, 0.2);

        forwardInput = -gamepad.left_stick_y;
        spinInput = gamepad.left_stick_x * 0.5;

        frontLeft.setPower((forwardInput + spinInput) * sloMo);
        frontRight.setPower((forwardInput - spinInput) * sloMo);
        backLeft.setPower((forwardInput + spinInput) * sloMo);
        backRight.setPower((forwardInput - spinInput) * sloMo);
    }
}
