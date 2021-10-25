package com.example.testlib;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.xml.transform.SourceLocator;

public class MyClass {

    public enum TestEnum{
        VALUE1,
        VALUE2
    }


    public static void main(String[] args) {
        ArrayList<String> ebotsMembers = new ArrayList<>();
        ebotsMembers.add("Thomas");
        ebotsMembers.add("Sean");
        ebotsMembers.add("Michael");
        ebotsMembers.add("Carter");
        ebotsMembers.add("Arjun");
        ebotsMembers.add("Ishita");
        ebotsMembers.add("Maria");
        ebotsMembers.add("Ethan");
        ebotsMembers.add("Christian");
        ebotsMembers.add("Kenny");
        ebotsMembers.add("Lyla");
        ebotsMembers.add("Ryleigh M.");
        ebotsMembers.add("Elanur");
        ebotsMembers.add("Riley W.");
        ebotsMembers.add("Zachary");


        ArrayList<ArrayList<String>> teams = new ArrayList<>();
        for(int i=0; i < 4; i++){
            teams.add(new ArrayList<>());
        }

        Random rnd = ThreadLocalRandom.current();
        while (ebotsMembers.size() > 0){
            //Select target Team
            int targetTeamIndex = ebotsMembers.size() % teams.size();
            ArrayList<String> targetTeam = teams.get(targetTeamIndex);

            int randomIndex = rnd.nextInt(ebotsMembers.size());
            String currentMember = ebotsMembers.remove(randomIndex);

            targetTeam.add(currentMember);
        }

        for(ArrayList<String> t: teams){
            System.out.println(t);
        }


//        FunctionChaining();
//        typeStudyPolymorphism();
        }

