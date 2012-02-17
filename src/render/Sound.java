/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import projekt.Menu;

/**
 *
 * @author johannes
 */
public class Sound {
	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/res/sounds/"+url));
					clip.open(inputStream);
					clip.start();
					} 
				catch (Exception e) {
					//System.err.println(e);
				}
			}
		}).start();
	}

}
