package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Carousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
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
        carousel = new Carousel(hardwareMap);
        stopWatch = new StopWatch();
        stateTimeLimit = 5000;
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
        boolean firstApproach = (stopWatch.getElapsedTimeMillis() < 2000);
        if (autonOpMode.getAlliance() == Alliance.BLUE && firstApproach){
            // if on the blue alliance, the Carousel wheel doesn't align well.  must rotate some during first approach
            double powerLevel = (stopWatch.getElapsedTimeMillis() < 2000) ? -0.2 : 0.0;
            frontRight.setPower(powerLevel);
            backRight.setPower(powerLevel);
            frontLeft.setPower(-powerLevel);
            backLeft.setPower(-powerLevel);
        } else {
            for (DcMotorEx m : motors) {
                m.setPower(-0.10);
            }
        }
    }

    @Override
    public void performTransitionalActions() {
        carousel.stopMotor();
    }
}
