import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class GUI {

    private JFrame frame;
    private JPanel panel;

    private JFileChooser chooser;
    private File imageDirectory;
    private JLabel chosenFileLabel;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    GUI(){
        frame = new JFrame("Image processor");

        frame.add(this.buttonMaker(), BorderLayout.CENTER);
        frame.add(this.labelMaker(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

    }

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

    private JPanel buttonMaker(){
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel labelMaker(){
        JPanel panel = new JPanel();
        chosenFileLabel = new JLabel("...");
        chosenFileLabel.setBounds(50, 50, 20, 100);
        panel.add(chosenFileLabel);


        JButton chooseFolderButton = new JButton("Wybierz folder");
        chooseFolderButton.setBounds(100, 100, 30, 30);
        chooseFolderButton.addActionListener(new StartListener());
        panel.add(chooseFolderButton);
        return panel;
    }

    private class StartListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                chooser.showOpenDialog(frame);
                imageDirectory = chooser.getCurrentDirectory();
                chosenFileLabel.setText("      Wybrany folder: " + imageDirectory.getName());
            } catch (NullPointerException ex){
                JOptionPane.showMessageDialog(frame, "Nie wybrano folderu ze zdjęciami!", "Wybierz folder", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

}


