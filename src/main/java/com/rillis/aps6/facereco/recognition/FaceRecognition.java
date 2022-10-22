package com.rillis.aps6.facereco.recognition;

import com.rillis.aps6.facereco.Main;
import com.rillis.aps6.facereco.auth.Person;
import com.rillis.aps6.facereco.images.Picture;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.*;
import org.opencv.face.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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

    public static void init(){
        unload();

        Loader.load(opencv_java.class);

        //faceRecognizer = FisherFaceRecognizer.create();
        faceRecognizer = LBPHFaceRecognizer.create();
    }

    public static void unload(){
        wipeFolder(Main.testingDir);
        wipeFolder(Main.trainingDir);
    }

    public static void train(){
        String trainingDir = Main.trainingDir;
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
        return recognize(new Picture(testImage, Main.testingDir+"/").getPicMat());
    }

    public static Person recognize(Mat mat){
        int[] label = new int[1];
        double[] confidence = new double[1];
        faceRecognizer.predict(mat, label, confidence);

        double realConfidence = (200 - confidence[0])/200;

        if(realConfidence > 0.7){
            Person choosen = Person.persons.get(label[0]);
            choosen.setConfidence(realConfidence);
            return choosen;
        }
        Person choosen = new Person();
        choosen.setConfidence(realConfidence);
        return choosen;
    }

}
