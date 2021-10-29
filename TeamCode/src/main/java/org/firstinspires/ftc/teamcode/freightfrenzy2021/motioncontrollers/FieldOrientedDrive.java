package org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.ebotsenums.RobotSide;
import org.firstinspires.ftc.teamcode.ebotsenums.WheelPosition;

import java.util.ArrayList;
import java.util.Arrays;

public class FieldOrientedDrive implements EbotsMotionController {
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private ArrayList<MecanumWheel> mecanumWheels = new ArrayList<>();
    private double maxAllowedPower;     // between 0-1
    private double spinScaleFactor;     // between 0-1 to reduce spin power
    private double requestedTranslateMagnitude;
    private double translateAngleRad;

    // The IMU sensor object
    private BNO055IMU imu;

    // State used for updating telemetry
    private Orientation angles;
    private Acceleration gravity;

    private double currentHeadingDeg;          // current field-heading of the robot

    private double zeroHeadingDeg = 0;      // the field heading of the robot when imu was initialized (90 facing Y+)

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public FieldOrientedDrive(HardwareMap hardwareMap){
        maxAllowedPower = 1.0;
        spinScaleFactor = 1.0;

        // Create a list of mecanum wheels and store in mecanumWheels
        // Wheel rollers are either 45 or -45 degrees.  Note which ones are negative with this list
        ArrayList<WheelPosition> positionsWithNegativeAngle = new ArrayList<>(
                Arrays.asList(WheelPosition.FRONT_LEFT, WheelPosition.BACK_RIGHT)  // X-Config
        );

        //Loop through each WheelPosition (e.b. FRONT_LEFT, FRONT_RIGHT)
        for(WheelPosition pos: WheelPosition.values()){
            // get motorName and initialize it
            String motorName = pos.getMotorName();
            DcMotorEx motor = hardwareMap.get(DcMotorEx.class, motorName);
            //  Reverse motor depending on side
            if(pos.getRobotSide() == RobotSide.RIGHT) motor.setDirection(DcMotorSimple.Direction.REVERSE);

            // set the angle of the rollers, modifying sign if needed
            double wheelAngleDeg = 45;
            if (positionsWithNegativeAngle.contains(pos)) wheelAngleDeg = -wheelAngleDeg;

            // create the new wheel and add it to the list
            MecanumWheel mecanumWheel = new MecanumWheel(wheelAngleDeg, pos, motor);
            mecanumWheels.add(mecanumWheel);
        }

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public double getCurrentHeadingDeg(){
        return currentHeadingDeg;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public double getZeroHeadingDeg() {
        return zeroHeadingDeg;
    }

    public void setZeroHeadingDeg() {
        this.zeroHeadingDeg = applyAngleBounds(zeroHeadingDeg + 90);
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

    public void performImuHardwareRead(){
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
        currentHeadingDeg = applyAngleBounds(angles.firstAngle + zeroHeadingDeg);
    }

    @Override
    public void stop() {
        for(MecanumWheel mecanumWheel: mecanumWheels){
            mecanumWheel.getMotor().setPower(0.0);
        }
    }

    @Override
    public void handleUserInput(Gamepad gamepad) {
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
        double forwardInput = -gamepad.left_stick_y;  //reversing sign because up on gamepad is negative
        double lateralInput = -gamepad.left_stick_x;  //reversing sign because right on gamepad is positive
        double spinInput = -gamepad.right_stick_x * spinScaleFactor;    //Positive means to spin to the left (counterclockwise (CCW) when looking down on robot)
        performImuHardwareRead();

        //  Step 1:  Calculate the magnitude for drive signal (hypotenuse of xDirDrive and yDirDrive signal)
        //  Step 2:  Calculate the translate angle (based on X & Y signals, robot heading is not a consideration)
        //  Step 3:  Calculate the robot angle, which adjusts for robot orientation
        //  Step 4:  Calculate the motor power and set mecanumWheel attribute calculated power (doesn't set motor power yet)
        //  Step 5:  If needed, scale all the calculated powers so max value equals maxAllowedPower
        //  Step 6:  Apply slow-mo or scale up if required

        //  Step 1:  Calculate the magnitude for drive signal (hypotenuse of xDirDrive and yDirDrive signal)
        requestedTranslateMagnitude = Math.hypot(forwardInput, lateralInput);

        //  Step 2:  Calculate the translate angle (based on X & Y signals, robot heading is not a consideration)
        translateAngleRad = Math.atan2(lateralInput, forwardInput);

        //  Step 3:  Calculate the robot angle, which adjusts for robot orientation
        double driveAngleRad = translateAngleRad - Math.toRadians(currentHeadingDeg);
        // overflow of angle is OK here, the calculation isn't affected
        //driveAngleRad=Math.toRadians(applyAngleBounds(Math.toDegrees(driveAngleRad)));

        //  Step 4:  Calculate and set attribute calculatedPower for each wheel (doesn't set motor power yet)
        for(MecanumWheel mecanumWheel: mecanumWheels) {
            // to calculate power, must offset translate angle by wheel roller angle
            double calcAngleRad = driveAngleRad - mecanumWheel.getWheelAngleRad();
            double translatePower = requestedTranslateMagnitude * Math.cos(calcAngleRad);
            double spinPower = mecanumWheel.getWheelPosition().getSpinSign() * spinInput;
            mecanumWheel.setCalculatedPower(translatePower + spinPower);
        }

        //  Step 5:  If needed, scale all the calculated powers so max value equals maxAllowedPower
        double maxCalculatedPowerMagnitude = getMaxCalculatedPowerMagnitude();
        if (maxCalculatedPowerMagnitude>maxAllowedPower){
            double scaleFactor = maxAllowedPower/maxCalculatedPowerMagnitude;
            this.applyScaleToCalculatedDrive(scaleFactor);
        }

        //  Step 6:  Apply slow-mo or scale up if required
        //Get the input for Super Slo-Mo
        double superSloMoInput = 1-gamepad.left_trigger;
        double thresholdValue = 0.85;

        // Either: apply super slo-mo  OR  maximize power when driving fast
        if (superSloMoInput < thresholdValue){
            superSloMoInput = Math.max(0.3, superSloMoInput);
            applyScaleToCalculatedDrive(superSloMoInput);
        }else if (requestedTranslateMagnitude >= thresholdValue &&
                maxCalculatedPowerMagnitude < requestedTranslateMagnitude){
            // sometimes the calculation doesn't drive as fast as expected
            // for instance, moving forward sets all motors to 0.707 power
            // this conditional scales powers up to equal the requested translate magnitude
            // but only when speed matters, so if requested magnitude is > 0.85
            double scaleFactor = requestedTranslateMagnitude / maxCalculatedPowerMagnitude;
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

}
