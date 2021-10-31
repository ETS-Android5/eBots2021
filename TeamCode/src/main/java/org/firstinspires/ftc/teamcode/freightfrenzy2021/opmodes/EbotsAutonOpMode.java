package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
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

    protected BNO055IMU imu;

    protected ArrayList<Class> itinerary = new ArrayList<>();

    protected double initialHeadingDeg = -90;
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

    public BNO055IMU getImu() {
        return imu;
    }

    public double getCurrentHeadingDeg() {
        if (imu == null) return 0;

        long headingRefreshRateMillis = 500;
        if (stopWatchHeading.getElapsedTimeMillis() > headingRefreshRateMillis){
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

    public void appendStatesToRoutine(EbotsAutonRoutine routine){
        this.itinerary.addAll(routine.getRoutine());
    }

    public void setInitialHeadingDeg(double initialHeadingDeg) {
        this.initialHeadingDeg = initialHeadingDeg;
    }

    public void setCurrentHeadingDeg(double currentHeadingDeg) {
        this.currentHeadingDeg = currentHeadingDeg;
    }

    public void initImu(){
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        updateHeading();
    }

    private void updateHeading(){
        double imuReading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        currentHeadingDeg = imuReading + initialHeadingDeg;
        stopWatchHeading.reset();
    }



}
