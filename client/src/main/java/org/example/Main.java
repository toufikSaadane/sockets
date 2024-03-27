package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        System.out.printf("Hello and welcome!\n");

        final File[] files = new File[1];

        JFrame jFrame = new JFrame("Transfer Client");
        jFrame.setSize(450, 450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("Sender");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choose file");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 25));
        jlFileName.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jButton = new JPanel();
        jButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jSendFile = new JButton("Send file");
        jSendFile.setPreferredSize(new Dimension(150, 70));
        jSendFile.setFont(new Font("Arial", Font.BOLD, 25));


        JButton jChooseFile = new JButton("choose file");
        jChooseFile.setPreferredSize(new Dimension(150, 70));
        jChooseFile.setFont(new Font("Arial", Font.BOLD, 25));

        jButton.add(jChooseFile);
        jButton.add(jSendFile);


        jChooseFile.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setDialogTitle("Choose a file to send");

                        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                            files[0] = jFileChooser.getSelectedFile();
                            jlFileName.setText("The file you want to send is : " + files[0].getName());
                        }

                    }
                }
        );

        jSendFile.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(files[0] == null){
                            jlFileName.setText("Please choose a file first");
                        }else {
                            try {
                                FileInputStream fileInputStream = new FileInputStream(files[0].getAbsoluteFile());
                                Socket socket = new Socket("localhost", 8000);

                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                String fileName = files[0].getName();
                                byte[] fileNameBytes = fileName.getBytes();
                                byte[] fileContentBytes = new byte[(int) files[0].length()];
                                fileInputStream.read(fileContentBytes);

                                dataOutputStream.writeInt(fileNameBytes.length);
                                dataOutputStream.write(fileNameBytes);

                                dataOutputStream.writeInt(fileContentBytes.length);
                                dataOutputStream.write(fileContentBytes);
                            }catch (IOException exception){
                                exception.getMessage();
                            }
                        }
                    }
                }
        );
        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jButton);
        jFrame.setVisible(true);
    }
}