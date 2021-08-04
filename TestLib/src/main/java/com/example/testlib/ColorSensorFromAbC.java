package com.example.testlib;

public class ColorSensorFromAbC extends SensorAbstract<ColorSensorFromAbC.MyColorsAbC> {

    public ColorSensorFromAbC(){
        name = "Color Sensor Abstract";
        reading = MyColorsAbC.PINK;
    }

    enum MyColorsAbC{
        PINK,
        ORANGE
    }


}
