package com.example.testlib.factorytrial;

public class FirstStateFromInt implements AutonStateInt<FirstStateFromInt>{
    private String name;
    public FirstStateFromInt(){
        name = "jojo";
    }


    public FirstStateFromInt getInstance() {
        return new FirstStateFromInt();
    }


}
