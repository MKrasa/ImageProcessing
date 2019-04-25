import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    private JSpinner forkNumberChooser;

    GUI(){
        frame = new JFrame("Image processor");

        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        frame.add(this.infoPanelMaker(0, 0), BorderLayout.SOUTH);
        frame.add(this.labelMaker(), BorderLayout.CENTER);
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

    String getDirectoryPath(){
        return imageDirectory.getAbsolutePath();
    }

    Integer getForksNumber(){
        int forksNumber = (Integer) forkNumberChooser.getValue();
        return forksNumber;
    }

    private JPanel labelMaker(){

        JPanel panel = new JPanel();
        panel.setSize(200, 400);

        chosenFileLabel = new JLabel("...");
        JButton chooseFolderButton = new JButton("Wybierz folder: ");
        chooseFolderButton.addActionListener(new ChoseDirectoryListener());

        JLabel forkNumberLabel = new JLabel("Wybierz liczbę wątków: ");
        SpinnerNumberModel forkNumberModel = new SpinnerNumberModel(0, 0, 20, 1);
        forkNumberChooser = new JSpinner(forkNumberModel);

        JButton startProcessing = new JButton("Start");
        startProcessing.addActionListener(new StartListener());

        panel.add(chooseFolderButton);
        panel.add(chosenFileLabel);
        panel.add(forkNumberLabel);
        panel.add(forkNumberChooser);
        panel.add(startProcessing);

        return panel;
    }

    private class ChoseDirectoryListener implements ActionListener{

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

    private class StartListener implements ActionListener{

        public List<String> GetDirs(String path) {

            List<String> dirs = new ArrayList<>();
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                dirs = walk.map(x -> x.toString())
                        .filter(f -> f.endsWith(".jpg")).collect(Collectors.toList());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return  dirs;
        }

        //TODO mierzenie czasu
        //TODO wyswietlanie czasu

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                String path = getDirectoryPath();
                System.out.println(path);

                Integer count = getForksNumber();
                System.out.println(count);

                List<String> lDirectories = GetDirs(path);

                ForkJoinPool forkJoinPool = new ForkJoinPool();

                try {
                    forkJoinPool.invoke(new ForkJoinManager(lDirectories, count));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (NullPointerException exept) {
                JOptionPane.showMessageDialog(frame, "Nie wybrano folderu ze zdjęciami!", "Wybierz folder", JOptionPane.WARNING_MESSAGE);
            }



        }
    }

}


