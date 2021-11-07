package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ebotsenums.CsysDirection;
import org.firstinspires.ftc.teamcode.ebotsenums.RobotSide;
import org.firstinspires.ftc.teamcode.ebotsenums.Speed;
import org.firstinspires.ftc.teamcode.ebotsenums.WheelPosition;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.PoseError;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonDrive implements EbotsMotionController {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private ArrayList<MecanumWheel> mecanumWheels = new ArrayList<>();
    private double requestedTranslateMagnitude;
    private double translateAngleRad;

    private Speed speed;


    // The IMU sensor object
    //private EbotsImu imu; // use singleton

    private double currentHeadingDeg;       // current field-heading of the robot

    private PoseError poseError;
    private double xSignal;
    private double ySignal;
    private double spinSignal;

    private String logTag = "EBOTS";

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public AutonDrive(EbotsAutonOpMode autonOpMode){
        this.speed = Speed.MEDIUM;

        // Create a list of mecanum wheels and store in mecanumWheels
        // Wheel rollers are either 45 or -45 degrees.  Note which ones are negative with this list
        ArrayList<WheelPosition> positionsWithNegativeAngle = new ArrayList<>(
                Arrays.asList(WheelPosition.FRONT_LEFT, WheelPosition.BACK_RIGHT)  // X-Config
        );

        //Loop through each WheelPosition (e.b. FRONT_LEFT, FRONT_RIGHT)
        for(WheelPosition pos: WheelPosition.values()){
            // get motorName and initialize it
            String motorName = pos.getMotorName();
            DcMotorEx motor = autonOpMode.hardwareMap.get(DcMotorEx.class, motorName);
            //  Reverse motor depending on side
            if(pos.getRobotSide() == RobotSide.RIGHT) motor.setDirection(DcMotorSimple.Direction.REVERSE);

            // set the angle of the rollers, modifying sign if needed
            double wheelAngleDeg = 45;
            if (positionsWithNegativeAngle.contains(pos)) wheelAngleDeg = -wheelAngleDeg;

            // create the new wheel and add it to the list
            MecanumWheel mecanumWheel = new MecanumWheel(wheelAngleDeg, pos, motor);
            mecanumWheels.add(mecanumWheel);
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public Speed getSpeed() {
        return speed;
    }

    public double getTranslateMagnitude(){
        double translateMagnitude = requestedTranslateMagnitude;
        translateMagnitude = Math.min(speed.getMaxSpeed(), translateMagnitude);
        return translateMagnitude;
    }

    public double getTranslateAngleRad() {
        return translateAngleRad;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private static double applyAngleBounds(double inputAngle){
        double outputAngle = inputAngle;

        while(outputAngle > 180) {
            outputAngle -= 360;
        }
        while (outputAngle <=-180){
            outputAngle += 360;
        }
        return outputAngle;
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public boolean isSignalSaturated(CsysDirection csysDirection){
        boolean isSignalSaturated = true;

        if (csysDirection == CsysDirection.X){
            isSignalSaturated = Math.abs(xSignal) > speed.getMaxSpeed();

        } else if (csysDirection == CsysDirection.Y){
            isSignalSaturated = Math.abs(ySignal) > speed.getMaxSpeed();

        } else if (csysDirection == CsysDirection.Heading){
            isSignalSaturated = Math.abs(spinSignal) > speed.getTurnSpeed();
        }

        return isSignalSaturated;
    }

    public void performImuHardwareRead(){
        currentHeadingDeg = EbotsImu.getCurrentFieldHeadingDeg(true);
    }


    @Override
    public void stop() {
        for(MecanumWheel mecanumWheel: mecanumWheels){
            mecanumWheel.getMotor().setPower(0.0);
        }
    }

    @Override
    public void handleUserInput(Gamepad gamepad) {

    }

    public void calculateDriveFromError(PoseError poseError){
        //  For field-oriented drive, the inputted forward and lateral commands are intepreted as:
        //  Assuming Red Alliance:
        //      FORWARD:  Robot translates Field Coordinate Y+
        //      LATERAL:  Left translated Field Coordinate X-

        //      Two angles become important:
        //          *translate angle - Field angle that the robot should travel (regardless of robot heading)
        //          *drive angle     - Angle of motion relative to ROBOT coordinate system
        //
        //      Example: Requested travel is in X direction (0), Robot is facing Y+ (90deg)
        //               translateAngle = 0
        //               driveAngle = 0-90 = -90  so robot drives to the right
        //  Step 1:  Use the poseError object to calculate X & Y signals based on PID coefficients from speed settings
        //  Step 2:  Calculate the spin signal using PID coefficients from speed settings
        //  Step 3:  Set values in the driveCommand object for magnitude, driveAngleRad, and spin based on speed limits

        //  Robot Drive Angle is interpreted as follows:
        //
        //      0 degrees -- forward - (Positive X-Direction)
        //      90 degrees -- left   - (Positive Y-Direction)
        //      180 degrees -- backwards (Negative X-Direction)
        //      -90 degrees -- right    (Negative Y-Direction)
        //
        //  NOTE: This convention follows the right hand rule method, where :
        //      +X --> Forward, +Y is Left, +Z is up
        //   +Spin --> Counter clockwise

        //Read in the gamepad inputs and update current heading
        xSignal = poseError.getXError() * speed.getK_p() + poseError.getXErrorSum() * speed.getK_i();
        ySignal = poseError.getYError() * speed.getK_p() + poseError.getYErrorSum() * speed.getK_i();
        spinSignal = poseError.getHeadingErrorDeg() * speed.getS_p() + poseError.getHeadingErrorDegSum() * speed.getS_i();
        performImuHardwareRead();

        //  Step 1:  Calculate the magnitude for drive signal (hypotenuse of xDirDrive and yDirDrive signal)
        //  Step 2:  Calculate the translate angle (based on X & Y signals, robot heading is not a consideration)
        //  Step 3:  Calculate the robot angle, which adjusts for robot orientation
        //  Step 4:  Calculate the motor power and set mecanumWheel attribute calculated power (doesn't set motor power yet)
        //  Step 5:  If needed, scale all the calculated powers so max value equals maxAllowedPower

        //  Step 1:  Calculate the magnitude for drive signal (hypotenuse of xDirDrive and yDirDrive signal)
        requestedTranslateMagnitude = Math.hypot(xSignal, ySignal);

        //  Step 2:  Calculate the translate angle (based on X & Y signals, in auton, this is field oriented)
        translateAngleRad = Math.atan2(ySignal, xSignal);

        //  Step 3:  Calculate the robot angle, which adjusts for robot orientation
        double driveAngleRad = translateAngleRad - Math.toRadians(currentHeadingDeg);
        // overflow of angle is OK here, the calculation isn't affected
        //driveAngleRad=Math.toRadians(applyAngleBounds(Math.toDegrees(driveAngleRad)));

        //  Step 4:  Calculate and set attribute calculatedPower for each wheel (doesn't set motor power yet)
        for(MecanumWheel mecanumWheel: mecanumWheels) {
            // to calculate power, must offset translate angle by wheel roller angle
            double calcAngleRad = driveAngleRad - mecanumWheel.getWheelAngleRad();
            double translatePower = requestedTranslateMagnitude * Math.cos(calcAngleRad);
            double spinPower = mecanumWheel.getWheelPosition().getSpinSign() * spinSignal;
            mecanumWheel.setCalculatedPower(translatePower + spinPower);
        }

        //  Step 5:  If needed, scale all the calculated powers so max value equals maxAllowedPower
        double maxAllowedPower = speed.getMaxSpeed();
        double maxCalculatedPowerMagnitude = getMaxCalculatedPowerMagnitude();
        if (maxCalculatedPowerMagnitude>maxAllowedPower){
            double scaleFactor = maxAllowedPower/maxCalculatedPowerMagnitude;
            this.applyScaleToCalculatedDrive(scaleFactor);
        }

        // This sets the motor to the calculated power to move robot
        drive();
    }

    private double getMaxCalculatedPowerMagnitude(){
        //Loop through the drive motors and return the max abs value of the calculated drive
        double maxCalculatedPowerMagnitude=0;
        for(MecanumWheel mecanumWheel: mecanumWheels){
            maxCalculatedPowerMagnitude = Math.max(maxCalculatedPowerMagnitude, Math.abs(mecanumWheel.getCalculatedPower()));
        }
        return maxCalculatedPowerMagnitude;
    }

    public void applyScaleToCalculatedDrive(double scaleFactor){
        //Loop through each driveWheel and scale calculatedDrive
        for(MecanumWheel mecanumWheel: mecanumWheels){
            double newPower = mecanumWheel.getCalculatedPower() * scaleFactor;
            mecanumWheel.setCalculatedPower(newPower);
        }
    }

    public void drive(){
        // set the calculated power to each wheel
        for(MecanumWheel mecanumWheel: mecanumWheels){
            mecanumWheel.getMotor().setPower(mecanumWheel.getCalculatedPower());
        }
    }

    public long calculateTimeLimitMillis(PoseError poseError){
        boolean debugOn = false;
        //Find the expected time required to achieve the target pose
        if(debugOn) Log.d(logTag, "Entering calculateTimeLimitMillis...");

        //First, read in required travel distance and spin
        double translateDistance = poseError.getMagnitude();
        double rotationAngleRad = Math.abs(poseError.getHeadingErrorRad());  //Don't allow negative value

        //Take the robot top Speed (in/s) and multiply by Speed object's top speed [0-1]
        double topTranslationSpeed = speed.getMeasuredTranslateSpeed() * speed.getMaxSpeed();

        //Take robots top angular speed (rad/s) and multiply by Speed Objects top turn speed[0-1]
        double topSpinSpeed = speed.getMeasuredAngularSpeedRad() * speed.getTurnSpeed();

        long translateTimeMillis = (long) ((translateDistance / topTranslationSpeed)*1000);
        long spinTimeMillis = (long) ((rotationAngleRad / topSpinSpeed)*1000);
        long bufferMillis = 1000L;      //The buffer is a little extra time allotted (maybe should be percentage)

        //The total calculated time is the travel time and spin time and buffer (plus the soft start duration)
        long calculatedTime = (translateTimeMillis + spinTimeMillis + bufferMillis);
        //long calculatedTime = (translateTimeMillis + spinTimeMillis + bufferMillis + softStart.getDurationMillis());

        if(debugOn) Log.d(logTag, "Calculated Time: " + String.format("%.2f", (float)(calculatedTime/1000)) + " s");
        return (calculatedTime);
    }


}
