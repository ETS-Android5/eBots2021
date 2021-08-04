package com.example.testlib.factorytrial;

public abstract class AbstractAutonState implements AutonStateFactory{

    protected String name = "From Abstract";
    protected Class nextState;
    public static GetStateConstructor<String, Class<? extends AbstractAutonState>, AbstractAutonState> myLambdaConstructor;

    public AbstractAutonState(){

    }
    public AbstractAutonState(String inName, Class<? extends AbstractAutonState> nextState){
        this.name = inName;
        this.nextState = nextState;
    }

    public static void testMethod(){
        System.out.println("Test method invoked from " + AbstractAutonState.class.getSimpleName());
    }

    public static String testReturnMethod(){
        return "This is the return from testReturnMethod";
    }

    public static GetStateConstructor<String, Class<? extends AbstractAutonState>, AbstractAutonState> getMyLambdaConstructor(){
        return myLambdaConstructor;
    }

    public static AbstractAutonState getIt(String inputName, Class<? extends AbstractAutonState> nextStateIn){
        return null;
    };
}
