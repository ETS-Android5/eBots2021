package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

import java.util.ArrayList;

public class DriveToEncoderTarget implements EbotsMotionController{
    StopWatch stopWatch = new StopWatch();
    EbotsAutonOpMode autonOpMode;

    private String name = this.getClass().getSimpleName();

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private ArrayList<DcMotorEx> motors = new ArrayList<>();

    private int encoderTarget;


    public DriveToEncoderTarget(EbotsAutonOpMode autonOpMode){
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
        motors.add(backLeft);
        motors.add(backRight);

        stopWatch.reset();

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void setEncoderTarget(int encoderTarget) {
        this.encoderTarget = encoderTarget;
        for(DcMotorEx motor: motors){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(encoderTarget);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1.0);
        }
    }

    public int getAverageClicks(){
        int totalClicks = 0;
        for(DcMotorEx motor: motors){
            totalClicks += motor.getCurrentPosition();
        }
        return (totalClicks / 4);
    }

    public boolean isTargetReached(){
        boolean targetReached = false;
        int error = getAverageClicks() - encoderTarget;
        if (Math.abs(error) < 25){
            targetReached = true;
        }
        return targetReached;
    }

    public void logAllEncoderClicks(){
        for(DcMotorEx motor: motors){
            Log.d("EBOTS", "Motor Position:" + String.format("%d", motor.getCurrentPosition()));
        }
    }

    @Override
    public void stop() {
        for(DcMotorEx motor: motors){
            motor.setPower(0.0);
        }
    }

    @Override
    public void handleUserInput(Gamepad gamepad) {

    }
}
