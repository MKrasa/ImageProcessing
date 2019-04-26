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


class GUI
{
    private JFrame frame;
    private File imageDirectory;
    private JFileChooser chooser;
    private JSpinner forkNumberChooser;
    private JLabel chosenFileLabel;
    private JLabel processingTimeLabel;

    GUI() {
        frame = new JFrame("Image processor");

        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        frame.add(this.infoPanelMaker(), BorderLayout.SOUTH);
        frame.add(this.labelMaker(), BorderLayout.CENTER);
        frame.setVisible(true);

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
    }


    private JPanel infoPanelMaker() {
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Czas przetwarzania: ");
        processingTimeLabel = new JLabel(Integer.toString(0));

        panel.add(label1);
        panel.add(processingTimeLabel);
        return panel;
    }

    private String getDirectoryPath(){
        return imageDirectory.getAbsolutePath();
    }

    private Integer getForksNumber(){
        return (int) (Integer) forkNumberChooser.getValue();
    }

    private JPanel labelMaker() {

        JPanel panel = new JPanel();
        panel.setSize(200, 400);

        chosenFileLabel = new JLabel("...");
        JButton chooseFolderButton = new JButton("Wybierz folder: ");
        chooseFolderButton.addActionListener(new ChoseDirectoryListener());

        JLabel forkNumberLabel = new JLabel("Wybierz liczbę zdjęć na forka: ");
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

    private class ChoseDirectoryListener implements ActionListener {
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

    private class StartListener implements ActionListener {
        List<String> GetDirs(String path) {
            List<String> dirs = new ArrayList<>();
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                dirs = walk.map(Path::toString)
                        .filter(f -> f.endsWith(".jpg")).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  dirs;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                long startTime = System.nanoTime();

                String path = getDirectoryPath();

                Integer count = getForksNumber();

                List<String> lDirectories = GetDirs(path);

                ForkJoinPool forkJoinPool = new ForkJoinPool();

                try {
                    forkJoinPool.invoke(new ForkJoinManager(lDirectories, count));
                    double processingTime = (System.nanoTime() - startTime) / (double) 1000000000;
                    processingTimeLabel.setText(String.valueOf(processingTime));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (NullPointerException exept) {
                JOptionPane.showMessageDialog(frame, "Nie wybrano folderu ze zdjęciami!", "Wybierz folder", JOptionPane.WARNING_MESSAGE);
            }



        }
    }

}


