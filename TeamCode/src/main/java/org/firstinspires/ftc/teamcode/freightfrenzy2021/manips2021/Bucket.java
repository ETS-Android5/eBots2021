package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;

public class Bucket {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    Servo bucketServo;
    private BucketState bucketState;
    private StopWatch stopWatchDump = new StopWatch();
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
       Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Bucket(HardwareMap hardwareMap){
        initServo(hardwareMap);
        stopWatchDump = new StopWatch();
        bucketState = BucketState.COLLECT;


    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void setPos(double servoPos){
        bucketServo.setPosition(servoPos);
    }

    public double getPos(){
        return bucketServo.getPosition();
    }

    public void setState(BucketState targetState){
        if(targetState == BucketState.DUMP){
            // If asking to dump but state was previously collect then toggle state
            if(bucketState == BucketState.COLLECT) toggleState();
            setPos(getDumpPositionWithVibrate());
        } else {
            // if asking to COLLECT but state was previously DUMP, then toggle state
            if(bucketState == BucketState.DUMP) toggleState();
            setPos(BucketState.COLLECT.getServoSetting());
        }
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void initServo (HardwareMap hardwareMap){
        bucketServo = hardwareMap.get(Servo.class,"bucket");
        bucketState = BucketState.COLLECT;
        bucketServo.setPosition(bucketState.getServoSetting());

    }
    public void handleUserInput(Gamepad gamepad){

        if(gamepad.dpad_left){
            if(bucketState == BucketState.COLLECT) toggleState();
            setPos(getDumpPositionWithVibrate());
        } else {
            if(bucketState == BucketState.DUMP) toggleState();
            setPos(BucketState.COLLECT.getServoSetting());
        }
    }

    private BucketState toggleState(){
        BucketState newState;
        if (bucketState == BucketState.COLLECT){
            newState = BucketState.DUMP;
            stopWatchDump.reset();
        } else{
            newState = BucketState.COLLECT;
        }
        bucketState = newState;
        return newState;
    }

    private double getDumpPositionWithVibrate(){
        double targetServoSetting = BucketState.DUMP.getServoSetting();
        double settingReduction = 0.1;  // amount to reduce setting
        // first allow bucket to dump or duration dumpTime
        long dumpTime = 500;
        // vibrate by frequency
        long frequencyMillis = 100;

        long currentTime = stopWatchDump.getElapsedTimeMillis();

        long cycleTime = currentTime - dumpTime;

        // on odd number cycles, move to a lesser dump angle
        int currentCycle = (int) Math.floor(cycleTime / frequencyMillis);

        boolean cycleIsOdd = currentCycle % 2 == 1;
        if (currentTime > dumpTime && !cycleIsOdd){
            targetServoSetting += settingReduction;
        }

        return targetServoSetting;
    }
}
