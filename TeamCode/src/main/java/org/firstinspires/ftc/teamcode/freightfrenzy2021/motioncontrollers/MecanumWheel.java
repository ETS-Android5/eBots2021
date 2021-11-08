package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.ebotsenums.WheelPosition;

public class MecanumWheel {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private DcMotorEx motor;        // the motor that controls the mecanum wheel
    private double wheelAngleRad;   // the orientation of the rollers on the mecanum wheel (either 45 or -45 deg)
    private WheelPosition wheelPosition;    //
    private double calculatedPower;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public MecanumWheel(double wheelAngleInDegrees, WheelPosition wheelPosition, DcMotorEx motor){
        this.wheelAngleRad = Math.toRadians(wheelAngleInDegrees);
        this.wheelPosition = wheelPosition;
        this.motor = motor;
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public DcMotorEx getMotor() {
        return motor;
    }

    public double getWheelAngleRad() {
        return wheelAngleRad;
    }

    public WheelPosition getWheelPosition(){
        return this.wheelPosition;
    }

    public double getCalculatedPower() {
        return calculatedPower;
    }

    public void setCalculatedPower(double newPower){
        this.calculatedPower = newPower;
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

}
