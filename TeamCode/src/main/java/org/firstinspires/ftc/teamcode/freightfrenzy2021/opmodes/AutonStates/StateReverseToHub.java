package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateReverseToHub implements EbotsAutonState{

    StopWatch stopWatch = new StopWatch();
    EbotsAutonOpMode autonOpMode;


    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    DistanceSensor distanceSensor;
    double currentDistance;
    double targetDistance;
    private ArrayList<DcMotorEx> motors = new ArrayList<>();
    private double speed;
    private long stateTimeLimit=2000;
    private Bucket bucket;


    public StateReverseToHub(EbotsAutonOpMode autonOpMode) {
        this.autonOpMode = autonOpMode;
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        motors.add(frontLeft);
        motors.add(frontRight);
        motors.add(backRight);
        motors.add(backLeft);

        distanceSensor = hardwareMap.get(DistanceSensor.class, "backDistanceSensor");
        targetDistance = 2;

        stateTimeLimit = 500;

        stopWatch.reset();

        bucket = new Bucket(hardwareMap);
    }

    @Override
    public boolean shouldExit() {
        currentDistance = distanceSensor.getDistance(DistanceUnit.INCH);
        boolean targetPositionAchieved = currentDistance <= targetDistance;
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;
        return targetPositionAchieved | stateTimedOut | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        double currentError = currentDistance - targetDistance;
        double power = currentError / 20;
        double maxAllowedSpeed = 0.6;

        // Now make sure that the power doesn't exceed our maxAllowedSpeed, and preserve sign
        double powerSign = Math.signum(power);
        double powerMagnitude = Math.min(Math.abs(power), maxAllowedSpeed);
        power = powerSign * powerMagnitude;

        for(DcMotorEx m : motors) {
            m.setPower(-power);
        }
    }

    @Override
    public void performTransitionalActions() {
        for (DcMotorEx m : motors) {
            m.setPower(0.0);
        }

        StopWatch stopWatchDumpFreight = new StopWatch();
        long dumpTime = 2000;
        while(stopWatchDumpFreight.getElapsedTimeMillis() < dumpTime){
            bucket.setState(BucketState.DUMP);
        }
        bucket.setState(BucketState.COLLECT);
    }
}
