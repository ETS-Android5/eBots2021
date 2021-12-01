package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsTeleOp;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsTeleOpV2;

public class Arm {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private DcMotorEx armMotor;
    private DigitalChannel zeroLimitSwitch;
    private boolean isZeroed = false;
    private StopWatch stopWatchInput = new StopWatch();
    private boolean wasAtLevelOne = false;
    private boolean isAtLevelOne = false;

    LinearOpMode opMode;

    private static String logTag = "EBOTS";

    public enum Level{
        ONE(0),
        TWO(625),
        THREE(1235);

        private int encoderPosition;

        Level(int pos){
            this.encoderPosition = pos;
        }

        public int getEncoderPosition() {
            return encoderPosition;
        }
    }

    private enum ArmState{
        AT_BOTTOM,
        MOVING_UP,
        MOVING_DOWN,
        JUST_DUMPED
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Arm(LinearOpMode opMode) {
        Log.d(logTag, "Instantiating arm...");
        HardwareMap hardwareMap = opMode.hardwareMap;
        this.opMode = opMode;
        this.armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        this.zeroLimitSwitch = hardwareMap.get(DigitalChannel.class, "zeroLimitSwitch");
        this.init();
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

    private void setIsAtLevelOne(){
        int currentPosition = armMotor.getCurrentPosition();
        int allowedTolerance = 5;
        int maxAllowedPosition = Level.ONE.encoderPosition + allowedTolerance;
        isAtLevelOne = currentPosition < maxAllowedPosition;
    }

    public boolean shouldBucketCollect(){
        boolean returnFlag = false;
        setIsAtLevelOne();
        if (!wasAtLevelOne && isAtLevelOne){
            returnFlag = true;
            wasAtLevelOne = true;
        }
        return returnFlag;
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
    public void init(){
        isZeroed = false;
        armMotor.setTargetPosition(0);
        // These lines are added because limit switch is not working properly
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        isZeroed = true;
        //****************************************
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setPower(0.0);
    }

    public void zeroArmHeight(){
        Log.d(logTag, "Entering zeroArmHeight");
        if(isAtBottom()) {
            Log.d(logTag, "Arm is at bottom...");
            performZeroActions();
            return;
        }

        StopWatch stopWatchZero = new StopWatch();
        long timeLimit = 250;
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
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void moveToLevel(Level level){
        Log.d(logTag, "Enter move to level");
        StopWatch stopWatch = new StopWatch();
        long timeLimit = 1000;
        long minTime = 400L;
        boolean isTimedOut = false;
        boolean bucketInTravelPosition = false;
        if(opMode instanceof EbotsTeleOp){
            Log.d(logTag, "inside if of TeleOp");
            while (opMode.opModeIsActive() && !bucketInTravelPosition && !isTimedOut) {
                ((EbotsTeleOp) opMode).bucket.setState(BucketState.TRAVEL);
                bucketInTravelPosition = stopWatch.getElapsedTimeMillis() > minTime;
                isTimedOut = stopWatch.getElapsedTimeMillis() > timeLimit;
            }
        }
        if(opMode instanceof EbotsTeleOpV2){
            Log.d(logTag, "inside if of TeleOpV2");
            while (opMode.opModeIsActive() && !bucketInTravelPosition && !isTimedOut) {
                ((EbotsTeleOpV2) opMode).bucket.setState(BucketState.TRAVEL);
                bucketInTravelPosition = stopWatch.getElapsedTimeMillis() > minTime;
                isTimedOut = stopWatch.getElapsedTimeMillis() > timeLimit;
            }
        }

        if (!isZeroed | !bucketInTravelPosition) return;

        int targetPosition = level.getEncoderPosition();
        int currentPosition = armMotor.getCurrentPosition();

        boolean travelingDown = (targetPosition < currentPosition);
        double targetPower = travelingDown ? 0.25 : 0.5;
        armMotor.setTargetPosition(targetPosition);
        armMotor.setPower(targetPower);
        if(level != Level.ONE) wasAtLevelOne = false;

    }

    public void moveToLevelAuton(Level level){
        Log.d(logTag, "Enter move to level auton");

        int targetPosition = level.getEncoderPosition();
        int currentPosition = armMotor.getCurrentPosition();

        boolean travelingDown = (targetPosition < currentPosition);
        double targetPower = travelingDown ? 0.25 : 0.5;
        armMotor.setTargetPosition(targetPosition);
        armMotor.setPower(targetPower);

    }

    public void handleUserInput(Gamepad gamepad) {
        if (stopWatchInput.getElapsedTimeMillis() < 500) return;

        if(gamepad.left_bumper && gamepad.right_stick_button){
            Log.d(logTag, "Captured input to zeroArmHeight");
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
