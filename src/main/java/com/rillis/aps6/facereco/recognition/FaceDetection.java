package com.rillis.aps6.facereco.recognition;


import com.rillis.aps6.facereco.images.Picture;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {
    public static CascadeClassifier cascadeClassifier;

    public static void init(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String xmlFile = "xml/lbpcascade_frontalface.xml";
        cascadeClassifier = new CascadeClassifier(xmlFile);
    }

    public static MatOfRect detectFaces(Picture source){
        MatOfRect result = new MatOfRect();
        cascadeClassifier.detectMultiScale(source.getPicMat(), result);
        return result;
    }

    public static MatOfRect detectFaces(Mat m){
        MatOfRect result = new MatOfRect();
        cascadeClassifier.detectMultiScale(m, result);
        return result;
    }


}
