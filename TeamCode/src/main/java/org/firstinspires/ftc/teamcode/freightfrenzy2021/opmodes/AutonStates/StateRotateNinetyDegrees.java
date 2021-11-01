package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateRotateNinetyDegrees implements EbotsAutonState{


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private EbotsAutonOpMode autonOpMode;
    // State used for updating telemetry
    private Orientation angles;
    private double targetHeadingDeg;
    BNO055IMU imu;
    ArrayList<DcMotorEx> leftMotors = new ArrayList<>();
    ArrayList<DcMotorEx> rightMotors = new ArrayList<>();
    long stateTimeLimit = 2000;
    StopWatch stopWatch = new StopWatch();

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateRotateNinetyDegrees(EbotsAutonOpMode autonOpMode){
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
        this.autonOpMode = autonOpMode;
        this.imu = autonOpMode.getImu();

        frontLeft = hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        rightMotors.add(frontRight);
        leftMotors.add(frontLeft);
        rightMotors.add(backRight);
        leftMotors.add(backLeft);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (autonOpMode.getAlliance() == Alliance.BLUE){
            targetHeadingDeg = 90;
        } else {
            targetHeadingDeg = -90;
        }
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
        autonOpMode.updateHeading();

        double acceptableError = 3;
        boolean targetHeadingAchieved = false;

        double currentError = applyAngleBound(autonOpMode.getCurrentHeadingDeg() - targetHeadingDeg);
        if (Math.abs(currentError)  <= acceptableError){
            targetHeadingAchieved = true;
        }

        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;

        return targetHeadingAchieved | stateTimedOut | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        double currentError = autonOpMode.getCurrentHeadingDeg() - targetHeadingDeg;
        double power = currentError * 0.03;

        if (autonOpMode.getAlliance() == Alliance.RED){
            for(DcMotorEx m : leftMotors) {
                m.setPower(power);
             }
            for(DcMotorEx m : rightMotors) {
                m.setPower(-power);
            }
        } else {
            for(DcMotorEx m : leftMotors) {
                m.setPower(-power);
            }
            for(DcMotorEx m : rightMotors) {
                m.setPower(power);
            }
        }
    }

    @Override
    public void performTransitionalActions() {
        for (DcMotorEx m : leftMotors) {
            m.setPower(0.0);
        }
        for (DcMotorEx m : rightMotors) {
            m.setPower(0.0);
        }
    }

    private double applyAngleBound (double inputAngle){
        while (inputAngle > 180){
            inputAngle -= 360;
        }
        while (inputAngle <= -180){
            inputAngle += 360;
        }
        return inputAngle;
    }
}