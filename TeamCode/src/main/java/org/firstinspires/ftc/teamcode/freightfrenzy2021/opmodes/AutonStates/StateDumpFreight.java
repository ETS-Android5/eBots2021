package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Arm;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;


public class StateDumpFreight implements EbotsAutonState{
    private EbotsAutonOpMode autonOpMode;
    private StopWatch stopWatchState;
    private StopWatch stopWatchDump;
    private Bucket bucket;
    private Arm arm;
    private final long stateTimeLimit;
    private final long dumpTime;
    private Arm.Level targetLevel;
    private boolean targetLevelAchieved = false;

    public StateDumpFreight(EbotsAutonOpMode autonOpMode){
        this.autonOpMode = autonOpMode;
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
        bucket = new Bucket(hardwareMap);
        arm = new Arm(autonOpMode);
        arm.zeroArmHeight();
        stopWatchState = new StopWatch();
        stopWatchDump = new StopWatch();
        stateTimeLimit = 5000L;
        dumpTime = 2000L;
        BarCodePosition barCodePosition = autonOpMode.getBarCodePosition();
        targetLevel = Arm.Level.ONE;
        if (barCodePosition == BarCodePosition.MIDDLE){
            targetLevel = Arm.Level.TWO;
        } else if (barCodePosition == BarCodePosition.RIGHT){
            targetLevel = Arm.Level.THREE;
        }
        if (targetLevel == Arm.Level.ONE){
            targetLevelAchieved = true;
            stopWatchDump.reset();
            bucket.setState(BucketState.DUMP);
        } else {
            arm.moveToLevel(targetLevel);
        }
    }

    @Override
    public boolean shouldExit() {
        boolean dumpComplete = targetLevelAchieved && stopWatchDump.getElapsedTimeMillis() > dumpTime;
        boolean stateTimedOut = stopWatchState.getElapsedTimeMillis() > stateTimeLimit;
        return stateTimedOut | dumpComplete | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        if (arm.isAtTargetLevel() && !targetLevelAchieved){
            stopWatchDump.reset();
            targetLevelAchieved = true;
            bucket.setState(BucketState.DUMP);
        }
    }

    @Override
    public void performTransitionalActions() {
        bucket.setState(BucketState.COLLECT);
        arm.moveToLevel(Arm.Level.ONE);
    }
}
