package com.rillis.aps6.facereco.recognition;

import com.rillis.aps6.facereco.auth.*;
import com.rillis.aps6.facereco.gui.*;
import com.rillis.aps6.facereco.images.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.opencv.*;
import org.opencv.core.*;
import org.opencv.face.*;
import org.opencv.imgcodecs.*;

import java.io.*;
import java.util.*;

public class FaceRecognition {
    private static FaceRecognizer faceRecognizer = null;

    private static void wipeFolder(String path){
        File folder = new File(path);
        for(File file : folder.listFiles()){
            if(!file.isDirectory()){
                file.delete();
            }
        }
    }

    private static boolean initialized = false;
    public static void init(){
        if(!initialized){
            unload();
            Loader.load(opencv_java.class);
            faceRecognizer = LBPHFaceRecognizer.create();
            initialized = true;
        }
    }

    public static void unload(){
        wipeFolder(CameraGUI.testingDir);
        wipeFolder(CameraGUI.trainingDir);
    }

    public static void train(){
        String trainingDir = CameraGUI.trainingDir;
        File root = new File(trainingDir);
        File[] files = root.listFiles();

        List<Mat> src = new ArrayList<>();
        Mat labels = new Mat(files.length, 1, CvType.CV_32SC1);
        int counter = 0;

        for(File file : files) {
            Mat img = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
            src.add(img);

            int i = Integer.parseInt(file.getName().split("\\-")[0]);
            labels.put(counter, 0, i);
            counter++;
        }

        faceRecognizer.train(src, labels);
    }

    public static Person recognize(String testImage) {
        return recognize(new Picture(testImage, CameraGUI.testingDir+"/").getPicMat());
    }

    public static Person recognize(Mat mat){
        int[] label = new int[1];
        double[] confidence = new double[1];
        faceRecognizer.predict(mat, label, confidence);

        double realConfidence = (200 - confidence[0])/200;

        Person chosen = new Person();
        if(realConfidence > 0.7){
            chosen = Person.persons.get(label[0]);
        }
        chosen.setConfidence(realConfidence);
        return chosen;
    }

}
