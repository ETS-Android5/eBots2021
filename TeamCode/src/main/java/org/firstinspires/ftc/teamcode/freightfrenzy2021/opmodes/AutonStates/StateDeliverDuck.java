package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateDeliverDuck implements EbotsAutonState{

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Class Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Instance Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    Carousel carousel;
    StopWatch stopWatch;
    long spinCarousel;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Constructors
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateDeliverDuck(HardwareMap hardwareMap){
        carousel = new Carousel(hardwareMap);
        stopWatch = new StopWatch();
        spinCarousel = 3750;

        frontLeft = hardwareMap.get(DcMotorEx.class,"frontLeft");
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
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Getters & Setters
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Class Methods
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Instance Methods
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public boolean shouldExit() {
    return stopWatch.getElapsedTimeMillis() >= spinCarousel;
}

    @Override
    public void performStateActions() {
        carousel.startMotor();
        for(DcMotorEx m: motors){
            m.setPower(-0.15);
        }
    }

    @Override
    public void performTransitionalActions() {
        carousel.stopMotor();
    }
}
