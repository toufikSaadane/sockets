package org.example;

import org.example.model.FileDetails;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<FileDetails> fileDetails = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int fileId = 0;
        JFrame jFrame = new JFrame("Server");
        jFrame.setSize(400, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jTitle = new JLabel("File receiver");
        jTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(8000);
        boolean connection = true;
        while (connection) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int fileNameLength = dataInputStream.readInt();
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel jFileRow = new JPanel();
                        jFileRow.setLayout(new BoxLayout(jFileRow, BoxLayout.Y_AXIS));

                        JLabel jFileName = new JLabel(fileName);
                        jFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jFileName.setBorder(new EmptyBorder(10, 0, 10, 0));

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")){
                            jFileRow.setName(String.valueOf(fileId));
                            jFileRow.addMouseListener(panelMouseListener());
                        }
                    }
                }


            } catch (IOException exception) {
                exception.getMessage();
            }
        }

    }

    private static MouseListener panelMouseListener() {
         return new MouseListener() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 JPanel jPanel = (JPanel) e.getSource();
                 int fieldId = Integer.parseInt(jPanel.getName());

                 for (FileDetails file : fileDetails){
                     if (file.getId() == fieldId){
                         JFrame jFrame = createFilesFrame(file.getName(), file.getData(), file.getFileExtension());
                         jFrame.setVisible(true);
                     }
                 }

             }

             @Override
             public void mousePressed(MouseEvent e) {

             }

             @Override
             public void mouseReleased(MouseEvent e) {

             }

             @Override
             public void mouseEntered(MouseEvent e) {

             }

             @Override
             public void mouseExited(MouseEvent e) {

             }
         };
    }

    private static JFrame createFilesFrame(String fileName, byte[] fileData, String fileExtension) {

        JFrame jFrame = new JFrame("Downloader");
        jFrame.setSize(400, 400);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JLabel jLabelTitle = new JLabel("File downloader");
        jLabelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 20));
        jLabelTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jPrompt = new JLabel("are you sure" + fileName);
        jPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));

        JButton jButtonYes = new JButton("YES");
        jButtonYes.setPreferredSize(new Dimension(125, 70));
        jButtonYes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jButtonNo = new JButton("NO");
        jButtonNo.setPreferredSize(new Dimension(125, 70));
        jButtonNo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel jFileContent = new JLabel();
        jFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jPanelButton = new JPanel();
        jPanelButton.setBorder(new EmptyBorder(20, 0, 10, 0));
        jPanelButton.add(jButtonYes);
        jPanelButton.add(jButtonNo);

        if (fileExtension.equalsIgnoreCase("txt")){
            jFileContent.setText("<html>" + new String(fileData) + "</html>");
        }

        jButtonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileDownload = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();
                    jFrame.dispose();

                }catch (IOException exception){
                    exception.getMessage();
                }
            }
        });

        jButtonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });

        jPanel.add(jLabelTitle);
        jPanel.add(jPrompt);
        jPanel.add(jFileContent);
        jPanel.add(jPanelButton);

        jFrame.add(jPanel);

        return jFrame;
    }

    public static String getFileExtension(String fileName){
        int index = fileName.indexOf(".");
        if (index > 0){
            return fileName.substring(index + 1);

        }else {
            return "No extension found";
        }
    }
}
