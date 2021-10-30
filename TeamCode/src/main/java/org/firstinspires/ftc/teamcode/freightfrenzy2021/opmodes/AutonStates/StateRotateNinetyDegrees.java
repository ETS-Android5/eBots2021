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
    private EbotsAutonOpMode opMode;
    // State used for updating telemetry
    private Orientation angles;
    private double initialHeadingDeg;
    private double currentHeadingDeg;
    private double targetHeadingDeg;
    BNO055IMU imu;
    ArrayList<DcMotorEx> leftMotors = new ArrayList<>();
    ArrayList<DcMotorEx> rightMotors = new ArrayList<>();

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateRotateNinetyDegrees(HardwareMap hardwareMap, BNO055IMU imu, EbotsAutonOpMode opMode){
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
        this.opMode = opMode;
        this.imu = imu;
        if (opMode.getAllianceColor() == Alliance.RED){
            targetHeadingDeg = -90;
        } else {
            targetHeadingDeg = 90;
        }
        updateHeading();
        initialHeadingDeg = 0;
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
        updateHeading();
        boolean shouldExit = false;
        double currentError = currentHeadingDeg - targetHeadingDeg;
        if(currentError <= -180){
            currentError = currentError + 360;
        }
        double acceptableError = 3;
        if (Math.abs(currentError)  <= acceptableError){
            shouldExit = true;
        }
        return shouldExit;
    }

    //double check this should exit
    private void updateHeading(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        currentHeadingDeg = angles.firstAngle + initialHeadingDeg;
    }

    @Override
    public void performStateActions() {
        double currentError = currentHeadingDeg - targetHeadingDeg;
        double power = currentError * 0.03;
        if (opMode.getAllianceColor() == Alliance.RED){
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
}