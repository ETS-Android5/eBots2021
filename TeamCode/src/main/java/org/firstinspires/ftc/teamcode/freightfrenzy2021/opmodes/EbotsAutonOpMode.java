package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.UtilFuncs;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.autonroutines.EbotsAutonRoutine;
import org.firstinspires.ftc.teamcode.ultimategoal2020.StopWatch;

import java.util.ArrayList;

public abstract class EbotsAutonOpMode extends LinearOpMode {

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    protected BarCodePosition barCodePosition;

    protected Alliance alliance = Alliance.BLUE;

    protected StartingSide startingSide = StartingSide.CAROUSEL;

    protected EbotsImu ebotsImu;

    protected ArrayList<Class> itinerary = new ArrayList<>();

    protected double initialHeadingDeg = -90;  // gets revised in transition of StateConfigureRoutine
    protected double currentHeadingDeg;
    protected StopWatch stopWatchHeading = new StopWatch();


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public void setAlliance(Alliance alliance){
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public StartingSide getStartingSide() {
        return startingSide;
    }

    public double getInitialHeadingDeg() {
        return initialHeadingDeg;
    }

    /**
     * getCurrentHeadingDeg() is a time-buffered value cache for the hardware reading
     * if a hardware read is necessary, call updateHeading first
     * @return
     * @param forceHardwareRead
     */
    public double getCurrentHeadingDeg(boolean forceHardwareRead) {
        if (ebotsImu == null) return 0;

        long headingRefreshRateMillis = 500;
        boolean timeBufferExpired = stopWatchHeading.getElapsedTimeMillis() > headingRefreshRateMillis;

        if (timeBufferExpired | forceHardwareRead){
            updateHeading();
        }
        return currentHeadingDeg;
    }

    public void setStartingSide(StartingSide startingSide){
        this.startingSide = startingSide;
    }

    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }

    public void appendStatesToRoutineItinerary(EbotsAutonRoutine routine){
        this.itinerary.addAll(routine.getRoutineItinerary());
    }

    public void setInitialHeadingDeg(double initialHeadingDeg) {
        this.initialHeadingDeg = initialHeadingDeg;
    }

    public void setCurrentHeadingDeg(double currentHeadingDeg) {
        this.currentHeadingDeg = currentHeadingDeg;
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
