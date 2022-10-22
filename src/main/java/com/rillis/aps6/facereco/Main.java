package com.rillis.aps6.facereco;

import com.rillis.aps6.facereco.auth.Person;
import com.rillis.aps6.facereco.images.Picture;
import com.rillis.aps6.facereco.recognition.FaceDetection;
import com.rillis.aps6.facereco.recognition.FaceRecognition;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

public class Main {
    public static String trainingDir = "images/training_data";
    public static String inputTrainingDir = "images/input_training";
    public static String inputTestingDir = "images/input_testing";
    public static String testingDir = "images/testing";

    public static void main(String[] args) {
        FaceDetection.init();
        FaceRecognition.init();

        //Person messi = new Person("Rillis", "Ministro", 3, 1000);
        //Person.persons.put(messi.getLabelID(), messi);

        Person neymar = new Person("Neymar", "Jogador de futebol", 2, 2000);
        Person.persons.put(neymar.getLabelID(), neymar);

        Person donovan = new Person("Donovan", "Diretor", 2, 3000);
        Person.persons.put(donovan.getLabelID(), donovan);

        FaceRecognition.train();

        for(File testCase : new File(inputTestingDir).listFiles()){
            System.out.println("Testcase: " + testCase.getName());

            Person personIdentified = FaceRecognition.recognize(inputTestingDir+"/"+testCase.getName());

            System.out.println("Identified: "+personIdentified+"\n");
        }
        //FaceRecognition.unload();
        end();
    }

    public static void end(){
        System.exit(0);
    }
}