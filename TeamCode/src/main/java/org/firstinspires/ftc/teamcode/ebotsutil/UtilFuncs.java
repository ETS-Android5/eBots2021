package org.firstinspires.ftc.teamcode.ebotsutil;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Arm;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;

public class UtilFuncs {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static double applyAngleBounds(double inputAngle){
        double boundedAngle = inputAngle;
        while (boundedAngle > 180){
            boundedAngle -= 360;
        }
        while (boundedAngle <= -180){
            boundedAngle += 360;
        }
        return boundedAngle;
    }

    public static int calculateTargetClicks(double distanceInInches){
        double clicksPerInch = 48.9;
        return (int) Math.round(distanceInInches * clicksPerInch);
    }

    public static void initManips(Arm arm, Carousel carousel,HardwareMap hardwareMap){
        arm.init(hardwareMap);
        carousel.initMotor(hardwareMap);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

}
