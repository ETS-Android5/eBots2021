package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public interface EbotsAutonState {

    public boolean shouldExit();

    public void performStateActions();

    public void performTransitionalActions();

    public static EbotsAutonState get(Class targetState, EbotsAutonOpMode autonOpMode){
        EbotsAutonState newState = null;
        if (targetState == StateDriveToCarousel.class){
            newState = new StateDriveToCarousel(autonOpMode);
        } else if (targetState == StateDeliverDuck.class){
            newState = new StateDeliverDuck(autonOpMode);
        } else if (targetState == StateObserveBarCode.class){
            newState = new StateObserveBarCode(autonOpMode);
        } else if (targetState == StateConfigureRoutine.class){
            newState = new StateConfigureRoutine(autonOpMode);
        } else if (targetState == StateNavigateToWarehouse.class){
            newState = new StateNavigateToWarehouse(autonOpMode);
        } else if (targetState == StateMoveToHubX.class){
            newState = new StateMoveToHubX(autonOpMode);
        } else if (targetState == StateReverseToHub.class){
            newState = new StateReverseToHub(autonOpMode);
        } else if (targetState == StatePushOff.class){
            newState = new StatePushOff(autonOpMode);
        } else if (targetState == StateRotateNinetyDegrees.class){
            newState = new StateRotateNinetyDegrees(autonOpMode);
        } else if (targetState == StateRotateToZeroDegrees.class){
            newState = new StateRotateToZeroDegrees(autonOpMode);
        } else if (targetState == StateMoveToWarehouseY.class){
            newState = new StateMoveToWarehouseY(autonOpMode);
        } else if (targetState == StateObserveBarCodeMarkers.class){
            newState = new StateObserveBarCodeMarkers(autonOpMode);
        } else if (targetState == StateReverseToHubUsingImage.class){
            newState = new StateReverseToHubUsingImage(autonOpMode);
        } else if (targetState == StateObserveTeamShippingElement.class){
            newState = new StateObserveTeamShippingElement(autonOpMode);
        } else if (targetState == StateDumpFreight.class){
            newState = new StateDumpFreight(autonOpMode);
        } else if (targetState == StateRotateForHubDump.class){
            newState = new StateRotateForHubDump(autonOpMode);
        } else if (targetState == StateRotateToZeroDegreesV2.class){
        newState = new StateRotateToZeroDegreesV2(autonOpMode);
    }
        return newState;
    }


}

