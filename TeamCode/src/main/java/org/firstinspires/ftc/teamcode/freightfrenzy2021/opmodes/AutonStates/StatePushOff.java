package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public class StatePushOff implements EbotsAutonState{

    StopWatch stopWatch = new StopWatch();

    private String name = this.getClass().getSimpleName();

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private ArrayList<DcMotorEx> motors = new ArrayList<>();

    public StatePushOff(HardwareMap hardwareMap){
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

        stopWatch.reset();
    }

    @Override
    public boolean shouldExit() {
        boolean shouldExit = false;
        long driveTime = 300;

        if(driveTime >= stopWatch.getElapsedTimeMillis()){
            shouldExit = true;
        }
        return shouldExit;
    }

    @Override
    public void performStateActions() {
        double speed = 0.5;

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
