package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;

import java.util.ArrayList;

public class ArmManip implements EbotsMotionController {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Instance Attributes
      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String name = this.getClass().getSimpleName();

    private DcMotorEx frontLeft;

    ArrayList<DcMotorEx> motors = new ArrayList<>();

    private double leftInput;
    private double rightInput;
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Constructors
         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public ArmManip(HardwareMap hardwareMap){
        frontLeft = hardwareMap.get(DcMotorEx .class,"frontLeft");

        motors.add(frontLeft);


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public double getArmInput() {
        return getarmInput;
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
