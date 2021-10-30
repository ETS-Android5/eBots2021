package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;
import org.firstinspires.ftc.teamcode.ebotsenums.StartingSide;
import org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.AutonStates.StateConfigureRoutine;

public abstract class EbotsAutonOpMode extends LinearOpMode {

    private BarCodePosition barCodePosition;
    private Alliance allianceColor;
    private StartingSide startingSide;
    public void setAllianceColor(Alliance color){
        this.allianceColor = color;
    }
    public Alliance getAllianceColor() {
        return allianceColor;
    }
    public StartingSide getStartingSide() {
        return startingSide;
    }
    public void setStartingSide(StartingSide startingSide){
        this.startingSide = startingSide;
    }
    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }

    public StartingSide getStartingSide() {
        return startingSide;
    }
}
