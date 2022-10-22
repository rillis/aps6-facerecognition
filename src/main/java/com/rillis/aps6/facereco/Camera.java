// Java Program to take a Snapshot from System Camera
// using OpenCV

// Importing openCV modules
package com.rillis.aps6.facereco;
// importing swing and awt classes
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Importing date class of sql package
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.rillis.aps6.facereco.auth.Person;
import com.rillis.aps6.facereco.images.Picture;
import com.rillis.aps6.facereco.recognition.FaceDetection;
import com.rillis.aps6.facereco.recognition.FaceRecognition;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
// Importing VideoCapture class
// This class is responsible for taking screenshot
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

// Class - Swing Class
public class Camera extends JFrame {

    // Camera screen
    private JLabel cameraScreen;

    // Button for image capture
    private JButton btnCapture;

    // Start camera
    private VideoCapture capture;

    // Store image as 2D matrix
    private Mat image;

    private boolean clicked = false;

    public Camera(int labelID)
    {

        // Designing UI
        setLayout(null);

        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);

        btnCapture = new JButton("capture");
        btnCapture.setBounds(300, 480, 80, 40);
        add(btnCapture);

        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                clicked = true;
            }
        });

        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Creating a camera
    public void startCamera(int labelID)
    {
        FaceDetection.init();
        FaceRecognition.init();

        Person messi = new Person("Rillis", "Ministro", 3, 1000);
        Person.persons.put(messi.getLabelID(), messi);

        Person neymar = new Person("Neymar", "Jogador de futebol", 2, 2000);
        Person.persons.put(neymar.getLabelID(), neymar);

        Person donovan = new Person("Donovan", "Diretor", 2, 3000);
        Person.persons.put(donovan.getLabelID(), donovan);

        FaceRecognition.train();


        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;

        ImageIcon icon;
        while (true) {
            // read image to matrix
            capture.read(image);

            // convert matrix to byte
            final MatOfByte buf = new MatOfByte();


            Mat originalImage = image.clone();

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
                    System.out.println(person.getName() + " " + round(person.getConfidence()*100)+"% "+person.getLabelID());

                Imgproc.rectangle(image, rect, scalar, 2);

            }


            Imgcodecs.imencode(".jpg", image, buf);



            imageData = buf.toArray();

            // Add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);

            // Capture and save to file
            if (clicked) {
                String folderName = Main.inputTrainingDir + "/" + labelID;
                File folder = new File(folderName);

                if(!folder.exists())
                    folder.mkdir();

                int nameOff = 0;
                if(folder.listFiles()!= null){
                    nameOff = folder.listFiles().length;
                }

                String fileName = folderName + "/" + nameOff + ".jpg";


                Picture picture = new Picture(originalImage);
                //picture.treatImage("");

                Imgcodecs.imwrite(fileName, picture.getPicMat());

                clicked = false;
            }
        }
    }

    private double round(double n){
        return Math.round(n * 10.0) / 10.0;
    }

    // Main driver method
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            // Overriding existing run() method
            @Override public void run()
            {
                int labelID = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do usuario"));
                final Camera camera = new Camera(labelID);
                // Start camera in thread
                new Thread(new Runnable() {
                    @Override public void run()
                    {
                        camera.startCamera(labelID);
                    }
                }).start();
            }
        });
    }
}
