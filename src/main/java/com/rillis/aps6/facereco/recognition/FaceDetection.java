package com.rillis.aps6.facereco.recognition;


import com.rillis.aps6.facereco.images.Picture;
import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {
    public static CascadeClassifier cascadeClassifier;
    private static boolean initialized = false;

    public static void init(){
        if(!initialized){
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            cascadeClassifier = new CascadeClassifier("xml/lbpcascade_frontalface.xml");
            initialized = true;
        }
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
