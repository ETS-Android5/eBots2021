package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsBlinkin;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.StopWatch;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;

public class StateCalibratingImu implements EbotsAutonState{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private DigitalChannel frontRollerTouch;
    private DigitalChannel backRollerTouch;
    private boolean initComplete = false;
    private EbotsAutonOpMode autonOpMode;
    private HardwareMap hardwareMap;
    private EbotsImu ebotsImu;
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateCalibratingImu(EbotsAutonOpMode autonOpMode){
        this.hardwareMap = autonOpMode.hardwareMap;
        this.autonOpMode = autonOpMode;
        if (AllianceSingleton.isBlue()){
            frontRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "leftFrontTouch");
            backRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "leftBackTouch");
        } else {
            frontRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "rightFrontTouch");
            backRollerTouch = autonOpMode.hardwareMap.get(DigitalChannel.class, "rightBackTouch");
        }
        EbotsBlinkin.getInstance(hardwareMap).lightsRed();
        ebotsImu = EbotsImu.getInstance(hardwareMap);
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
        return initComplete;
    }

    @Override
    public void performStateActions() {
        if (!frontRollerTouch.getState() && !backRollerTouch.getState()){
            autonOpMode.initEbotsImu();
            autonOpMode.setInitialHeadingDeg(0);
            initComplete = true;
        }
    }

    @Override
    public void performTransitionalActions() {
        long duration = 3000;
        double cycleTime = 500;
        StopWatch stopWatch = new StopWatch();
        EbotsBlinkin ebotsBlinkin = EbotsBlinkin.getInstance(hardwareMap);

        double currentCycle = Math.round(stopWatch.getElapsedTimeMillis() / cycleTime);
        boolean cycleOn = currentCycle % 2 == 0;
        while (!autonOpMode.isStarted() && !autonOpMode.isStopRequested() && stopWatch.getElapsedTimeMillis() < duration) {
            if(cycleOn) {
                ebotsBlinkin.lightsGreen();
            } else {
                ebotsBlinkin.lightsOff();
            }
            currentCycle = Math.round(stopWatch.getElapsedTimeMillis() / cycleTime);
            cycleOn = currentCycle % 2 == 0;
        }
        ebotsBlinkin.lightsOff();
}
}