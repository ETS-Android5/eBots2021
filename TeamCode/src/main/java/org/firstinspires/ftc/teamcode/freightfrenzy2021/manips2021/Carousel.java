package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

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

    public void startMotor (){carouselMotor.setPower(0.35);}

    public void stopMotor (){carouselMotor.setPower(0);}

    public void handleUserInput(Gamepad gamepad){
        double increment = 0.05;
        long timeOut = 1000;
      if(gamepad.start && stopWatch.getElapsedTimeMillis() > timeOut){
          startMotor();
          stopWatch.reset();
      } else if(gamepad.dpad_up && stopWatch.getElapsedTimeMillis() > timeOut){
          setSpeed(increment);
          stopWatch.reset();
      } else if(gamepad.dpad_down && stopWatch.getElapsedTimeMillis() > timeOut){
          setSpeed(-increment);
          stopWatch.reset();
      }else if(gamepad.back){
          stopMotor();
      }

    }

}
