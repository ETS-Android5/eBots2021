package com.example.testlib;

public class ColorSensorFromInterface implements SensorInterface, SensorReadingInterface<ColorSensorFromInterface.MyColors> {

    public String name;
    public MyColors reading;

    enum MyColors{
        BLUE,
        RED
    }

    public ColorSensorFromInterface(){
        name = "Color Sensor";
        reading = MyColors.BLUE;
    }

    @Override
    public MyColors getReading() {
        return this.reading;
    }

    @Override
    public void flushReading() {
        System.out.println("Flushing Reading for ColorSensor");
    }

    @Override
    public void reset() {
        System.out.println("Resetting sensor for Color Sensor");
    }

//    @Override
//    public String getTypeName() {
//        return "ColorSensorTrial";
//    }
}
