package com.example.testlib.factorytrial;

import java.util.ArrayList;
import java.util.Arrays;

public enum PresetAutonSequences {
    STANDARD(new ArrayList<>(Arrays.asList(
            FirstStateFromAbC.class))),
    AGGRESSIVE(new ArrayList<>(Arrays.asList(
            SecondStateFromAbC.class,
            ThirdStateFromAbC.class)));

    private ArrayList<Class<? extends AbstractAutonState>> sequence;
    PresetAutonSequences(ArrayList<Class<? extends AbstractAutonState>> sequenceIn)
    {
        this.sequence = sequenceIn;
    }

    public ArrayList<Class<? extends AbstractAutonState>>getSequence(){
        // Get the preset list
        return this.sequence;
    }
}
