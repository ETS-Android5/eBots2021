package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private DcMotorEx intakeMotor;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public double getSpeed(){
        return intakeMotor.getPower();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void start(){
        intakeMotor.setPower(0.8);
    }
    public void stop(){
        intakeMotor.setPower(0.0);
    }

    public void handleUserInput(Gamepad gamepad) {
        double maxPower = 0.75;
        double power = 0.0;

        if ((gamepad.right_trigger > 0.3) && gamepad.right_bumper){
            power = 1.0;
            intakeMotor.setPower(power);
        } else if (gamepad.right_trigger > 0.3){
            power = Math.min(gamepad.right_trigger,maxPower);
            intakeMotor.setPower(power);
        } else if ((gamepad.left_trigger > 0.3) && gamepad.left_bumper){
            power = 1.0;
            intakeMotor.setPower(-power);
        } else if (gamepad.left_trigger > 0.3) {
            power = Math.min(gamepad.left_trigger,maxPower);
            intakeMotor.setPower(-power);
        } else  {
            intakeMotor.setPower(0);
        }
    }
}