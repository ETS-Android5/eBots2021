package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;

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
    EbotsAutonOpMode autonOpMode;
    long stateTimeLimit;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    ArrayList<DcMotorEx> motors = new ArrayList<>();
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Constructors
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateDeliverDuck(EbotsAutonOpMode autonOpMode){
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
        this.autonOpMode = autonOpMode;
        carousel = Carousel.getInstance(hardwareMap);
        stopWatch = new StopWatch();
        stateTimeLimit = 4250;
        this.autonOpMode = autonOpMode;
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

        for(DcMotorEx motor: motors){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;

    return stateTimedOut | !autonOpMode.opModeIsActive();
}

    @Override
    public void performStateActions() {
        carousel.startMotor();

        for (DcMotorEx m : motors) {
            m.setPower(-0.10);
        }

    }

    @Override
    public void performTransitionalActions() {
        carousel.stopMotor();
        for(DcMotorEx motor: motors){
            motor.setPower(0.0);
        }
    }
}
