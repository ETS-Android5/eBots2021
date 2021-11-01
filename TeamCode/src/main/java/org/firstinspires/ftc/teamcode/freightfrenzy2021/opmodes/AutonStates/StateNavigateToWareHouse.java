package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StateNavigateToWareHouse implements EbotsAutonState{

    StopWatch stopWatch;

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private ArrayList<DcMotorEx> motors = new ArrayList<>();

    public StateNavigateToWareHouse(HardwareMap hardwareMap){
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

        stopWatch = new StopWatch();
    }

    @Override
    public boolean shouldExit() {
        boolean shouldExit = false;
        long driveTime = 1750;

        if(stopWatch.getElapsedTimeMillis() >= driveTime){
            shouldExit = true;
        }
        return shouldExit;
    }

    @Override
    public void performStateActions() {
        double speed = 1.0;

        for(DcMotorEx motor: motors){
            motor.setPower(speed);
        }
    }

    @Override
    public void performTransitionalActions() {
        for(DcMotorEx motor: motors){
            motor.setPower(0.0);
        }
    }
}
