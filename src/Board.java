import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private final int DELAY = 40;
    private Image background;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);

        ImageIcon ii = new ImageIcon(this.getClass().getResource("board.png"));
        background = ii.getImage();

        player = new Player();

        timer = new Timer(DELAY, this);
        timer.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background,0,0,null);
        g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        player.move();
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}