        private static void typeStudyPolymorphism () {

            //  ***     Section 1 review and observe polymorphism with interfaces
            System.out.println("***  SECTION 1:  review and observe polymorphism with interfaces");
            System.out.println("______________________________________________");
            DistanceSensorFromInterface myDistanceSensor = new DistanceSensorFromInterface();
            System.out.println(myDistanceSensor.name + " -- Reading: " + myDistanceSensor.reading);
            System.out.println(myDistanceSensor.getClass());
            System.out.println(myDistanceSensor.getClass().getSimpleName());

            //  Perform some type checking of myDistanceSensor.
            //  It tests to belong to base class and implemented interfaces
            checkType(myDistanceSensor);

            System.out.println("______________________________________________");
            System.out.println("");
            //  ***     Section 2 explore casting
            System.out.println("***  SECTION 2:  explore casting");
            System.out.println("______________________________________________");

            //  Note:  methodInDistanceSensor is not in interface, only in DistanceSensorFromInterface
            myDistanceSensor.methodInDistanceSensor();
            System.out.println("myDistanceSensor is class: " + myDistanceSensor.getClass().getSimpleName());

            // You can change the way the COMPILER interprets the object by casting it
            SensorInterface myDistanceSensorCastToSensorInterface = (SensorInterface) myDistanceSensor;   //Case as SensorTrial
            myDistanceSensorCastToSensorInterface.flushReading();   // methodInDistanceSensor is not available
            System.out.println("Interface methods are available, but not DistanceSensorFromInterface methods");
            System.out.println();

            // But it doesn't actually change the type of the variable, it can be cast back if needed
            System.out.println("But when checking the class of DistanceSensorFromInterface, it hasn't really changed...");
            System.out.println("DistanceSensorFromInterface is class: " + myDistanceSensorCastToSensorInterface.getClass().getSimpleName());
            System.out.println();

            System.out.println("Recasting myDistanceSensorCastToSensorInterface to access DistanceSensorFromInterface class methods...");
            ((DistanceSensorFromInterface) myDistanceSensorCastToSensorInterface).methodInDistanceSensor();
            System.out.println();

            checkType(myDistanceSensorCastToSensorInterface);

            System.out.println("Conclusion:  Casting affects how the compiler views objects and how it directs available behavior");
            System.out.println("             But it doesn't actually change the type of the underlying object");
            System.out.println("______________________________________________");
            System.out.println("");


            //  ***     Section 3 explore casting further
            System.out.println("***  SECTION 3:  explore casting further");
            System.out.println("______________________________________________");

            //  Create a color sensor, which also implements the same interface
            ColorSensorFromInterface myColorSensor = new ColorSensorFromInterface();
            System.out.println(myColorSensor.name + " -- Reading: " + myColorSensor.reading);  // uses same reading method name
            checkType(myColorSensor);

            // Uncomment these next line to see the compiler errors
            // DistanceSensor myColorSensorCastToDistSensor = (DistanceSensor) myColorSensor;  // Can't cast a color sensor as a dist sensor

            SensorInterface myColorSensorCastToSensorInterface = (SensorInterface) myColorSensor;   //Case as SensorTrial

            // Casting can be dangerous because it can trick the compiler into a runtime error
            // Uncomment next line and run to see runtime error
            // ((DistanceSensorFromInterface) myColorSensorCastToSensorInterface).methodInDistanceSensor();  // This throws an error

            try {
                ((DistanceSensorFromInterface) myColorSensorCastToSensorInterface).methodInDistanceSensor();
            } catch (Exception e) {
                System.out.println("***   Error Catch   ***");
                System.out.println(e.getMessage());
                System.out.println(e.getClass());
                System.out.println("***   End Error Catch   ***");
                System.out.println();
            }

            System.out.println("Conclusion:  Casting is powerful, and with great power...");
            System.out.println("______________________________________________");
            System.out.println("");


            //  ***     Section 4 utilizing polymorphism
            System.out.println("***  SECTION 4:  utilizing polymorphism");
            System.out.println("______________________________________________");

            System.out.println();
            System.out.println("One advantage of using common interfaces is the ability to GENERALIZE");
            System.out.println("For instance, create a list of all robot sensors");

            //  Create an array list of type SensorInterface
            ArrayList<SensorInterface> sensors = new ArrayList<>();
            sensors.add(myDistanceSensor);
            sensors.add(myColorSensor);

            //  Loop through the array and check types
            int i = 0;
            for (SensorInterface s : sensors) {
                String typeString = ("Sensor " + i + " is type: " + s.getClass().getSimpleName());
                if (s instanceof DistanceSensorFromInterface) {
                    System.out.println(typeString + " named: " + ((DistanceSensorFromInterface) s).name);
                } else {
                    System.out.println(typeString + "Sensor not a distance sensor");
                }
                i++;
            }

            System.out.println();
            System.out.println("Sensors can't be combined in the list if they aren't the same type");
            ArrayList<DistanceSensorFromInterface> distanceSensors = new ArrayList<>();
            distanceSensors.add(myDistanceSensor);
            //distanceSensors.add(myColorSensor);     //  The compiler throws an error


            System.out.println();
            System.out.println("The combined list is handy to perform actions to all sensors");
            for (SensorInterface s : sensors) {
                s.reset();
            }
            System.out.println();


            //  ***     Section 5:  Generic typing for interfaces
            System.out.println("***  SECTION 5:  Generic typing for interfaces");
            System.out.println("______________________________________________");

            System.out.println("Interfaces can be further generalized to allow for multiple types");
            System.out.println("For instance, the readings for the sensors return different types...");
            System.out.println();
            for (SensorInterface s : sensors) {
                SensorReadingInterface r = (SensorReadingInterface) s;
                System.out.println("Reading for " + s.getClass().getSimpleName() + ": " + r.getReading() +
                        " <" + r.getClass().getSimpleName() + ">");
            }
            System.out.println();
            System.out.println("This is performed similarly to how parameters are passed into functions");
            System.out.println("Except type is passed into angled brackets <> instead of parenthesis ()");
            System.out.println("In fact, ArrayList uses this in the declaration ArrayList<SensorInterface>");

            System.out.println();
            System.out.println("In this code example, a second interface SensorReadingInterface<T> is implemented");
            System.out.println(" -- Within DistanceSensorFromInterface:  SensorReadingInterface <Double>");
            System.out.println(" -- Within ColorSensorFromInterface:  SensorReadingInterface <ColorSensorFromInterface.MyColors>");

            System.out.println();

            //  ***     Section 6:  Abstract classes
            System.out.println("***  SECTION 6:  Abstract classes");
            System.out.println("______________________________________________");
            System.out.println();
            System.out.println("One limitation of interfaces is they only introduce methods");
            System.out.println("Abstract classes are similar, but they allow for inclusion of class variables");
            System.out.println("They also allow for override methods of shared methods such as toString()");
            DistanceSensorFromAbC distanceSensorFromAbC = new DistanceSensorFromAbC();
            checkType(distanceSensorFromAbC);
            distanceSensorFromAbC.reset();
            System.out.println(distanceSensorFromAbC.toString());
            sensors.add(distanceSensorFromAbC);


            System.out.println();
            System.out.println("Similar to before, these can be added to a single list of type SensorAbstract...");
            System.out.println();
            ArrayList<SensorAbstract> sensorsAbC = new ArrayList<>();
            sensorsAbC.add(distanceSensorFromAbC);
            sensorsAbC.add(new ColorSensorFromAbC());

            for (SensorAbstract s : sensorsAbC) {
                System.out.println(s.toString());

                System.out.println("Attempting to run methodInDistanceSensorFromAbC for " + s.name);
                try {
                    ((DistanceSensorFromAbC) s).methodInDistanceSensorFromAbC();

                } catch (Exception e) {
                    System.out.println("Can't access methodInDistanceSensorFromAbC");
                }
                System.out.println();
            }


            //  Distance sensor can't be added to list distanceSensors because not type DistanceSensorFromInterface
            // distanceSensors.add(distanceSensorFromAbC);

        }

        private static void checkType (Object object){
            //  Perform some type checking of object.
            //  It tests to belong to base class and implemented interfaces

            System.out.println();
            System.out.println("***  TYPE CHECK  ***");

            if (object instanceof DistanceSensorFromInterface) {
                System.out.println("Sensor is instance of DistanceSensorFromInterface");
            } else {
                System.out.println("Sensor is NOT instance of DistanceSensorFromInterface");
            }

            if (object instanceof ColorSensorFromInterface) {
                System.out.println("Sensor is instance of ColorSensorFromInterface");
            } else {
                System.out.println("Sensor is NOT instance of ColorSensorFromInterface");
            }


            if (object instanceof SensorInterface) {
                System.out.println("Sensor is instance of SensorInterface");
            } else {
                System.out.println("Sensor is NOT instance of SensorInterface");
            }
            if (object instanceof SensorReadingInterface) {
                System.out.println("Sensor is instance of SensorReadingTrial");
            } else {
                System.out.println("Sensor is NOT instance of SensorReadingInterface");
            }
            if (object instanceof SensorAbstract) {
                System.out.println("Sensor is instance of SensorAbstract");
            } else {
                System.out.println("Sensor is NOT instance of SensorAbstract");
            }
            if (object instanceof Object) {
                System.out.println("Sensor is instance of Object");
            } else {
                System.out.println("Sensor is NOT instance of Object");
            }
            System.out.println("***  END TYPE CHECK  ***");
            System.out.println();
        }


        public static void FunctionChaining () {
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