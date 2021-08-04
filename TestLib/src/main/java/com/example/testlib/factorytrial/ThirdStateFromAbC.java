package com.example.testlib.factorytrial;

public class ThirdStateFromAbC extends AbstractAutonState implements AutonStateFactory{

    public ThirdStateFromAbC(){
        name = "Third State";
    }

    public ThirdStateFromAbC(String inName, Class<? extends AbstractAutonState> nextState) {
        super(inName, nextState);
    }


    public AbstractAutonState getInstance(String name, Class nextState) {
        return new ThirdStateFromAbC(name, nextState);
    }

}
