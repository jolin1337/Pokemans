/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
/**
 *
 * @author johannes
 */
public class PokemansApplet extends JApplet{
    public void init(){
        try {
            SwingUtilities.invokeAndWait(new Runnable(){
                @Override
                public void run(){
                    add(new Runner());
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(PokemansApplet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
