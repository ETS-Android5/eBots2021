package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.EbotsAutonRoutine;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.RoutineCarousel;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.RoutineWarehouse;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

public class StateConfigureRoutine implements EbotsAutonState{

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    StopWatch stopWatch;
    long touchSensorTimer;
    StartingSide startingSide;
    private Telemetry telemetry;
    private DigitalChannel allianceTouchSensor;
    private DigitalChannel startingSideTouchSensor;
    private EbotsAutonOpMode opMode;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateConfigureRoutine(EbotsAutonOpMode opMode){
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
        HardwareMap hardwareMap = opMode.hardwareMap;
        startingSide = opMode.getStartingSide();

        allianceTouchSensor = hardwareMap.get(DigitalChannel.class, "allianceTouchSensor");
        startingSideTouchSensor = hardwareMap.get(DigitalChannel.class, "startingSideTouchSensor");
        startingSideTouchSensor.setMode(DigitalChannel.Mode.INPUT);
        allianceTouchSensor.setMode(DigitalChannel.Mode.INPUT);

        stopWatch = new StopWatch();
        touchSensorTimer  = 500;
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
        boolean shouldExit = false;
        if (opMode.gamepad1.left_bumper && opMode.gamepad1.right_bumper){
           shouldExit = true;
        }
        return opMode.isStarted() | shouldExit | opMode.isStopRequested();
    }
// if blue set heading to -heading
    @Override
    public void performStateActions() {
        if (!allianceTouchSensor.getState() && stopWatch.getElapsedTimeMillis() >= touchSensorTimer) {
            stopWatch.reset();
            if (AllianceSingleton.getAlliance() == Alliance.BLUE) {
                AllianceSingleton.setAlliance(Alliance.RED);
            } else {
                AllianceSingleton.setAlliance(Alliance.BLUE);
            }
        }
        if (!startingSideTouchSensor.getState() && stopWatch.getElapsedTimeMillis() >= touchSensorTimer) {
            stopWatch.reset();
            if (this.startingSide == StartingSide.CAROUSEL) {
                startingSide = StartingSide.WAREHOUSE;
            } else {
                startingSide = StartingSide.CAROUSEL;
            }
        }
        telemetry.addData("alliance color: ", AllianceSingleton.getAlliance());
        telemetry.addData("starting side: ", startingSide);
    }

    @Override
    public void performTransitionalActions() {
        telemetry.addData("Transitioning out of", this.getClass().getSimpleName());
        telemetry.update();
        opMode.setStartingSide(this.startingSide);
        double initialHeadingDeg = (AllianceSingleton.getAlliance() == Alliance.BLUE) ? -90 : 90;
        opMode.initEbotsImu();
        opMode.setInitialHeadingDeg(initialHeadingDeg);

        // Note that IMU and Alliance must be set prior to initializing pose
        Pose startingPose = new Pose(AllianceSingleton.getAlliance(), startingSide);
        opMode.setCurrentPose(startingPose);

        EbotsAutonRoutine routine = (startingSide == StartingSide.CAROUSEL) ?
                new RoutineCarousel() : new RoutineWarehouse();
        opMode.appendStatesToRoutineItinerary(routine);
    }
}
