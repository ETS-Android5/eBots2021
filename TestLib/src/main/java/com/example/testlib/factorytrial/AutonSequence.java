package com.example.testlib.factorytrial;

import java.util.ArrayList;

public class AutonSequence {

    private ArrayList<Class<? extends AbstractAutonState>> sequence;

    public ArrayList<Class<? extends AbstractAutonState>> getSequence(){
        return this.sequence;
    }

    public AutonSequence (PresetAutonSequences presetSequence){
        this.sequence = new ArrayList<>(presetSequence.getSequence());
    }

    public Class<? extends AbstractAutonState> getNextAutonStateClass(){
        Class<? extends AbstractAutonState> returnValue = null;
        try{
            returnValue = this.sequence.get(0);
            this.sequence.remove(0);
        } catch (Exception e){
            System.out.println("In catch of AutonSequence::getnextAutonStateClass");
            System.out.println(e.getMessage());
        }
        return returnValue;
    }
}
