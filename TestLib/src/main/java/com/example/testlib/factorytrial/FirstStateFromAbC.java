package com.example.testlib.factorytrial;

import java.util.function.Function;

public class FirstStateFromAbC extends AbstractAutonState{

    public FirstStateFromAbC(){

    }
    public FirstStateFromAbC(String inName, Class<? extends AbstractAutonState> nextState){
        super(inName, nextState);
        myLambdaConstructor = (nameIn, nextStateIn)->new FirstStateFromAbC(nameIn, nextStateIn);
    }


    @Override
    public AbstractAutonState getInstance(String name, Class nextState) {
        return new FirstStateFromAbC(name, nextState);
    }

    public static Function<String, String> getFunction(){
        return s->"Hello::" + s;
    }

    public static GetStateConstructor<String, Class<? extends AbstractAutonState>, AbstractAutonState> myConsFunc = (nameIn, nextStateIn)->new FirstStateFromAbC(nameIn, nextStateIn);

    public static AbstractAutonState getIt(String inputName, Class<? extends AbstractAutonState> nextStateIn){
        return new FirstStateFromAbC(inputName, nextStateIn);
    }
}
