package org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsTeleOp;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsTeleOpV2;

public class Bucket {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    Servo bucketServo;
    private BucketState bucketState;
    private BucketState dumpStartedFrom;
    private StopWatch stopWatchDump = new StopWatch();
    private StopWatch stopWatchInput = new StopWatch();
    private LinearOpMode opMode;
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
       Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Bucket(LinearOpMode opMode){
        this.opMode = opMode;
        HardwareMap hardwareMap = opMode.hardwareMap;
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

    public BucketState getBucketState() {
        return bucketState;
    }

    public void setState(BucketState targetState){
        if(targetState == BucketState.DUMP) {
            // If asking to dump but state was previously collect then toggle state
            if (bucketState == BucketState.COLLECT) toggleState();
            setPos(getDumpPositionWithVibrate());
        } else if(targetState == BucketState.TRAVEL) {
            bucketState = BucketState.TRAVEL;
            setPos(BucketState.TRAVEL.getServoSetting());
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

        long lockOutLimit = 500;
        boolean isLockedOut = stopWatchInput.getElapsedTimeMillis() <= lockOutLimit;
        if (gamepad.circle && !isLockedOut){
            // if push the circle, toggle between COLLECT and TRAVEL
            toggleState();
            stopWatchInput.reset();
        } else if (gamepad.dpad_left){
            // if dpad left, set the state to dump and start the timer for the bucket waggle
            if (bucketState != BucketState.DUMP) {
                stopWatchDump.reset();
                dumpStartedFrom = bucketState;  // either collect or travel
                // Log.d("EBOTS", "dumpStartedFrom: " + dumpStartedFrom.name());
                bucketState = BucketState.DUMP;
                // Log.d("EBOTS", "dumpStartedFrom: (After set to DUMP)" + dumpStartedFrom.name());

            }
            // the target position varies in time to shake freight out
            setPos(getDumpPositionWithVibrate());
        } else if (bucketState == BucketState.DUMP) {
            toggleState();
        }
    }

    private void toggleState(){
        if (bucketState == BucketState.COLLECT){
            bucketState = BucketState.TRAVEL;
        } else if(bucketState == BucketState.TRAVEL) {
            bucketState = BucketState.COLLECT;
        } else {
            // if state was DUMP them return arm to level 1
            if (opMode instanceof EbotsTeleOp || opMode instanceof EbotsTeleOpV2){
                ((EbotsTeleOp) opMode).setJustDumped(true);
            }
            // whether state was TRAVEL or DUMP it sets it to collect
            bucketState = BucketState.TRAVEL;
        }
        setPos(bucketState.getServoSetting());
    }

    private double getDumpPositionWithVibrate(){
        double targetServoSetting = BucketState.DUMP.getServoSetting();
        double settingReduction = 0.1;  // amount to reduce setting
        // first allow bucket to dump or duration dumpTime
        long dumpTime = (dumpStartedFrom == BucketState.COLLECT) ? 450 : 200;
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
