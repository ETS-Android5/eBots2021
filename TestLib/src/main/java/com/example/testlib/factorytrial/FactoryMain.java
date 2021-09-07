package com.example.testlib.factorytrial;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class FactoryMain{

    static int performTimesTen(TimesTen op, int inputInt){
        return op.doIt(inputInt);
    }

    public static void main(String[] args){

//        AutonStateInt firstState = FirstStateFromInt.getInstance();
//        HashMap<Integer, Integer> testHash = new HashMap<>();
//
//        testHash.put(1,1);
//        testHash.put(2,20);
//
//        System.out.println(testHash.get(1));
//        System.out.println(testHash.get(2));
//
//        HashMap<String, Class<? extends AbstractAutonState>> handlerMap = new HashMap<>();
//
//        TimesTen timesTen = i -> i*10;
//        TimesTen timesHundred = i -> i*100;
//
//        int inputValue = testHash.get(2);
//        int result = performTimesTen(timesTen, inputValue);
//        System.out.println(result);
//
//
//        HashMap<Integer, TimesTen> funcMap = new HashMap<>();
//        funcMap.put(1, i->i*10);
//        funcMap.put(2, i->i*100);
//
//        TimesTen myFunc = funcMap.get(2);
//        int funcResult = myFunc.doIt(25);
//        System.out.println(funcResult);
//
//        HashMap<String, StateInstanceFactory> factoryHashMap = new HashMap<>();
//        factoryHashMap.put("FirstStateFromAbC", FirstStateFromAbC::new);
//        factoryHashMap.put("SecondStateFromAbC", SecondStateFromAbC::new);
//
//        StateInstanceFactory stateInstanceFactory = factoryHashMap.get("FirstStateFromAbC");
//        AbstractAutonState myState = stateInstanceFactory.getInstance();
//
//        System.out.println(myState.name);
//
//        StateInstanceFactory secondStateFactory = factoryHashMap.get("SecondStateFromAbC");
//        AbstractAutonState secondState = secondStateFactory.getInstance();
//
//        System.out.println(secondState.name);
//
//        String myName = "mono";
//        HashMap<Class, StateInstanceFactory> classHashMap = new HashMap<>();
//        classHashMap.put(FirstStateFromAbC.class, ()->new FirstStateFromAbC(myName));
//        classHashMap.put(SecondStateFromAbC.class, ()-> new SecondStateFromAbC(myName + SecondStateFromAbC.class.getSimpleName()));
//
//        StateInstanceFactory s1f = classHashMap.get(FirstStateFromAbC.class);
//        AbstractAutonState s1 = s1f.getInstance();
//        System.out.println(s1.name);
//
//        AbstractAutonState s2 = classHashMap.get(SecondStateFromAbC.class).getInstance();
//        System.out.println(s2.name);
//
//        System.out.println();
//        System.out.println("Now using the handler class...");
//        System.out.println();
//
        FactoryService factoryService = new FactoryService();
//        AbstractAutonState firState = factoryService.get(FirstStateFromAbC.class, "Did it work?");
//        System.out.println(firState.name);
//
//        AbstractAutonState secState = factoryService.get(SecondStateFromAbC.class, "I think it did");
//        System.out.println(secState.name);
//        System.out.println();
//
//
//        //  Now try to use class as a variable
//        Class myClass = FirstStateFromAbC.class;
//        System.out.println(myClass.getSimpleName());
//
//        try {
//            myClass.getMethod("testMethod").invoke(null);
//            String returnFromMethod = (String) myClass.getMethod("testReturnMethod").invoke(null);
//            System.out.println(returnFromMethod);
//            FirstStateFromAbC stateInvokedConstruct = (FirstStateFromAbC) myClass.getConstructor(String.class).newInstance("jojo");
//            System.out.println(stateInvokedConstruct.name);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println();


        System.out.println("Now trying using embedded interface");

        AbstractAutonState firstStateFromAbC = factoryService.get(FirstStateFromAbC.class,"firstState", SecondStateFromAbC.class);
        System.out.println(firstStateFromAbC.name);

        AbstractAutonState secondStateFromAbC = factoryService.get(SecondStateFromAbC.class,"secondState", ThirdStateFromAbC.class);
        System.out.println(secondStateFromAbC.name);
        AbstractAutonState thirdStateFromAbC = factoryService.get(ThirdStateFromAbC.class,"thirdDoggie", FirstStateFromAbC.class);
        System.out.println(thirdStateFromAbC.name);

        String s =FirstStateFromAbC.getFunction().apply("jojo".toString());
        System.out.println(s);

        AbstractAutonState funcState = FirstStateFromAbC.myConsFunc.apply("Albert", SecondStateFromAbC.class);
        System.out.println(funcState.name);
        System.out.println(funcState.nextState.getSimpleName());

        AbstractAutonState funcState2 = SecondStateFromAbC.myConsFunc.apply("Second Albert", ThirdStateFromAbC.class);
        System.out.println(funcState2.name);
        System.out.println(funcState2.nextState.getSimpleName());
        System.out.println(funcState2.getClass().getSimpleName());

        Class varNextState = funcState.nextState;
//        varNextState.myConsFunc.apply()

        AbstractAutonState lambdaState = FirstStateFromAbC.myLambdaConstructor.apply("MyLambdaOne", SecondStateFromAbC.class);
        System.out.println(lambdaState.name + " " + lambdaState.nextState.getSimpleName());

        Class<? extends AbstractAutonState> lambdaNextState = lambdaState.nextState;
        try {
            AbstractAutonState dynNextState = lambdaNextState.getConstructor(String.class, Class.class).newInstance("ConsCall", ThirdStateFromAbC.class);
            System.out.println(dynNextState.name + "  " + dynNextState.nextState.getSimpleName());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        try{
            Class<AbstractAutonState> myClass2 = lambdaState.nextState;
            Method[] methods = myClass2.getMethods();
            for(int i=0;i<methods.length;i++){
                System.out.println(methods[i].getName());
            }
            Method myLam = myClass2.getMethod("getMyLambdaConstructor");
            System.out.println(myLam.getName());
            AbstractAutonState methState = (AbstractAutonState) myLam.invoke(myClass2);
            System.out.println(methState.name);
        } catch (Exception e){
            System.out.println("In catch of Method try");
            System.out.println(e.getMessage());
        }
        try{
            System.out.println("Looking at varNextState " + varNextState.getSimpleName());
        } catch (Exception e){
            System.out.println("In the catch block");
            System.out.println(e.getMessage());
        }

        HashMap<Class<? extends AbstractAutonState>, GetStateConstructor<String, Class<? extends AbstractAutonState>, AbstractAutonState>>
              lambdaMap  = new HashMap<>();
        lambdaMap.put(FirstStateFromAbC.class, FirstStateFromAbC.myLambdaConstructor);
        lambdaMap.put(SecondStateFromAbC.class, SecondStateFromAbC.myLambdaConstructor);

        GetStateConstructor<String, Class<? extends AbstractAutonState>, AbstractAutonState> myConstr = lambdaMap.get(lambdaNextState);
        AbstractAutonState forcedLambda = myConstr.apply("Forcing the Lambda", ThirdStateFromAbC.class);
        System.out.println(forcedLambda.name + "  " + forcedLambda.nextState.getSimpleName());

        String nameJojo = "jojo one one";
        Class<? extends AbstractAutonState> startClass = FirstStateFromAbC.class;
        try {
            Method myInstantiator = startClass.getMethod("getIt", String.class, Class.class);
            AbstractAutonState methState = (AbstractAutonState) myInstantiator.invoke(null, nameJojo, SecondStateFromAbC.class);
            System.out.println(methState.name + "  " + methState.nextState.getSimpleName());
        }catch (Exception e){
            System.out.println("In Catch clause for startClass");
            System.out.println(e.getMessage());
        }


//        HashMap<Class<? extends AbstractAutonState>, Function<String, AbstractAutonState>> constructorHashMap = new HashMap<>();
//        constructorHashMap.put(FirstStateFromAbC.class, "jojo"->new FirstStateFromAbC(nameJojo, SecondStateFromAbC.class));

        // Create a sequence test getting next ones
        System.out.println();
        System.out.println("Looking at sequence for...");
        AutonSequence sequence = new AutonSequence(PresetAutonSequences.STANDARD);
        Class<? extends AbstractAutonState> c;
        for(c = sequence.getNextAutonStateClass(); c != null; c=sequence.getNextAutonStateClass()){
            System.out.println(c.getSimpleName());
        }

        int a[][] = {{2,1},{1,2}};
        int b[][] = {{5,5},{5,5}};

    }

}

interface TimesTen{
    public int doIt(int inputInt);
}

interface StateInstanceFactory{
    public AbstractAutonState getInstance();
}

