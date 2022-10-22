package com.rillis.aps6.facereco.gui;

import com.rillis.aps6.facereco.auth.Person;
import com.rillis.aps6.facereco.images.Picture;
import com.rillis.aps6.facereco.recognition.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.util.Objects;

public class Webcam {
    private boolean capturing = true;
    private VideoCapture capture;

    public Webcam(){
        FaceDetection.init();
        FaceRecognition.init();
    }

    public void start(JLabel label){
        FaceRecognition.train();
        startCapturing(label);
    }

    private void startCapturing(JLabel label){
        capture = new VideoCapture(0);
        Mat image = new Mat();

        new Thread(() -> {
            while(capturing){
                byte[] imageData;
                capture.read(image);
                ImageIcon icon;
                final MatOfByte buf = new MatOfByte();

                treat(image);

                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();
                icon = new ImageIcon(imageData);

                label.setIcon(icon);
            }
        }).start();
    }

    private void treat(Mat image) {
        MatOfRect matOfRect = FaceDetection.detectFaces(image);

        for(Rect rect : matOfRect.toArray()){
            Picture picture = new Picture(new Mat(image, rect));
            picture.resizeToDefaultSize();
            picture.toGrayScale();

            Scalar scalar = new Scalar(0,0,255);
            Person person = FaceRecognition.recognize(picture.getPicMat());
            if(!Objects.equals(person.getName(), "Unknown")){
                scalar = new Scalar(0, 255, 0);
            }
            Imgproc.putText(image, person.getName() + " " + round(person.getConfidence()*100)+"%", new Point(rect.x, rect.y+rect.height+22), Imgproc.FONT_HERSHEY_PLAIN, 2, scalar);

            Imgproc.rectangle(image, rect, scalar, 2);
        }
    }

    public void stop(){
        capturing = false;
        capture.release();
    }

    private double round(double n){
        return Math.round(n * 10.0) / 10.0;
    }
}
