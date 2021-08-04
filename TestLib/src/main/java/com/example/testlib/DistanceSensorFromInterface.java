package com.example.testlib;

import java.lang.reflect.Type;

public class DistanceSensorFromInterface implements SensorInterface, SensorReadingInterface<Double> {

    public String name;
    public Double reading;
    public Type readingClass = Double.TYPE;

    public DistanceSensorFromInterface(){
        name = "newSensor";
        reading = 4.3;
    }

    //  Overridden methods from interfaces
    @Override
    public Double getReading() {
        return  this.reading;
    }

    @Override
    public void flushReading() {
        System.out.println("Flushing Reading for Distance Sensor");
    }

    @Override
    public void reset() {
        System.out.println("Resetting sensor for Distance Sensor");
    }

//    @Override
//    public String getTypeName() {
//        return "DistanceSensor";
//    }

    @Override
    public String toString() {
        return (this.name + " -- Reading: " + this.reading + " with constant: " + SensorInterface.MY_CONSTANT);
    }

    public void methodInDistanceSensor(){
        System.out.println("DistanceSensorFromInterface::methodInDistanceSensor --> " +
                "This method is not shared with any interface or inherited class");
    }
}
