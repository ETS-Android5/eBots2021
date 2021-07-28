package com.example.testlib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyClass {

    public static void main(String[] args){
        System.out.println("Hello World");

//        FunctionChaining();

        DistanceSensor mySensor = new DistanceSensor();
        System.out.println(mySensor.name + " -- Reading: " + mySensor.reading);
        System.out.println(mySensor.getClass());
//        if(mySensor.getClass() == Sensor.class)
        if (mySensor instanceof DistanceSensor){
            System.out.println("They are the same class");
        }

        System.out.println("Reading Type: " + mySensor.reading.getClass());

        ColorSensorTrial myColorSensor = new ColorSensorTrial();
        System.out.println(myColorSensor.name + " -- Reading: " + myColorSensor.reading);
        System.out.println(myColorSensor.getClass());
//        if(mySensor.getClass() == Sensor.class)
        if (myColorSensor instanceof ColorSensorTrial){
            System.out.println("They are the same class");
        }

        System.out.println("Reading Type: " + myColorSensor.reading.getClass());




    }


    public static void FunctionChaining(){
        ArrayList<String> myList = new ArrayList<>();
        myList.add("djojo");
        myList.add("ckrimber");
        myList.add("amomo");
        myList.add("sow");

        System.out.println("Original List: " + myList);
        List<String> newList = myList.stream()
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("New List after stream:  " + newList);
        System.out.println("Original List after stream:  " + myList);

    }

}