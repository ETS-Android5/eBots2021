package com.example.testlib.factorytrial;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.util.HashMap;

/**
 * Provide this factory a class for a state object and it will provide a factory for creating an instance
 *
 */
public class FactoryService {

    private static HashMap<Class, AutonStateFactory> handlerMap = new HashMap<>();

    public AbstractAutonState get(Class forClass, String name, Class nextState){
        AbstractAutonState returnValue = null;
//        if(forClass != ThirdStateFromAbC.class) {
//            AutonStateFactory factory = getStateFactory(forClass);
//            AbstractAutonState state = instantiate(factory, name);
//            returnValue= state;
//        } else {
        try {
            returnValue= (AbstractAutonState) forClass.getConstructor(String.class, Class.class).newInstance(name, nextState);
//            returnValue = null;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
//        }
        return returnValue;
    }

    public AbstractAutonState instantiate(AutonStateFactory factory, String name){
        return factory.getInstance(name, null);
    }

    public FactoryService(){
        String givenName = "Factory:";
        handlerMap.put(FirstStateFromAbC.class, (providedName, nextState)->new FirstStateFromAbC(providedName, nextState));
        handlerMap.put(SecondStateFromAbC.class, (inName, nextState) -> new SecondStateFromAbC(inName, nextState));
//        handlerMap.put(SecondStateFromAbC.class, providedName->new SecondStateFromAbC(providedName));
    }

    public AutonStateFactory getStateFactory(Class inClass){
            return handlerMap.get(inClass);
    }
}

@FunctionalInterface
interface AutonStateFactory{
    AbstractAutonState getInstance(String name, Class nextState);
}

@FunctionalInterface
interface GetStateConstructor<String, Class, AbstractAutonState>{
    public AbstractAutonState apply(String nameIn, Class nextStateIn);
}