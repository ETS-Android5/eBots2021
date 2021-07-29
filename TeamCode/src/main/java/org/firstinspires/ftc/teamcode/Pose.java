package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.CsysDirection;
import org.firstinspires.ftc.teamcode.fieldobjects.LaunchLine;
import org.firstinspires.ftc.teamcode.fieldobjects.PlayField;
import org.firstinspires.ftc.teamcode.fieldobjects.StartLine;
import org.firstinspires.ftc.teamcode.fieldobjects.TowerGoal;

import java.util.Formatter;

import static java.lang.String.format;

/**
 * Pose represents the robots position AND heading relative to field coordinates
 * It can also carry instructions on what to do after an auton travel leg
 */
public class Pose {

    /****************************************************************
    //******    CLASS VARIABLES
    //***************************************************************/

    private double headingDeg;         // Degrees, 0 inline with x-axis, positive CCW when viewed from top
    private double newHeadingReadingDeg;   //New incoming reading for heading
    private FieldPosition fieldPosition;    // x, y and z position on field in inches.  0 is center of field.  x+ towards target goals.  y+ towards blue alliance side.
    /***************************************************************
    //******    STATIC VARIABLES
    //****************************************************************/


    /***************************************************************
    //******    ENUMERATIONS
    //***************************************************************/
    public enum PresetPose {
        //These starting poses assume BLUE Alliance
        INNER_START_LINE(calculateXCoord(), calculateYCoord(StartLine.LinePosition.INNER), 0.0)
        , OUTER_START_LINE(calculateXCoord(), calculateYCoord(StartLine.LinePosition.OUTER), 0.0)
        , LAUNCH_TARGET_GOAL(new LaunchLine().getX(), new TowerGoal().getY(), 0.0)
        , LAUNCH_POWER_SHOT(new LaunchLine().getX(), new TowerGoal().getY(), 0.0);

        private double xStart;
        private double yStart;
        private double headingStart;

        PresetPose(double xInput, double yInput, double headingInput){
            this.xStart = xInput;
            this.yStart = yInput;
            this.headingStart = headingInput;
        }

        public double getXStart() {
            return xStart;
        }
        public double getYStart() {
            return yStart;
        }
        public double getHeadingStart() {
            return headingStart;
        }

        private static double calculateXCoord(){
            //Start on the bottom wall, heading = 0
            double wallX = -new PlayField().getFieldHeight()/2;
            double robotX = EbotsRobot.RobotSize.xSize.getSizeValue()/2;
            return (wallX + robotX);
        }

        private static double calculateYCoord(StartLine.LinePosition linePosition){
            //Assumed blue alliance, robot heading 0, right wheels on start line
            double startLineY = linePosition.getyCenter();
            double colorSensorYInset = 2.5;
            double robotY = (EbotsRobot.RobotSize.ySize.getSizeValue()/2 - colorSensorYInset);
            return (startLineY + robotY);
        }

    }

    //***************************************************************88

    /*****************************************************************
    //******    CONSTRUCTORS
    //****************************************************************/

    public Pose(){
        //this.x = 0.0;
        //this.y = 0.0;
        this.fieldPosition = new FieldPosition();
        this.headingDeg = 0.0;
        this.newHeadingReadingDeg = headingDeg;
    }     //Default constructor

    public Pose(double xInput, double yInput, double headingInputDeg){
        this.fieldPosition = new FieldPosition(xInput,yInput);
        //this.x = xInput;
        //this.y = yInput;
        this.headingDeg = applyAngleBound(headingInputDeg);
        this.newHeadingReadingDeg = headingDeg;

    }

    public Pose(FieldPosition fp, double headingInputDeg){
        this.fieldPosition = fp;
        this.headingDeg = applyAngleBound(headingInputDeg);
        this.newHeadingReadingDeg = headingDeg;

    }

