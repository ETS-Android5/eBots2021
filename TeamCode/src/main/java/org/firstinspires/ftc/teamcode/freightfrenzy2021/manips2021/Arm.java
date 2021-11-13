package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;

public class Arm {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private DcMotorEx armMotor;
    private DigitalChannel zeroLimitSwitch;
    private boolean isZeroed = false;
    private StopWatch stopWatchInput = new StopWatch();

    LinearOpMode opMode;

    String logTag = "EBOTS";

    public enum Level{
        ONE(0),
        TWO(590),
        THREE(1200);

        private int encoderPosition;

        Level(int pos){
            this.encoderPosition = pos;
        }

        public int getEncoderPosition() {
            return encoderPosition;
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Arm(LinearOpMode opMode) {
        Log.d(logTag, "Instantiating arm...");
        HardwareMap hardwareMap = opMode.hardwareMap;
        this.opMode = opMode;
        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(0.0);

        zeroLimitSwitch = hardwareMap.get(DigitalChannel.class, "zeroLimitSwitch");
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public double getSpeed(){
        return armMotor.getPower();
    }

    public boolean isAtBottom(){
        return !zeroLimitSwitch.getState();
    }

    public int getPosition(){
        return armMotor.getCurrentPosition();
    }

    public boolean isAtTargetLevel(){
        boolean verdict = false;
        int error = armMotor.getTargetPosition() - armMotor.getCurrentPosition();
        if (Math.abs(error) <= 25){
            verdict = true;
        }
        return verdict;
    }

    public boolean getIsZeroed(){return isZeroed;}

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public void zeroArmHeight(){
        Log.d(logTag, "Entering zeroArmHeight");
        if(isAtBottom()) {
            performZeroActions();
            return;
        }

        StopWatch stopWatchZero = new StopWatch();
        long timeLimit = 1500;
        boolean isTimedOut = stopWatchZero.getElapsedTimeMillis() >= timeLimit;

        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        while (!isAtBottom() && !isTimedOut && opMode.opModeIsActive()){
            armMotor.setPower(-0.25);
            isTimedOut = stopWatchZero.getElapsedTimeMillis() >= timeLimit;
        }
        // stop the motor
        armMotor.setPower(0.0);

        if(isAtBottom()){
            performZeroActions();
        } else{
            Log.d(logTag, "Zero operation timed out!!");
            int currentPosition = armMotor.getCurrentPosition();
            armMotor.setTargetPosition(currentPosition);
        }

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void performZeroActions(){
        Log.d(logTag, "Zero is reached");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        isZeroed = true;
        opMode.gamepad2.rumble(350);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void moveToLevel(Level level){
        Log.d(logTag, "Enter move to level");

        if (!isZeroed) return;
        int targetPosition = level.getEncoderPosition();
        int currentPosition = armMotor.getCurrentPosition();
        int error = targetPosition - currentPosition;

        boolean travelingDown = (targetPosition < currentPosition);
        double targetPower = travelingDown ? 0.25 : 0.5;
        armMotor.setTargetPosition(targetPosition);
        armMotor.setPower(targetPower);

    }

    public void handleUserInput(Gamepad gamepad) {
        if (stopWatchInput.getElapsedTimeMillis() < 500) return;

        if(gamepad.left_bumper && gamepad.right_stick_button){
            zeroArmHeight();
            stopWatchInput.reset();
        } else if(gamepad.square){
            moveToLevel(Level.TWO);
            stopWatchInput.reset();
        } else if(gamepad.cross) {
            moveToLevel(Level.ONE);
            stopWatchInput.reset();
        } else if (gamepad.triangle){
            moveToLevel(Level.THREE);
            stopWatchInput.reset();
        }

    }


}
