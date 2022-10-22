package com.rillis.aps6.facereco.auth;

import com.rillis.aps6.facereco.Main;
import com.rillis.aps6.facereco.images.Picture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Person {
    public static Map<Integer, Person> persons = new HashMap<>();

    String name;
    String cargo;
    int authLevel;
    int labelID;
    Picture[] trainingPics;
    double confidence = 0;

    public Person(String name, String cargo, int authLevel, int labelID) {
        this.name = name;
        this.cargo = cargo;
        this.authLevel = authLevel;
        this.labelID = labelID;

        ArrayList<Picture> pictures = new ArrayList<>();
        File trainFolder = new File(Main.inputTrainingDir+"/"+labelID);
        for(File trainFile : trainFolder.listFiles()) {
            pictures.add(new Picture(Main.inputTrainingDir+"/"+labelID+"/"+trainFile.getName(), Main.trainingDir+"/"+labelID+"-"));
        }
        this.trainingPics = pictures.toArray(new Picture[0]);
    }

    public Person(){
        this.name = "Unknown";
        this.cargo = "Unknown";
        this.authLevel = 1;
    }

    public String getName() {
        return name;
    }

    public String getCargo() {
        return cargo;
    }

    public int getAuthLevel() {
        return authLevel;
    }

    public int getLabelID() {
        return labelID;
    }

    @Override
    public String toString() {
        return "name = " + getName() + ", cargo = " + cargo + ", authLevel = " + getAuthLevel();
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
