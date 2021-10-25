package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.EbotsMotionController;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.BarCodeObservation;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.EbotsAutonState;
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
        boolean stateComplete = false;

        EbotsAutonState currentState = EbotsAutonState.get(StateObserveBarCode.class,hardwareMap, this);
        while (!stateComplete){
            if (currentState.shouldExit()) {
                currentState.performTransitionalActions();
                stateComplete = true;
            } else {
                currentState.performStateActions();
                telemetry.addData("Current State", currentState.getClass().getSimpleName());
                telemetry.update();
            }

        }


        waitForStart();
        currentState = EbotsAutonState.get(StateDriveToCarousel.class,hardwareMap, this);
        stateComplete = false;
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
        currentState = EbotsAutonState.get(StateDeliverDuck.class, hardwareMap,this);
        stateComplete = false;
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
    }
}
