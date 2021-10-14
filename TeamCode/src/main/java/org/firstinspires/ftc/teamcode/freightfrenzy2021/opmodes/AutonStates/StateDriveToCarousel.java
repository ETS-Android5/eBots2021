package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.TankDrive;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateDriveToCarousel implements EbotsAutonState{
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Class Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Instance Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    StopWatch stopWatch;
    long driveTime;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Constructors
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateDriveToCarousel(HardwareMap hardwareMap) {
        stopWatch = new StopWatch();
        driveTime = 1750;
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
        return stopWatch.getElapsedTimeMillis() >= driveTime;

    }

    @Override
    public void performStateActions() {
        for(DcMotorEx m : motors) {
            m.setPower(-0.35);


        }
    }

    @Override
    public void performTransitionalActions() {
        for (DcMotorEx m : motors) {
            m.setPower(0.0);
        }
    }

}
