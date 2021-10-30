package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.BarCodeObservation;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.EbotsAutonState;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateConfigureRoutine;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateObserveBarCode;

@Autonomous
public class AutonOpModeV1 extends EbotsAutonOpMode {


    private BarCodePosition barCodePosition;

    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }



    @Override
    public void runOpMode() throws InterruptedException {

        // The IMU sensor object
        BNO055IMU imu;

        // State used for updating telemetry
        Orientation angles;
        Acceleration gravity;
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

        boolean stateComplete = false;
        EbotsAutonState currentState = EbotsAutonState.get(StateConfigureRoutine.class,hardwareMap, this);
        while (!stateComplete){
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
            }

        }
        stateComplete = false;
        currentState = EbotsAutonState.get(StateObserveBarCode.class,hardwareMap, this);
        while (!stateComplete){
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
            }

        }


        waitForStart();
//        currentState = EbotsAutonState.get(StateDriveToCarousel.class,hardwareMap, this);
//        stateComplete = false;
//        while (opModeIsActive() && !stateComplete) {
//            if (currentState.shouldExit()) {
//                currentState.performTransitionalActions();
//                stateComplete = true;
//            } else {
//                currentState.performStateActions();
//                telemetry.addData("Current State", currentState.getClass().getSimpleName());
//                telemetry.update();
//            }
//
//        }
//        currentState = EbotsAutonState.get(StateDeliverDuck.class, hardwareMap,this);
//        stateComplete = false;
//        while (opModeIsActive() && !stateComplete) {
//            if (currentState.shouldExit()) {
//                currentState.performTransitionalActions();
//                stateComplete = true;
//            } else {
//                currentState.performStateActions();
//                telemetry.addData("Current State", currentState.getClass().getSimpleName());
//                telemetry.update();
//
//            }
//
//        }
    }
}
