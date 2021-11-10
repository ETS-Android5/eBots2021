package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

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
    ArrayList<DcMotorEx> leftMotors = new ArrayList<>();
    ArrayList<DcMotorEx> rightMotors = new ArrayList<>();

    long stateTimeLimit = 3500;
    StopWatch stopWatch = new StopWatch();
    private final Intake intake;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateCollectFreight(EbotsAutonOpMode autonOpMode) {
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
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
        intake = new Intake(hardwareMap);
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
        //stopping intake motor
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;

        return  stateTimedOut | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
            intake.start();
            for(DcMotorEx m : leftMotors) {
                m.setPower(0.15);
            }
            for(DcMotorEx m : rightMotors) {
                m.setPower(0.15);
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
        intake.stop();
    }
}