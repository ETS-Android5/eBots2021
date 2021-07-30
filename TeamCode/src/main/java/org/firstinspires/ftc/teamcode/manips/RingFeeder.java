package org.firstinspires.ftc.teamcode.manips;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.StopWatch;

public class RingFeeder implements EbotsManip{

    private Servo ringFeeder;
    private StopWatch ringFeederCycleTimer = new StopWatch();


    final long CYCLE_TIME = 500;    // intended to be time to move between positions
    final double RECEIVE = 0.06;
    final double FEED =    0.37;


    public RingFeeder(HardwareMap hardwareMap){
        ringFeeder = hardwareMap.get(Servo.class, "ringFeeder");
    }

    @Override
    public void handleGamepadInput(Gamepad gamepad) {
        // ************     RING FEEDER     **********************
        // ring feeder servo should cycle between 2 positions: RECEIVE and FEED
        // time is used to control cycle
        // cycle is triggered using right trigger

        double inputThreshold = 0.3;

        boolean triggerPressed = Math.abs(gamepad.right_trigger) > inputThreshold;
        //  readyToReceiveRing makes sure that the servo is back to the original position before cycling again
        //  also, conveyors shouldn't feed into shooter if not ready for feed
        boolean cycleTimeout = (ringFeederCycleTimer.getElapsedTimeMillis() > (2*CYCLE_TIME));
        double errorFromReceivePosition = ringFeeder.getPosition() - RECEIVE;
        double errorFromFeedPosition = ringFeeder.getPosition() - FEED;

        if(triggerPressed  && (cycleTimeout)){
            ringFeederCycleTimer.reset();
            ringFeeder.setPosition(FEED);
        } else if(ringFeederCycleTimer.getElapsedTimeMillis() > CYCLE_TIME){
            ringFeeder.setPosition(RECEIVE);
        }

    }

    @Override
    public void stop() {
        //  Find the current position and set as target position
        ringFeeder.setPosition(ringFeeder.getPosition());
    }

    public void feedRing(){
        ringFeederCycleTimer.reset();
        ringFeeder.setPosition(FEED);
        while(ringFeederCycleTimer.getElapsedTimeMillis() < CYCLE_TIME){
            //wait for a cycle
        }
        ringFeeder.setPosition(RECEIVE);
    }

}
