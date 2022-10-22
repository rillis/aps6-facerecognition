package com.rillis.aps6.facereco;

import com.rillis.aps6.facereco.recognition.FaceDetection;
import com.rillis.aps6.facereco.recognition.FaceRecognition;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Tests {
    public static void main(String[] args) {
        FaceDetection.init();
        FaceRecognition.init();

        Mat m = Imgcodecs.imread("images/input_testing/neymar.png");

        MatOfRect rects = FaceDetection.detectFaces(m);
        Rect rect = rects.toArray()[0];

        //Imgproc.rectangle(m, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0,255), 2);
        Mat m2 = new Mat(m, rect);
        Imgproc.resize(m2, m2, new Size(200, 200));

        Imgproc.cvtColor(m2, m2, Imgproc.COLOR_BGRA2GRAY);

        Imgcodecs.imwrite("images/tests/test.png", m2);

        Main.end();
    }
}
