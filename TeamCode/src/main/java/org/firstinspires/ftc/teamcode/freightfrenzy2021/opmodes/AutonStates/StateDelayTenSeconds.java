package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateDelayTenSeconds implements EbotsAutonState{

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Class Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Instance Attributes
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    StopWatch stopWatch;
    EbotsAutonOpMode autonOpMode;
    long stateTimeLimit;
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Constructors
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateDelayTenSeconds(EbotsAutonOpMode autonOpMode){
        this.autonOpMode = autonOpMode;
        stopWatch = new StopWatch();
        stateTimeLimit = 10000;
        this.autonOpMode = autonOpMode;

    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Getters & Setters
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Class Methods
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Instance Methods
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public boolean shouldExit() {
        boolean stateTimedOut = stopWatch.getElapsedTimeMillis() >= stateTimeLimit;

        return stateTimedOut | !autonOpMode.opModeIsActive();
}

    @Override
    public void performStateActions() {
        autonOpMode.telemetry.addLine(stopWatch.toString());
    }

    @Override
    public void performTransitionalActions() {
        autonOpMode.telemetry.addLine("Exiting state StateDelayFiveSeconds");
    }
}
