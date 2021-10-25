package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ebotsenums.BarCodePosition;

public abstract class EbotsAutonOpMode extends LinearOpMode {

    private BarCodePosition barCodePosition;

    public void setBarCodePosition(BarCodePosition barCodePosition) {
        this.barCodePosition = barCodePosition;
    }
}
