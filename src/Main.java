import javax.swing.*;

public class Main {

    private Main() {
        GUI gui = new GUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
