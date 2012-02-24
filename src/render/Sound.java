/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import projekt.Menu;
import java.util.ArrayList;
/**
 *
 * @author johannes
 */
public class Sound {
    static ArrayList<Thread> sounds=new ArrayList<Thread>();
    static ArrayList<Runnable> r=new ArrayList<Runnable>();
    public static synchronized void playSound(final String url) {
        r.add(new Runnable() {
            boolean runned=false;
            Clip clip;
            int index=-1;
            public void run() {
                if(!runned){
                    try {
                        index = Sound.sounds.size()-1;
                        clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/res/sounds/" + url));
                        clip.open(inputStream);
                        clip.start();
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                        System.out.println(clip.getFrameLength());
                    } catch (Exception e) {
                        //System.err.println(e);
                    }
                    runned=true;
                }
                else{
                    clip.loop(0);
                    clip.setFramePosition(clip.getFrameLength()-80000);
                    while(clip.isRunning());
                    Sound.sounds.get(index).interrupt();
                    Sound.sounds.remove(index);
                }
            }
        });
        sounds.add(new Thread( r.get(r.size()-1) ));
        sounds.get(sounds.size()-1).setName("Sound number:"+sounds.size());
        sounds.get(sounds.size()-1).start();
    }

    public static synchronized void stopSound(int index) {
        if(sounds.size()>index){
            r.get(index).run();
            //sounds.remove(index);
        }
    }
}
