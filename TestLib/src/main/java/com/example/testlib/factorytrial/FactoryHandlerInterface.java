package com.example.testlib.factorytrial;

public interface FactoryHandlerInterface {

    public Class<? extends AbstractAutonState> instanceHandler(String stateType);

}
