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
    public static ArrayList<Thread> sounds=new ArrayList<Thread>();
    public static ArrayList<Runnable> r=new ArrayList<Runnable>();
    public static String args[] = null;
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
                        runned=true;
                        return;
                    } catch (Exception e) {
                        runned=true;
                    }
                }
                if( args != null && args.length > 0 ){
                    /** @FIXME add arguments here */
                    return;
                }

                clip.loop(0);
                /*if(Sound.sounds.get(index).getName().equals("hitros.wav")){
                    clip.start();
                    clip.setFramePosition(clip.getFrameLength()-80000);
                    while(clip.isRunning());
                }*/
                clip.stop();
                Sound.sounds.get(index).interrupt();
                Sound.sounds.remove(index);
                Sound.r.remove(index);
            }
        });
        sounds.add(new Thread( r.get(r.size()-1) ));
        sounds.get(sounds.size()-1).setName(url);
        sounds.get(sounds.size()-1).start();
    }

    public static synchronized void stopSound(int index) {
        if(sounds.size()>index && index >= 0){
            r.get(index).run();
            //sounds.remove(index);
        }
    }

    public static void stopAllSound() {
        for(int i=0;i<sounds.size();i++)
            stopSound(i);
    }
}
