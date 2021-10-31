package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.EbotsAutonState;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateConfigureRoutine;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDeliverDuck;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateDriveToCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateMoveToHubX;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateObserveBarCode;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StatePushOff;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateReverseToHub;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateRotateToZeroDegrees;

@Autonomous
public class AutonOpModeV1 extends EbotsAutonOpMode {

    // These are already included in the abstract class EbotsAutonOpMode
    //private BarCodePosition barCodePosition;
    //private BNO055IMU imu;

    String logTag = "EBOTS";
    int statesCreated = 0;
    private EbotsAutonState currentState;
    private boolean stateComplete;

    @Override
    public void runOpMode() throws InterruptedException {


        // State used for updating telemetry
        Orientation angles;
        Acceleration gravity;
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        itinerary.add(StateConfigureRoutine.class);
        itinerary.add(StateObserveBarCode.class);


        Log.d(logTag, "About to start State Machine...");
        transitionToNextState();

        while (!stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (!stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
            }
        }
        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());

        waitForStart();
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }

        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
        transitionToNextState();

        while (opModeIsActive() && !stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }
        }
        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());

    }

    private void transitionToNextState(){
        stateComplete = false;

        // get the next state if exists
        if (itinerary.size() > 0){
            Class nextStateClass = itinerary.remove(0);
            currentState = EbotsAutonState.get(nextStateClass, hardwareMap, this);
            logNewlyCreatedState(currentState);
        } else {
            Log.d(logTag, "No more states in routine!!!");
        }
    }

    private void logNewlyCreatedState(EbotsAutonState newState){

        statesCreated++;
        String intfmt = "%d";
        String strStateCount = String.format(intfmt, statesCreated);

        try{
            Log.d(logTag, "State #" + strStateCount + " created type " + newState.getClass().getSimpleName());
        } catch (NullPointerException e){
                Log.d(logTag, "Error creating state #" + strStateCount + ".  Returned Null");
                Log.d(logTag, e.getStackTrace().toString());
        } catch (Exception e) {
                Log.d(logTag, "Exception encountered " + e.getStackTrace().toString());
        }

    }
}
