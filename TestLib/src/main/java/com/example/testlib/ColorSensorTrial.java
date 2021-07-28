package com.example.testlib;

public class ColorSensorTrial implements SensorReadingTrial<ColorSensorTrial.MyColors> {

    public String name;
    public MyColors reading;

    enum MyColors{
        BLUE,
        RED
    }

    public ColorSensorTrial(){
        name = "Color Sensor";
        reading = MyColors.BLUE;
    }

    @Override
    public MyColors getReadingGen() {
        return this.reading;
    }
}
