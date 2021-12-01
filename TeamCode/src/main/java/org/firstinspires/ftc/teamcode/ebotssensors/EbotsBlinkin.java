package org.firstinspires.ftc.teamcode.ebotssensors;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;

public class EbotsBlinkin {

    private RevBlinkinLedDriver blinkinLedDriver;
    private StopWatch stopWatch = new StopWatch();
    private final long lockoutDuration = 500L;
    private LedState ledState = LedState.OFF;

    public enum LedState{
        ON,
        OFF
    }

    public EbotsBlinkin(HardwareMap hardwareMap){
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
    }

    public void lightsOn(){
        blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        ledState = LedState.ON;
    }

    public void lightsOff(){
        blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        ledState = LedState.OFF;
    }

    private void toggleLedState(){
        if(ledState == LedState.OFF){
            lightsOn();
        } else {
            lightsOff();
        }
    }

    public void handleUserInput(Gamepad gamepad) {
        boolean lockoutActive = stopWatch.getElapsedTimeMillis() < lockoutDuration;
        if(lockoutActive) return;

        if(gamepad.dpad_right){
            toggleLedState();
        }
        stopWatch.reset();

    }

    }
