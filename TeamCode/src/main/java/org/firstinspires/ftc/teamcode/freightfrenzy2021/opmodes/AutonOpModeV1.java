package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
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
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

@Autonomous
public class AutonOpModeV1 extends EbotsAutonOpMode {

    // These are already included in the abstract class EbotsAutonOpMode
    //private BarCodePosition barCodePosition;
    //private BNO055IMU imu;

    String logTag = "EBOTS";
    int statesCreated = 0;
    private EbotsAutonState currentState;
    private boolean stateComplete = false;

    @Override
    public void runOpMode() throws InterruptedException {
        // Setup the pre-match autonStates
        itinerary.add(StateConfigureRoutine.class);
        itinerary.add(StateObserveBarCode.class);

        Log.d(logTag, "About to start State Machine...");
        // Execute the pre-match state machine
        while (!isStarted()) {
            transitionToNextState();
            executeStateMachine();
        }

        if (imu == null) initImu();

        waitForStart();

        // Execute the rest of the autonStates
        while (opModeIsActive()) {
            transitionToNextState();
            executeStateMachine();
        }
    }

    private void executeStateMachine(){
        while (!stateComplete) {
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                updateTelemetry();
            }
        }
        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
    }

    private void transitionToNextState(){
        // get the next state if exists
        if (itinerary.size() > 0){
            stateComplete = false;
            Class nextStateClass = itinerary.remove(0);
            currentState = EbotsAutonState.get(nextStateClass, hardwareMap, this);
            logNewlyCreatedState(currentState);
            telemetry.clearAll();
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

    private void updateTelemetry(){
        telemetry.addData("Current State", currentState.getClass().getSimpleName());
        telemetry.addData("Current heading", getCurrentHeadingDeg());
        telemetry.update();
    }
}
