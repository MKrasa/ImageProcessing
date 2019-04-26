import javax.swing.*;

/**
 *
 * @author Aleksandra Krystecka
 * @author Maciej Krasa
 * @author Oskar Słyk
 * @author Krzysztof Urban
 */


public class Main {

    private Main() {
        GUI gui = new GUI();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
