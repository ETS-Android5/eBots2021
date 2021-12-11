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
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsBlinkin;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;

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
    private static Arm armInstance = null;
    private Level targetLevel;
    HardwareMap hardwareMap;

    private LinearOpMode opMode;
    private ArmState armState;

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

    public enum ArmState{
        AT_LEVEL_1,
        AT_LEVEL_2,
        AT_LEVEL_3,
        MOVING_UP,
        MOVING_DOWN,
        JUST_DUMPED
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private Arm(LinearOpMode opMode) {
        Log.d(logTag, "Instantiating arm...");
        this.init(opMode);
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

    public ArmState getArmState() {
        updateArmState();
        return armState;
    }

    private void setTargetLevel(Level level){
        int targetPosition = level.getEncoderPosition();
        int currentPosition = armMotor.getCurrentPosition();

        boolean travelingDown = (targetPosition < currentPosition);
        double targetPower = 0.5;
        if (travelingDown) {
            armState = ArmState.MOVING_DOWN;
            targetPower = 0.25;
        } else {
            armState = ArmState.MOVING_UP;
        }

        armMotor.setTargetPosition(targetPosition);
        armMotor.setPower(targetPower);
        targetLevel = level;
        armMotor.setTargetPosition(level.getEncoderPosition());
    }

    public boolean shouldBucketCollect(){
        // this is intended to move the bucket to Collect position just after dumping
        boolean returnFlag = false;
        updateArmState();
        isAtLevelOne = (armState == ArmState.AT_LEVEL_1);
        if (!wasAtLevelOne && isAtLevelOne){
            returnFlag = true;
            wasAtLevelOne = true;
        }
        return returnFlag;
    }

    public boolean isAtTargetLevel(){
        boolean verdict = false;
        int error = armMotor.getTargetPosition() - armMotor.getCurrentPosition();
        if (Math.abs(error) <= 5){
            verdict = true;
        }
        return verdict;
    }

    private void updateArmState(){
        if (isAtTargetLevel()){
            if (targetLevel == Level.ONE){
                armState = ArmState.AT_LEVEL_1;
            } else if(targetLevel == Level.TWO){
                armState = ArmState.AT_LEVEL_2;
            } else if (targetLevel == Level.THREE) {
                armState = ArmState.AT_LEVEL_3;
            }
        }
    }

    public boolean getIsZeroed(){return isZeroed;}

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    //Return the instance if not present
    public static Arm getInstance(LinearOpMode opMode){
        if (armInstance == null){
            armInstance = new Arm(opMode);
        }
        return armInstance;
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void init(LinearOpMode opMode){
        this.opMode = opMode;
        this.hardwareMap = this.opMode.hardwareMap;
        isZeroed = false;
        this.armMotor = this.hardwareMap.get(DcMotorEx.class, "armMotor");
        this.zeroLimitSwitch = this.hardwareMap.get(DigitalChannel.class, "zeroLimitSwitch");
        armMotor.setTargetPosition(0);

        // These lines are added because limit switch is not working properly
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        isZeroed = true;
        armState = ArmState.AT_LEVEL_1;
        targetLevel = Level.ONE;
        wasAtLevelOne = true;
        //****************************************

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
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
//        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

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
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    private void performZeroActions(){
        Log.d(logTag, "Zero is reached");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        isZeroed = true;
        opMode.gamepad2.rumble(350);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armState = ArmState.AT_LEVEL_1;
        targetLevel = Level.ONE;
        wasAtLevelOne = true;
    }

    public void moveToLevel(Level level){
        Log.d(logTag, "Enter move to level");

        rotateBucketToTravelPosition();
        Bucket bucket = Bucket.getInstance(opMode);
        boolean bucketInTravelPosition = bucket.getBucketState() == BucketState.TRAVEL;
        if (!isZeroed | !bucketInTravelPosition) return;

        setTargetLevel(level);
//        if(level != Level.ONE) wasAtLevelOne = false;
        wasAtLevelOne = false;

    }

    private void rotateBucketToTravelPosition(){
        Bucket bucket = Bucket.getInstance(opMode);
        EbotsBlinkin ebotsBlinkin = EbotsBlinkin.getInstance(opMode.hardwareMap);
        ebotsBlinkin.lightsOff();
        long rotateTime = 400L;
        StopWatch stopWatch = new StopWatch();
        boolean bucketInTravelPosition = bucket.getBucketState() == BucketState.TRAVEL;
        if (!bucketInTravelPosition) bucket.setState(BucketState.TRAVEL);
        while (opMode.opModeIsActive() && !bucketInTravelPosition) {
            bucketInTravelPosition = stopWatch.getElapsedTimeMillis() > rotateTime;
        }
    }

    public void moveToLevelAuton(Level level){
        Log.d(logTag, "Enter move to level auton");

        rotateBucketToTravelPosition();
        Bucket bucket = Bucket.getInstance(opMode);

        int targetPosition = level.getEncoderPosition();
        int currentPosition = armMotor.getCurrentPosition();


        boolean bucketInTravelPosition = bucket.getBucketState() == BucketState.TRAVEL;
        if (!isZeroed | !bucketInTravelPosition) return;

        setTargetLevel(level);
//        if(level != Level.ONE) wasAtLevelOne = false;
        wasAtLevelOne = false;

//        boolean travelingDown = (targetPosition < currentPosition);
//        if (travelingDown) {
//            armState = ArmState.MOVING_DOWN;
//        } else {
//            armState = ArmState.MOVING_UP;
//        }
//
//        double targetPower = travelingDown ? 0.25 : 0.5;
//        armMotor.setTargetPosition(targetPosition);
//        armMotor.setPower(targetPower);

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