    //  When using a pre-defined StartingPose from the enumeration
    public Pose(PresetPose presetPose, Alliance alliance) {
        this(presetPose.getXStart(), presetPose.getYStart(), presetPose.getHeadingStart());

        //Now flip the sign of the y component if on the red alliance
        if(alliance == Alliance.RED){
            this.fieldPosition.setyPosition(-this.fieldPosition.getyPosition());
        }
    }

    // When using a StartLine.LinePosition
    public Pose(StartLine.LinePosition linePosition, Alliance alliance){
        double xPosition = -new PlayField().getFieldHeight()/2 + EbotsRobot.RobotSize.xSize.getSizeValue()/2;
        //Assumed blue alliance, robot heading 0, right wheels on start line
        double yPosition = linePosition.getyCenter() + EbotsRobot.RobotSize.ySize.getSizeValue()/2;
        this.fieldPosition = new FieldPosition(xPosition, yPosition);
        this.headingDeg = 0;
        this.newHeadingReadingDeg = headingDeg;

        //Now flip the sign of the y component if on the red alliance
        if(alliance == Alliance.RED){
            this.fieldPosition.setyPosition(-this.fieldPosition.getyPosition());
        }

    }

    /*****************************************************************
    //******    SIMPLE GETTERS AND SETTERS
    //****************************************************************/
    public double getX() { return this.fieldPosition.getPositionComponent(CsysDirection.X);}
    public double getY() { return this.fieldPosition.getPositionComponent(CsysDirection.Y); }
    public double getZ() { return this.fieldPosition.getPositionComponent(CsysDirection.Z); }

    public FieldPosition getFieldPosition(){return this.fieldPosition;}
    public double getCoordinate(CsysDirection dir){
        double coordinateValue = 0;
        if(dir == CsysDirection.Heading){
            coordinateValue = headingDeg;
        } else{
            coordinateValue = this.fieldPosition.getPositionComponent(dir);
        }
        return coordinateValue;
    }

    public double getHeadingDeg() { return headingDeg;}
    public double getHeadingRad(){ return Math.toRadians(headingDeg); }
    public double getNewHeadingReadingDeg(){return this.newHeadingReadingDeg;}
    public double getNewHeadingReadingRad(){return Math.toRadians(this.newHeadingReadingDeg);}

    public void setX(double x) {
        this.fieldPosition.setxPosition(x);
    }

    public void setY(double y) {
        this.fieldPosition.setyPosition(y);
    }

    public void setHeadingDeg(double headingDeg) {
        /** Sets heading, but makes sure it is within the legal bounds
         *  which is -180 < heading <= 180
         */
        headingDeg = applyAngleBound(headingDeg);
        this.headingDeg = headingDeg;
    }

    public void setNewHeadingReadingDeg(double headingReadingDeg){
        headingReadingDeg = applyAngleBound(headingReadingDeg);
        this.newHeadingReadingDeg = headingReadingDeg;
    }


    /***************************************************************88
    //******    Class METHODS
    //***************************************************************/

    public void updateHeadingWithReading(){
        this.headingDeg = this.newHeadingReadingDeg;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        //Loop through the coordinates
        boolean firstPass = true;
        for(CsysDirection dir: CsysDirection.values()){
            if(firstPass) sb.append("Pose: (");
            else if (dir == CsysDirection.Heading) sb.append(") @ ");
            else sb.append(", ");

            Formatter fmt = new Formatter(sb);
            fmt.format("%.2f",this.getCoordinate(dir));
            if (dir == CsysDirection.Heading) sb.append("°");
            firstPass = false;
        }

        return sb.toString();

        //return "(" + String.format("%.2f",fieldPosition.getxPosition()) + " ," + String.format("%.2f",fieldPosition.getyPosition()) + " @ "
        //        + String.format("%.2f", headingDeg) + ")";
    }

    /***************************************************************88
     //******    Static METHODS
     //***************************************************************/
    public static double applyAngleBound (double inputAngle){
        while (inputAngle > 180){
            inputAngle -= 360;
        }
        while (inputAngle <= -180){
            inputAngle += 360;
        }
        return inputAngle;
    }
}
