package com.example.testlib.factorytrial;

public class SecondStateFromAbC extends AbstractAutonState {

    public SecondStateFromAbC(){
        name = "Second State";
    }


    public SecondStateFromAbC(String inName, Class<? extends AbstractAutonState> nextState) {
        super(inName, nextState);
        myLambdaConstructor = (nameIn, nextStateIn)->new SecondStateFromAbC(nameIn, nextStateIn);

    }

    @Override
    public AbstractAutonState getInstance(String name, Class nextState) {
        return new SecondStateFromAbC(name, nextState);
    }

    public static GetStateConstructor<String, Class, AbstractAutonState> myConsFunc = (nameIn, nextStateIn)->new SecondStateFromAbC(nameIn, nextStateIn);

}
