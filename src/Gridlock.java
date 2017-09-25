// Gridlock

import javax.swing.*;
import java.awt.*;

public class Gridlock extends JFrame{

    public Gridlock() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setSize(800, 600);
        setResizable(false);

        setTitle("Gridlock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Gridlock ex = new Gridlock();
            ex.setVisible(true);
        });
    }
}
