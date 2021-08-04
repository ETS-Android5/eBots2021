package com.example.testlib.factorytrial;

public interface AutonStateInt<T> {
    /**
     * AutonState is an interface defining common methods for autonomous states
     * It is used along with AutonStateFactory to generate instances of AutonStates
     * The enumeration AutonStateEnum controls the available options
     */

    public T getInstance();


}
