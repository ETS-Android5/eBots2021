package com.example.testlib;

public class BodyOfWater {
    private String name;
    private double maxDepth;
    private double volume;
    private boolean isSalty;
    private boolean bordersMichigan;

    //  Constructor
    public BodyOfWater(String name, double depth, double volume, boolean isSalty, boolean bordersMichigan){
        this.name = name;
        this.maxDepth = depth;
        this.volume = volume;
        this.isSalty = isSalty;
        this.bordersMichigan = bordersMichigan;

    }

    public double getVolume(){
        return this.volume;
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    public boolean isSalty() {
        return isSalty;
    }

    public String getName() {
        return name;
    }

    public void receiveRainfall(double volume){
        this.volume += volume;
        this.maxDepth += (volume / 100);
    }

    public void evaporate(double volume){
        this.volume -= volume;
        this.maxDepth -= (volume / 100);

    }

    @Override
    public String toString(){
        return "The volume of " + this.name + " is " + this.volume + " and the maxDepth is " + this.maxDepth + " and isSalty: " + isSalty;
    }
}
