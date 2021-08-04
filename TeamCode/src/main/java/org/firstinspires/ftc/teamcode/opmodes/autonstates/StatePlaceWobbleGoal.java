package org.firstinspires.ftc.teamcode.opmodes.autonstates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.EbotsRobot;
import org.firstinspires.ftc.teamcode.StopWatch;

public class StatePlaceWobbleGoal extends AbstractAutonState {

    long stateTimeLimit;
    StopWatch stateStopWatch;

    // ***********   CONSTRUCTOR   ***********************
    public StatePlaceWobbleGoal(LinearOpMode opModeIn, EbotsRobot robotIn, Class<? extends AbstractAutonState> nextAutonState){
        // Call the generic constructor from the super class (AbstractAutonState) to initialize opmode, robot, nextAutonStateClass
        super(opModeIn, robotIn, nextAutonState);

        stateTimeLimit = 750L;
        stateStopWatch = new StopWatch();
        robot.getGripper().toggleGripper();
    }

    // ***********   GETTERS    ***********************

    // NOTE: there are default getters in AbstractAutonState for
    //      getCurrentAutonState
    //      getNextAutonState

    // ***********   INTERFACE METHODS   ***********************
    @Override
    public boolean areExitConditionsMet() {
        // Time limit is a dummy condition until manip mech ready
        return (stateStopWatch.getElapsedTimeMillis() > stateTimeLimit | !opMode.opModeIsActive());
    }

    @Override
    public void performStateSpecificTransitionActions() {

    }

    @Override
    public void performStateActions() {
//        robot.getEbotsMotionController().moveToTargetPose(robot, stateStopWatch);
        //report telemetry

        opMode.telemetry.addData("Current State ", currentAutonState.getSimpleName());
        opMode.telemetry.addLine(stateStopWatch.toString() + " time limit " + stateTimeLimit);
        opMode.telemetry.update();
    }
}
