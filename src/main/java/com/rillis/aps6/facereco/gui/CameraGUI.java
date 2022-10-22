package com.rillis.aps6.facereco.gui;

import com.rillis.aps6.facereco.auth.Person;
import com.rillis.aps6.facereco.recognition.*;

import javax.swing.*;

public class CameraGUI extends JFrame {
    public static String trainingDir = "images/training_data";
    public static String inputTrainingDir = "images/input_training";
    public static String inputTestingDir = "images/input_testing";
    public static String testingDir = "images/testing";
    public CameraGUI(){
        setLayout(null);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBounds(0, 0, 600, 600);

        JLabel label = new JLabel();
        label.setBounds(0, 0, 600, 600);
        contentPane.add(label);

        setContentPane(contentPane);
        setVisible(true);

        Webcam webcam = new Webcam();
        webcam.start(label);
    }

    public static void main(String[] args) {
        FaceDetection.init();
        FaceRecognition.init();

        Person messi = new Person("Rillis", "Ministro", 3, 1000);
        Person.persons.put(messi.getLabelID(), messi);

        Person neymar = new Person("Neymar", "Jogador de futebol", 2, 2000);
        Person.persons.put(neymar.getLabelID(), neymar);

        Person donovan = new Person("Donovan", "Diretor", 2, 3000);
        Person.persons.put(donovan.getLabelID(), donovan);
        new CameraGUI();
    }
}
