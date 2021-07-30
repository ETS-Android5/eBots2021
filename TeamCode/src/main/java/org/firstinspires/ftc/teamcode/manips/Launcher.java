package org.firstinspires.ftc.teamcode.manips;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher implements EbotsManip{

    private DcMotorEx launcher;
    final int  HIGH_GOAL = 1347;
    final int  LOW_GOAL = 1250;
    final int  POWER_SHOTS = 1303;

    public Launcher(HardwareMap hardwareMap){
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void handleGamepadInput(Gamepad gamepad) {
        // ************     LAUNCHER   **********************
        // Set the speed for the shooter
        //  Y - HIGH GOAL
        //  B - POWER SHOTS
        //  A - LOW GOAL
        //  X - STOP
        if(gamepad.y){
            launcher.setVelocity(HIGH_GOAL);
//            launcher.setPower(HIGH_GOAL);
        }else if(gamepad.b){
            launcher.setVelocity(POWER_SHOTS);
//            launcher.setPower(POWER_SHOTS);
        }else if(gamepad.a){
            launcher.setVelocity(LOW_GOAL);
//            launcher.setPower(LOW_GOAL);
        }else if(gamepad.x){
            launcher.setPower(0);
        }
    }

    @Override
    public void stop() {
        launcher.setPower(0);
    }

    public void startLauncher(){
        launcher.setVelocity(HIGH_GOAL);
    }

}
