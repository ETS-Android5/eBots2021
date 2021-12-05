package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public interface EbotsAutonState {

    public boolean shouldExit();

    public void performStateActions();

    public void performTransitionalActions();

    public static EbotsAutonState get(Class targetState, EbotsAutonOpMode autonOpMode){
        EbotsAutonState newState = null;
        if (targetState == StateDeliverDuck.class){
            newState = new StateDeliverDuck(autonOpMode);
        } else if (targetState == StateConfigureRoutine.class){
            newState = new StateConfigureRoutine(autonOpMode);
        } else if (targetState == StateNavigateToWarehouse.class){
            newState = new StateNavigateToWarehouse(autonOpMode);
        } else if (targetState == StateMoveToHubX.class){
            newState = new StateMoveToHubX(autonOpMode);
        } else if (targetState == StateDumpFreight.class){
            newState = new StateDumpFreight(autonOpMode);
        } else if (targetState == StateRotateToZeroDegreesV2.class){
            newState = new StateRotateToZeroDegreesV2(autonOpMode);
        }  else if (targetState == StateRotateToCollect.class){
            newState = new StateRotateToCollect(autonOpMode);
        } else if (targetState == StateCollectFreight.class){
            newState = new StateCollectFreight(autonOpMode);
        } else if (targetState == StateCalibrateHubX.class){
            newState = new StateCalibrateHubX(autonOpMode);
        } else if (targetState == StateOpenCVObserve.class){
            newState = new StateOpenCVObserve(autonOpMode);
        } else if (targetState == StatePushOffWithEncoders.class){
            newState = new StatePushOffWithEncoders(autonOpMode);
        } else if (targetState == StateRotateForDeliverDuck.class){
            newState = new StateRotateForDeliverDuck(autonOpMode);
        } else if (targetState == StatePushOffAllianceHub.class){
            newState = new StatePushOffAllianceHub(autonOpMode);
        } else if (targetState == StateReverseToHubUsingEncoders.class){
            newState = new StateReverseToHubUsingEncoders(autonOpMode);
        } else if (targetState == StateRotateToFieldCenter.class){
            newState = new StateRotateToFieldCenter(autonOpMode);
        } else if (targetState == StateMoveToHubY.class){
            newState = new StateMoveToHubY(autonOpMode);
        } else if (targetState == StateRotate180.class){
            newState = new StateRotate180(autonOpMode);
        } else if (targetState == StateDriveToWall.class){
            newState = new StateDriveToWall(autonOpMode);
        } else if (targetState == StateParkInStorageUnit.class) {
            newState = new StateParkInStorageUnit(autonOpMode);
        } else if (targetState == StateReverseToCarouselWithEncoders.class) {
            newState = new StateReverseToCarouselWithEncoders(autonOpMode);
        } else if (targetState == StatePushOffWithEncodersBlue.class) {
            newState = new StatePushOffWithEncodersBlue(autonOpMode);
        } else if (targetState == StateRotateToDeliverDuckBlue.class) {
            newState = new StateRotateToDeliverDuckBlue(autonOpMode);
        } else if (targetState == StateStrafeRight.class) {
            newState = new StateStrafeRight(autonOpMode);
        } else if (targetState == StateDelayFiveSeconds.class) {
            newState = new StateDelayFiveSeconds(autonOpMode);
        } else if (targetState == StateDelayTenSeconds.class) {
            newState = new StateDelayTenSeconds(autonOpMode);
        } else if (targetState == StateStrafeRightForWarehouse.class) {
            newState = new StateStrafeRightForWarehouse(autonOpMode);
        } else if (targetState == StateDriveToCarousel.class) {
            newState = new StateDriveToCarousel(autonOpMode);
        } else if (targetState == StateObserveBarCode.class){
            newState = new StateObserveBarCode(autonOpMode);
        } else if (targetState == StateDriveToCarouselWithDeadReckoningNav.class){
            newState = new StateDriveToCarouselWithDeadReckoningNav(autonOpMode);
        } else if (targetState == StateMoveToHubXWithEncoders.class){
            newState = new StateMoveToHubXWithEncoders(autonOpMode);
        } else if (targetState == StateRotateForHubDump.class){
            newState = new StateRotateForHubDump(autonOpMode);
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
        }

        return newState;
    }


}

