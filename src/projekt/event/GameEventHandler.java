package projekt.event;

import java.awt.KeyEventDispatcher;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * läs er till på internet om denna jag orkar inte kommentera mera :(
 *
 * @author johannes
 */
public class GameEventHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener, KeyEventDispatcher {

    public boolean[] keys = new boolean[65536];
    public boolean release = true;
    public int previus;

    public GameEventHandler() {
    }

    public void mouseDragged(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void focusGained(FocusEvent arg0) {
    }

    public void focusLost(FocusEvent arg0) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k > 0 && k < keys.length) {
            keys[k] = true;
        }
        release = false;
        previus = e.getKeyCode();
        //proj.tick(keys);
    }

    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k > 0 && k < keys.length) {
            keys[k] = false;
        }
        release = true;
        //proj.tick(keys);
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        System.out.println("hej");
        int k = e.getKeyCode();
        if (k > 0 && k < keys.length) {
            keys[k] = !keys[k];
        }
        release = true;
        return false;
    }
}
