import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    //
    private int dx, dy, x, y;
    private Image image;

    public Player(){
        initPlayer();
    }

    private void initPlayer(){
        ImageIcon ii = new ImageIcon(this.getClass().getResource("player.png"));
        image = ii.getImage();
        x = 10;
        y = 10;
    }

    public void move(){
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                dx -= 1;
                break;
            case KeyEvent.VK_RIGHT:
                dx += 1;
                break;
            case KeyEvent.VK_UP:
                dy -= 1;
                break;
            case KeyEvent.VK_DOWN:
                dy += 1;
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                dx = 0;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 0;
                break;
            case KeyEvent.VK_UP:
                dy = 0;
                break;
            case KeyEvent.VK_DOWN:
                dy = 0;
                break;
            default:
                break;
        }
    }
}
