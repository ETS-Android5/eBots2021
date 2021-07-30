package org.firstinspires.ftc.teamcode.manips;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Conveyor implements EbotsManip{

    private DcMotorEx conveyor;


    public Conveyor(HardwareMap hardwareMap){
        conveyor = hardwareMap.get(DcMotorEx.class, "conveyor");
        conveyor.setDirection(DcMotorSimple.Direction.FORWARD);
        conveyor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        conveyor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void handleGamepadInput(Gamepad gamepad) {
        double inputThreshold = 0.3;
        double conveyorInput = -gamepad.left_stick_y;

        // Condition the input signal to either be -1, 0, or 1
        double conveyorPower = (Math.abs(conveyorInput) < inputThreshold) ? 0 : Math.signum(conveyorInput) * 1;

        conveyor.setPower(conveyorPower);

    }

    @Override
    public void stop() {
        conveyor.setPower(0);
    }

    public void startConveyor(){
        conveyor.setPower(1.0);
    }

}
