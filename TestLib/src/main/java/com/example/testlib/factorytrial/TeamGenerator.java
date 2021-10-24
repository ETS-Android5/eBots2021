package com.example.testlib.factorytrial;

import com.sun.deploy.appcontext.AppContext;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TeamGenerator {
    public static void main(String[] args) {
        Random rnd = ThreadLocalRandom.current();
        int randomIndex = rnd.nextInt(14);
        ArrayList<String> ebotsMembers = new ArrayList<>();
        ArrayList<String> team1 = new ArrayList<>();
        ArrayList<String> team2 = new ArrayList<>();
        ArrayList<String> team3 = new ArrayList<>();
        ArrayList<String> team4 = new ArrayList<>();
        //there are 15 members
        ebotsMembers.add("Thomas");
        ebotsMembers.add("Christian");
        ebotsMembers.add("Michael");
        ebotsMembers.add("Carter");
        ebotsMembers.add("Arjun");
        ebotsMembers.add("Ishita");
        ebotsMembers.add("Maria");
        ebotsMembers.add("Ethan");
        ebotsMembers.add("Christian");
        ebotsMembers.add("Kenny");
        ebotsMembers.add("Lyla");
        ebotsMembers.add("Ryleigh M.");
        ebotsMembers.add("Elanur");
        ebotsMembers.add("Riley W.");
        ebotsMembers.add("Zachary");

        String member = ebotsMembers.remove(randomIndex);
        ebotsMembers.
    }
}
