package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsWebcam;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDrive;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.EbotsAutonRoutine;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.navigators.NavigatorVuforia;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public abstract class EbotsAutonOpMode extends LinearOpMode {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    protected BarCodePosition barCodePosition;

    // AllianceSingleton is managed so it can be passed to Teleop for FieldOrientedDrive
    protected EbotsWebcam frontWebcam;


    protected StartingSide startingSide = StartingSide.CAROUSEL;

    protected ArrayList<Class> itinerary = new ArrayList<>();

    // motion controller
    protected AutonDrive motionController;

    protected Pose currentPose;

    protected NavigatorVuforia navigatorVuforia;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Sets the AllianceSingleton to desired Alliance color
     * @param alliance enum value of alliance color
     */
    public void setAlliance(Alliance alliance){
        AllianceSingleton.setAlliance(alliance);
    }

    public Alliance getAlliance() {
        return AllianceSingleton.getAlliance();
    }

    public StartingSide getStartingSide() {
        return startingSide;
    }

    public double getCurrentHeadingDeg(boolean forceHardwareRead){
        return EbotsImu.getCurrentFieldHeadingDeg(forceHardwareRead);
    }

    public Pose getCurrentPose() {
        if (currentPose==null) currentPose = new Pose();    // null protect the getter
        return currentPose;
    }


    public EbotsWebcam getFrontWebcam() {
        return frontWebcam;
    }

    public AutonDrive getMotionController() {
        return motionController;
    }

    public NavigatorVuforia getNavigatorVuforia() {
        return navigatorVuforia;
    }


    public void setStartingSide(StartingSide startingSide){
        this.startingSide = startingSide;
    }

    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }

    public void setInitialHeadingDeg(double initialHeadingDeg) {
        // initial heading is managed by the imu
        EbotsImu.setFieldHeadingWhenInitializedDeg(initialHeadingDeg);
    }

    public void setCurrentPose(Pose currentPose) {
        this.currentPose = currentPose;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public abstract void initAutonOpMode();

    public void appendStatesToRoutineItinerary(EbotsAutonRoutine routine){
        this.itinerary.addAll(routine.getRoutineItinerary());
    }

    public void initEbotsImu(){
        EbotsImu.getInstance(hardwareMap, true);
    }




}
