package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.AllianceSingleton;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.EbotsAutonRoutine;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public abstract class EbotsAutonOpMode extends LinearOpMode {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    protected BarCodePosition barCodePosition;

    // AllianceSingleton is managed so it can be passed to Teleop for FieldOrientedDrive

    protected StartingSide startingSide = StartingSide.CAROUSEL;

    // EbotsImu is managed as a Singleton so it can be passed to Teleop for FieldOrientedDrive
    protected EbotsImu ebotsImu;

    protected ArrayList<Class> itinerary = new ArrayList<>();

    protected double initialHeadingDeg = -90;  // gets revised in transition of StateConfigureRoutine
    protected double currentHeadingDeg;
    protected StopWatch stopWatchHeading = new StopWatch();


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
        return ebotsImu.getCurrentFieldHeadingDeg(forceHardwareRead);
    }

    public void setStartingSide(StartingSide startingSide){
        this.startingSide = startingSide;
    }

    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }

    public void setInitialHeadingDeg(double initialHeadingDeg) {
        // initial heading is managed by the imu
        if (ebotsImu == null) ebotsImu = EbotsImu.getInstance(hardwareMap, true);
        this.ebotsImu.setFieldHeadingWhenInitializedDeg(initialHeadingDeg);
    }

    public void appendStatesToRoutineItinerary(EbotsAutonRoutine routine){
        this.itinerary.addAll(routine.getRoutineItinerary());
    }


    public void initEbotsImu(){
        ebotsImu = EbotsImu.getInstance(hardwareMap, true);
    }

    public void updateHeading(){
        if (ebotsImu == null) {
            currentHeadingDeg = 0;
        } else {
            double imuReading = ebotsImu.performHardwareRead();
            currentHeadingDeg = UtilFuncs.applyAngleBounds(imuReading + initialHeadingDeg);
            stopWatchHeading.reset();
        }
    }



}
