package com.example.testlib;

import java.lang.reflect.Type;

public class DistanceSensorFromAbC extends SensorAbstract<Double> {

    public Type readingClass = Double.TYPE;

    public DistanceSensorFromAbC(){
        name = "AbC Distance Sensor";
        reading = 4.3;
    }


    public void methodInDistanceSensorFromAbC(){
        System.out.println("This method is not shared with any interface or inherited (abstract) class");
    }
}
