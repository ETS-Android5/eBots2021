package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateCollectFreight implements EbotsAutonState{


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
    private DcMotorEx intakeMotor;
    private EbotsAutonOpMode opMode;
    private Orientation angles;
    boolean targetHeadingAchieved = false;
    private double targetHeadingDeg;
    BNO055IMU imu;
    ArrayList<DcMotorEx> leftMotors = new ArrayList<>();
    ArrayList<DcMotorEx> rightMotors = new ArrayList<>();
    ArrayList<DcMotorEx> motors = new ArrayList<>();

    long runningIntake = 3500;
    StopWatch stopWatch = new StopWatch();

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateCollectFreight(EbotsAutonOpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        this.opMode = opMode;
        this.imu = opMode.getImu();
        this.autonOpMode = autonOpMode;
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        rightMotors.add(frontRight);
        leftMotors.add(frontLeft);
        rightMotors.add(backRight);
        leftMotors.add(backLeft);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (autonOpMode.getAlliance() == Alliance.RED) {
            targetHeadingDeg = -45;
        } else {
            targetHeadingDeg = 45;
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
        //turning to 45
        opMode.updateHeading();
        double acceptableError = 3;
        double currentError = opMode.getCurrentHeadingDeg() - targetHeadingDeg;
        if (Math.abs(currentError)  <= acceptableError){
            targetHeadingAchieved = true;
        }
        //stopping intake motor
        boolean stopIntakeMotor = false;
        if (intakeMotor.isMotorEnabled() && runningIntake <= stopWatch.getElapsedTimeMillis()) {
            stopIntakeMotor = true;
        }

        return targetHeadingAchieved  && stopIntakeMotor | !opMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        //going to heading
        double currentError = opMode.getCurrentHeadingDeg() - targetHeadingDeg;
        double power = currentError * 0.03;
        for(DcMotorEx m : leftMotors) {
            m.setPower(power);
        }
        for(DcMotorEx m : rightMotors) {
            m.setPower(-power);
        }
        //turing on intake motor and moving forward a bit
        if (targetHeadingAchieved){
            intakeMotor.setPower(0.70);
            for(DcMotorEx m : leftMotors) {
                m.setPower(0.15);
            }
            for(DcMotorEx m : rightMotors) {
                m.setPower(0.15);
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
        intakeMotor.setPower(0);
    }
}