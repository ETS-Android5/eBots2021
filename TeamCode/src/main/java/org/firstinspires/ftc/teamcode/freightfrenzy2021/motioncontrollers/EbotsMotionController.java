package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public interface EbotsMotionController {
    public String getName();

    public void stop();

    public void handleUserInput(Gamepad gamepad);

    public static EbotsMotionController get(Class targetClass, LinearOpMode opMode){
        EbotsMotionController outputMotionController = null;
        if(targetClass == DriveAndSpin.class){
            outputMotionController = new DriveAndSpin(opMode);
        } else if(targetClass == MecanumDrive.class){
            outputMotionController = new MecanumDrive(opMode);
        } else if(targetClass == FieldOrientedDrive.class){
            outputMotionController = new FieldOrientedDrive(opMode);
        }

        return outputMotionController;
    }

}
