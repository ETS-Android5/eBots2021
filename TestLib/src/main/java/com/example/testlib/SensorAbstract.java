package com.example.testlib;

import java.lang.reflect.Type;

public abstract class SensorAbstract<T> implements SensorInterface, SensorReadingInterface<T>{

    protected String name;
    protected T reading;

    @Override
    public void flushReading() {
        System.out.println("Flushing Reading for: " + this.getClass().getSimpleName());
    }

    @Override
    public void reset() {
        System.out.println("Resetting sensor for: "  + this.getClass().getSimpleName());

    }

    @Override
    public T getReading() {
        return this.reading;
    }

    @Override
    public String toString() {
        return this.name + "With Reading: " + this.getReading() +
                " <" + this.getReading().getClass().getSimpleName() + ">" ;
    }
}
