package org.firstinspires.ftc.teamcode.ebotsenums;

public enum RobotSize {
    xSize(CsysDirection.X, 18.0),
    ySize(CsysDirection.Y, 18.0),
    zSize(CsysDirection.Z, 18.0);

    CsysDirection csysDirection;
    double sizeValue;

    RobotSize(CsysDirection csysDirectionIn, double sizeValueIn){
        this.csysDirection = csysDirectionIn;
        this.sizeValue = sizeValueIn;
    }

    public CsysDirection getCsysDirection() {
        return csysDirection;
    }

    public double getSizeValue() {
        return sizeValue;
    }
}
