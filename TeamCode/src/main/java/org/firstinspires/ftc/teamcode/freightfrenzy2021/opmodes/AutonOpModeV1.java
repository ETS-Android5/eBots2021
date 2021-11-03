package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.EbotsAutonState;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateConfigureRoutine;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateObserveBarCode;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateObserveBarCodeMarkers;

@Autonomous
public class AutonOpModeV1 extends EbotsAutonOpMode {

    String logTag = "EBOTS";
    int statesCreated = 0;
    private EbotsAutonState currentState;
    private boolean stateComplete = false;

    @Override
    public void runOpMode() throws InterruptedException {
        // Setup the pre-match autonStates
        itinerary.add(StateConfigureRoutine.class);
        itinerary.add(StateObserveBarCode.class);

        // prevent auto-clear of telemetry

        Log.d(logTag, "About to start State Machine...");
        // Execute the pre-match state machine
        while (!isStarted()) {
            transitionToNextState();
            executeStateMachine();
        }

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
//        Log.d(logTag, "....Completed state " + currentState.getClass().getSimpleName());
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
//            Log.d(logTag, "No more states in routine!!!");
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
        telemetry.addData("Current heading", EbotsImu.getCurrentFieldHeadingDeg(false));
        telemetry.update();
    }
}
