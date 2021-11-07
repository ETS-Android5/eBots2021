package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.navigators;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.ebotssensors.EbotsImu;
import org.firstinspires.ftc.teamcode.ebotsutil.Pose;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.motioncontrollers.AutonDrive;

public class NavigatorDeadReckoning implements EbotsNavigator{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Class Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Attributes
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private Pose startingPose;
    private EbotsImu imu;
    private AutonDrive motionController;
    private long loopDuration;
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Constructors
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public NavigatorDeadReckoning(Pose startingPose, EbotsImu imu, AutonDrive motionController, long loopDuration){
        this.startingPose = startingPose;
        this.imu = imu;
        this.motionController = motionController;
        this.loopDuration = loopDuration;
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
    public @Nullable Pose getPoseEstimate() {
        // what direction was the robot driving
        double translateHeading = getTranslateHeading();

        // how fast was the robot moving (in/s)
        double driveMagnitude = motionController.getTranslateMagnitude();
        double rateInPerSec = driveMagnitude * motionController.getSpeed().getMeasuredTranslateSpeed();

        // how far did it travel in each direction
        double distanceTraveled = rateInPerSec * loopDuration;
        double xDist = Math.cos(Math.toRadians(translateHeading)) * distanceTraveled;
        double yDist = Math.sin(Math.toRadians(translateHeading)) * distanceTraveled;

        // apply translation to starting pose
        double newX = startingPose.getX() + xDist;
        double newY = startingPose.getY() + yDist;
        Pose endPose = new Pose(newX, newY, EbotsImu.getCurrentFieldHeadingDeg(false));

        return endPose;
    }

    private double getTranslateHeading(){
        // what was the heading at the start of the move (from startingPose)
        double startHeading = startingPose.getHeadingDeg();
        // what is the current heading (from imu)
        imu.performHardwareRead();
        double endHeading = EbotsImu.getCurrentFieldHeadingDeg(false);
        // what direction was the robot driving
        // must be careful of rollover between -180 and 180
        // to protect, if the difference between end and start is >180 in magnitude
        // then add 360 * sign(difference) to the start angle
        // so if start angle is 177, end angle is -179, diff = -356, so subtract 180 from start angle
        // and if start angle -179, end angle 179, diff is 358, add 180 to start
        double difference = endHeading - startHeading;
        if (Math.abs(difference) > 180){
            startHeading += (360 * Math.signum(difference));
        }
        double translateHeading = (startHeading + endHeading) / 2;
        return translateHeading;
    }

}
