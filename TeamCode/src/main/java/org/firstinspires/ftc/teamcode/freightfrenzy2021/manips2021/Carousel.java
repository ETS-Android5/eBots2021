package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

public class Carousel {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    double speed;
    StopWatch stopWatch;
    boolean isTouching;
    int encoderClicks;
    DcMotorEx carouselMotor;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Carousel(HardwareMap hardwareMap){
        initMotor(hardwareMap);
        stopWatch = new StopWatch();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setSpeed(double amount){
        double newPower = carouselMotor.getPower() + amount;
        carouselMotor.setPower(newPower);
    }
    public double getSpeed(){
        return carouselMotor.getPower();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void initMotor (HardwareMap hardwareMap){
         carouselMotor = hardwareMap.get(DcMotorEx.class,"carouselMotor");
         speed = carouselMotor.getPower();
    }

    public void startMotor (){
        int spinSign = 1;
        if(AllianceSingleton.getAlliance() == Alliance.BLUE){
            spinSign = -1;
        }
        carouselMotor.setPower(0.35 * spinSign);
    }

    public void stopMotor (){carouselMotor.setPower(0);}

    public void handleUserInput(Gamepad gamepad){
        int spinSign = 1;
        if(AllianceSingleton.getAlliance() == Alliance.BLUE){
            spinSign = -1;
        }
        double increment = 0.05 * spinSign;
        long timeOut = 400;
      if(gamepad.right_bumper && stopWatch.getElapsedTimeMillis() > timeOut){
          startMotor();
          stopWatch.reset();
      } else if(gamepad.dpad_up && stopWatch.getElapsedTimeMillis() > timeOut){
          setSpeed(increment);
          stopWatch.reset();
      } else if(gamepad.dpad_down && stopWatch.getElapsedTimeMillis() > timeOut){
          setSpeed(-increment);
          stopWatch.reset();
      }else if(gamepad.left_bumper){
          stopMotor();
      }

    }

}
