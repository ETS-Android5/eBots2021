package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();

    DistanceSensor distanceSensor;
    double currentDistance;
    double targetDistance;
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Constructors
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateDriveToCarousel(HardwareMap hardwareMap) {
        stopWatch = new StopWatch();
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

        distanceSensor = hardwareMap.get(DistanceSensor.class, "backDistanceSensor");
        targetDistance = 15.0;
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
        currentDistance = distanceSensor.getDistance(DistanceUnit.INCH);
        return currentDistance <= 15.0;

    }

    @Override
    public void performStateActions() {

        double currentError = currentDistance - targetDistance;
        double power = currentError / 20;
        power = Math.min(power, 0.6);

        for(DcMotorEx m : motors) {
            m.setPower(-power);

        }
    }

    @Override
    public void performTransitionalActions() {
        for (DcMotorEx m : motors) {
            m.setPower(0.0);
        }
    }

}
