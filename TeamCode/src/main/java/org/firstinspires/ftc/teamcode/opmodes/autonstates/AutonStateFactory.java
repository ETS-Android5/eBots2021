package org.firstinspires.ftc.teamcode.opmodes.autonstates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.EbotsRobot;

public class AutonStateFactory {
    /**
     * This class acts as a factory for AutonStates which implement the AutonState interface
     */

    public AutonState getAutonState(Class<? extends AutonState> targetState, LinearOpMode opMode,
                                    EbotsRobot robot, Class<? extends AbstractAutonState> nextState) {
        AutonState returnState = null;

        if (targetState == StatePrematchSetup.class) {
            returnState = new StatePrematchSetup(opMode, robot, nextState);
        }


        return returnState;
    }

    @Deprecated
    public AutonState getAutonState(AutonStateEnum autonStateEnum, LinearOpMode opMode, EbotsRobot robot) {
        AutonState returnState = null;

        if (autonStateEnum == null) {
            returnState = null;
        }
        return returnState;
    }
}
