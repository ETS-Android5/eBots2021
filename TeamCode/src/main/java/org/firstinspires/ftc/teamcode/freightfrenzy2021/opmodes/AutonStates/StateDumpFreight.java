package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.BucketState;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.manips2021.Bucket;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

import java.util.ArrayList;

public class StateDumpFreight implements EbotsAutonState{
    private EbotsAutonOpMode autonOpMode;
    private StopWatch stopWatch;
    private Bucket bucket;
    private final long stateTimeLimit;


    public StateDumpFreight(EbotsAutonOpMode autonOpMode){
        this.autonOpMode = autonOpMode;
        HardwareMap hardwareMap = autonOpMode.hardwareMap;
        bucket = new Bucket(hardwareMap);
        stopWatch = new StopWatch();
        stateTimeLimit = 2000L;
    }

    @Override
    public boolean shouldExit() {
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() > stateTimeLimit;
        return stateTimedOut | !autonOpMode.opModeIsActive();
    }

    @Override
    public void performStateActions() {
        bucket.setState(BucketState.DUMP);
    }

    @Override
    public void performTransitionalActions() {
        bucket.setState(BucketState.COLLECT);
    }
}
