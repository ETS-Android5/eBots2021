package com.example.testlib;

import java.lang.reflect.Type;

public class DistanceSensor implements SensorReadingTrial<Double> {

    public String name;
    public Double reading;
    public Type readingClass = Double.TYPE;

    public DistanceSensor(){
        name = "newSensor";
        reading = 4.3;
    }

    @Override
    public Double getReadingGen() {
        return  this.reading;
    }
}
