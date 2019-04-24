import sun.misc.JavaLangAccess;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class GUI {

    private JFrame frame;
    private java.util.List<JTextField> textFields;
    private java.util.List<JLabel> labels;
    private JPanel panel;

    private JFileChooser chooser;
    private File imageDirectory;
    private JLabel chosenFileLabel;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    GUI(){
        frame = new JFrame("Image processor");

        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        frame.add(this.infoPanelMaker(0, 0), BorderLayout.SOUTH);
        frame.add(this.labelMaker(), BorderLayout.PAGE_START);
        frame.setVisible(true);

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
    }

    //ilość zdjęć na 1 forka
    //ilość zdjęć w folderze?
    //czas przetwarzania

    private void panelMaker(){
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JTextField textField = new JTextField(10);
        JLabel label = new JLabel("Wybierz folder ze zdjęciami");

        panel.add(textField);
        panel.add(label);

        if (imageDirectory ==null) {
            chosenFileLabel = new JLabel("     Wybrany Plik: ...");
        } else {
            chosenFileLabel.setText("      Wybrany Plik: " + imageDirectory.getName());
        }
    }

    private void textAreaMaker() {
        JPanel panel = new JPanel();
        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.setLayout(new BorderLayout());
        panel.add(textArea);
        scrollPane = new JScrollPane(panel);
    }

    private JPanel infoPanelMaker(int photoPerFork, int processingTime){
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Liczba zdjęc na forka: ");
        JLabel photoPerForkLabel = new JLabel(Integer.toString(photoPerFork));
        JLabel label1 = new JLabel("Czas przetwarzania: ");
        JLabel processingTimeLabel = new JLabel(Integer.toString(processingTime));

        panel.add(label);
        panel.add(photoPerForkLabel);
        panel.add(label1);
        panel.add(processingTimeLabel);
        return panel;
    }

    private JPanel labelMaker(){

        JPanel panel = new JPanel();
        chosenFileLabel = new JLabel("...");
//        chosenFileLabel.setBounds(50, 50, 20, 200);
        panel.add(chosenFileLabel);

        JButton chooseFolderButton = new JButton("Wybierz folder");
//        chooseFolderButton.setBounds(100, 100, 30, 60);
        chooseFolderButton.addActionListener(new StartListener());
        panel.add(chooseFolderButton);
        return panel;
    }

    private class StartListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                chooser.showOpenDialog(frame);
                imageDirectory = chooser.getSelectedFile();
                chosenFileLabel.setText("      Wybrany folder: " + imageDirectory.getPath());
            } catch (NullPointerException ex){
                JOptionPane.showMessageDialog(frame, "Nie wybrano folderu ze zdjęciami!", "Wybierz folder", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

}


