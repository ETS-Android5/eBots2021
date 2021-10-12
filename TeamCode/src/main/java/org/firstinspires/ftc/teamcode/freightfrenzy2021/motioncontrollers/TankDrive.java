package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

public class TankDrive implements EbotsMotionController {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String name = this.getClass().getSimpleName();

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();

    private double leftInput;
    private double rightInput;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public TankDrive(HardwareMap hardwareMap){
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


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public double getLeftInput() {
        return leftInput;
    }

    public double getRightInput() {
        return rightInput;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void stop(){
        for (DcMotorEx m : motors){
            m.setPower(0.0);
        }
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void handleUserInput(Gamepad gamepad){
        double sloMo = 1-gamepad.left_trigger;
        sloMo = Math.max(sloMo, 0.2);

        leftInput = -gamepad.left_stick_y;
        rightInput = -gamepad.right_stick_y;

        frontLeft.setPower((leftInput) * sloMo);
        frontRight.setPower((rightInput) * sloMo);
        backLeft.setPower((leftInput) * sloMo);
        backRight.setPower((rightInput) * sloMo);
    }
}
