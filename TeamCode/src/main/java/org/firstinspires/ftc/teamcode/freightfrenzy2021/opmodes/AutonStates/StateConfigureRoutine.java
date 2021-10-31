package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.EbotsAutonOpMode;
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
    Alliance alliance;
    StartingSide startingSide;
    private Telemetry telemetry;
    private DigitalChannel allianceTouchSensor;
    private DigitalChannel startingSideTouchSensor;
    private EbotsAutonOpMode opMode;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public StateConfigureRoutine(HardwareMap hardwareMap, EbotsAutonOpMode opMode){
        allianceTouchSensor = hardwareMap.get(DigitalChannel.class, "allianceTouchSensor");
        startingSideTouchSensor = hardwareMap.get(DigitalChannel.class, "startingSideTouchSensor");
        alliance = Alliance.BLUE;
        startingSide = StartingSide.CAROUSEL;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
        stopWatch = new StopWatch();
        touchSensorTimer  = 500;
        startingSideTouchSensor.setMode(DigitalChannel.Mode.INPUT);
        allianceTouchSensor.setMode(DigitalChannel.Mode.INPUT);



    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }
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
        return opMode.isStarted() | shouldExit;
    }
// if blue set heading to -heading
    @Override
    public void performStateActions() {
        if (!allianceTouchSensor.getState() && stopWatch.getElapsedTimeMillis() >= touchSensorTimer) {
            stopWatch.reset();
            if (this.alliance == Alliance.BLUE) {
                alliance = Alliance.RED;
            } else {
                alliance = Alliance.BLUE;
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
        telemetry.addData("alliance color: ", alliance);
        telemetry.addData("starting side: ", startingSide);
        telemetry.update();
    }

    @Override
    public void performTransitionalActions() {
        opMode.setAlliance(this.alliance);
        opMode.setStartingSide(this.startingSide);


    }
}
