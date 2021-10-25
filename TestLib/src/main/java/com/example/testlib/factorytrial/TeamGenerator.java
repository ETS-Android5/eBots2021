package com.example.testlib.factorytrial;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TeamGenerator {
    public static void main(String[] args) {

        int teamBuffer1 = 4;
        int teamBuffer2 = 3;
        ArrayList<String> ebotsMembers = new ArrayList<>();
        //3 teams of 4 and 1 team of 3
        ArrayList<String> team1 = new ArrayList<>();
        ArrayList<String> team2 = new ArrayList<>();
        ArrayList<String> team3 = new ArrayList<>();
        ArrayList<String> team4 = new ArrayList<>();
        //there are 15 members
        ebotsMembers.add("Thomas"); //
        ebotsMembers.add("Christian"); //
        ebotsMembers.add("Michael"); //
        ebotsMembers.add("Carter"); //
        ebotsMembers.add("Arjun"); //
        ebotsMembers.add("Ishita"); //
        ebotsMembers.add("Maria"); //
        ebotsMembers.add("Ethan"); //
        ebotsMembers.add("Sean"); //
        ebotsMembers.add("Kenny"); //
        ebotsMembers.add("Lyla"); //
        ebotsMembers.add("Ryleigh M."); //
        ebotsMembers.add("Elanur"); //
        ebotsMembers.add("Riley W."); //
        ebotsMembers.add("Zachary"); //
        for (int i = 0; i <15 ; i++) {
            Random rnd = ThreadLocalRandom.current();
            int randomIndex = rnd.nextInt(ebotsMembers.size());
            team1.add(ebotsMembers.remove(randomIndex));
            if (team1.size() > teamBuffer1) {
                team2.add(team1.remove(0));
            }
            if (team2.size() > teamBuffer1) {
                team3.add(team2.remove(0));
            }
            if (team3.size() > teamBuffer1) {
                team4.add(team3.remove(0));
            }
        }

        System.out.println("Team 1");
        System.out.println(team1);
        System.out.println("Team 2");
        System.out.println(team2);
        System.out.println("Team 3");
        System.out.println(team3);
        System.out.println("Team 4");
        System.out.println(team4);
    }
}
