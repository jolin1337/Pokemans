package projekt;

import render.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import projekt.event.Dialogs;
import projekt.event.Keys;
import render.Battle;
import render.Game;
import render.Sound;

/**
 * Runner, spelets loop. Runner är klassen som loopar igenom spelet och anropar
 * vissa specifika funktioner
 *
 * @param public void run()	loop
 * @param public void start()	<JPanel>(panel, layerU startloop
 * @param public void stop()	stoploop
 * @param public eCheck(boolean[] keys)	eventskontroll
 * @author johannes
 */
public class Runner extends JFrame implements Runnable {

    private static final int WIDTH = 405;
    private static final int HEIGHT = 405;
    private static final int SCALE = 1;
    private boolean menu = false;
    private Menu menurender = null;
    private Battle fight = null;
    private static final String NAME = "POKEMANS";
    Player boss = new Player(10, 17);
    /**
     * avgör om skärmen skall visas i fullskärm eller i specifika dimmensioner.
     */
    protected boolean fullScreen = false;
    /**
     * Main game, denna variabel representerar canvasobjektet som visas på
     * skärmen.
     */
    Game game = null;
    /**
     * runner är den nya tråden som gör det möjligt att loopa spelet i en
     * whileloop utan att stänga ner andra funktioner så som events osv.
     */
    private Thread runner = null; // looping device

    /**
     * Konstruktorn sätter standardvärderna så som bakgrundsfärg, eventlisteners
     * och Canvaskomponenten
     */
    public Runner() {
        //super(NAME);
        //this.setTitle(NAME);
        //setUndecorated(true);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.BLACK);
        this.setResizable(false);
        this.setVisible(true);
        this.setFont(new Font("Arial", Font.PLAIN, 24));
        this.setDefaultCloseOperation(Runner.EXIT_ON_CLOSE);

        Dimension size = new Dimension(WIDTH, HEIGHT);
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        menurender = new Menu();
        Sound.stopAllSound();
        Sound.playSound("hitros.wav");
        game = new Game();
        game.ins = getInsets();
        boss = new Player(10, 17);
        boss.setChar("/res/CharMain/boss.png");
        boss.direciton = 2;
        fight = new Battle(game.focus, boss);

        add(game);
        add(menurender);
        add(fight);
        menurender.setVisible(true);
        game.setVisible(false);
        fight.setVisible(false);
        fight.setBounds(0, 0, WIDTH, HEIGHT);
        game.setBounds(0, 0, WIDTH, HEIGHT);
        menurender.setBounds(0, 0, WIDTH, HEIGHT);

        this.addKeyListener(game.eHandle);
        menurender.setFocusable(false);
        game.setFocusable(false);
        fight.setFocusable(false);
        start();

        /*
         * try { //if (!(this.fullScreen && s.setFullScreen(this))) {
         * //s.setScreen(this); //} try { //Thread.sleep(10000);
         * //System.out.print("...exit_complete();\n"); } catch (Exception e) {
         * } } finally { //game.stop();
         * //if(!(this.fullScreen&&s.restoreFullScreen(false)))
         * //s.restoreScreen(this,false); //this.dispose(); }
         */

        this.setLocationRelativeTo(null);
        this.pack();
    }

    /**
     * run() är den funktionen som anropas så fort du skapar ett nytt objekt och
     * då startar den en loop som inte slutar fören spelet pausas eller man
     * stänger ner fönstret
     */
    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        int frame = 0;

        double tickInt = 0;
        long startT = System.nanoTime();
        double secperTick = 1 / 60.0;
        int tickCount = 0;
        long bi = 100000000;
        //requestFocus();
        boolean b = false;

        while (runner != null) {
            long dt = System.nanoTime();
            long PST = dt - startT;
            startT = dt;
            if (PST < 0) {
                PST = 0;
            }
            if (PST > bi) {
                PST = bi;
            }

            tickInt += PST / (bi * 10.0);
            boolean ticked = false;
            while (tickInt > secperTick) {
                eCheck(game.eHandle.keys);
                if (!Keys.status.equals("")) {
                    long t = System.currentTimeMillis() % 1000;
                    String title = this.getTitle();
                    int speed = 10;
                    if (t >= 0 && t <= speed && title.equals(NAME + " - " + Keys.status + "  .") || title.equals(NAME)) {
                        this.setTitle(NAME + " - " + Keys.status + ".  ");
                    } else if (t >= 0 && t <= speed) {
                        if (title.equals(NAME + " - " + Keys.status + ".  ")) {
                            this.setTitle(NAME + " - " + Keys.status + " . ");
                        } else if (title.equals(NAME + " - " + Keys.status + " . ")) {
                            this.setTitle(NAME + " - " + Keys.status + "  .");
                        }
                    }
                } else {
                    this.setTitle(NAME);
                }
                tickInt -= secperTick;
                if (game.exitCode == 1 || menurender.exitCode == 1) {
                    System.out.println("\n");
                    this.dispose();
                    this.stop();
                }
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    //System.out.println(frame + " fps");
                    startT += bi;
                    frame = 0;
                    tickCount = 0;
                }
            }
            if (ticked) {

                game.screenCoords = getLocation();
                if (menu || menurender.story.running) {
                    menurender.render();
                } else if (game.fight) {
                    fight.render();
                } else {
                    game.render();
                }

                frame++;
            }
            /*
             * else //kanske lite onödig { try { Thread.sleep(1); } catch
             * (InterruptedException e) { } }
             */
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    /**
     * startar run() loopen
     */
    public final synchronized void start() {
        this.runner = new Thread(this);
        this.runner.start();
    }

    /**
     * stoppar run() loopen
     */
    public final synchronized void stop() {
        if (runner != null) {
            //this.runner.stop();
            runner = null;
        }
    }

    /**
     * omintetgör loopen run() har ingen annan effect än stop() för tillfälligt
     */
    public final synchronized void destroy() {
        if (runner != null) {
            //this.runner.destroy();
            runner = null;
        }
    }

    /**
     * eCheck används för att kolla om spelet har några nedtryckta tangenter och
     * vad man skall göra då
     *
     * @param keys boolean[], alla keys som finns
     */
    private void eCheck(boolean[] keys) {
        if (menu || menurender.story.running) {
            menurender.event(game.eHandle);
        } else if (game.fight) {
            fight.tick(keys);
        } else {
            game.tick(keys);
        }
        if (game.fight && !fight.isShowing()) {
            boss.copyChar(game.currentFightingOp);
            fight.me.copyChar(game.focus);
            game.setVisible(false);
            menurender.setVisible(false);
            fight.setVisible(true);
            menu = false;
            if(!Sound.sounds.get(0).getName().equals("teleporter.wav")){
                Sound.stopAllSound();
                Sound.playSound("teleporter.wav");
            }
        }
        if( fight.exitCode > 0 ){
            game.fight = false;
            if( fight.me.health <= 0  ){
                fight.clear();
                this.removeKeyListener(game.eHandle);
                this.remove(game);
                game = new Game();
                this.addKeyListener(game.eHandle);
                game.setFocusable(false);
                game.setBounds(0, 0, WIDTH, HEIGHT);
                this.add(game);
                
                game.setVisible(true);
                game.paintProps = true;
                menurender.setVisible(false);
                fight.setVisible(false);
                game.focus.health = game.focus.maxHealth;
                Sound.stopAllSound();
                Sound.playSound("hitros.wav");
                return;
            }
            if( fight.exitCode == 2 ){
                game.focus.kills++;
                if(game.focus.kills%2 == 1)
                    game.focus.lvl++;
            }
            game.currentFightingOp.copyChar(boss);
            fight.clear();
            game.setVisible(true);
            menurender.setVisible(false);
            fight.setVisible(false);
            keys[Keys.a] = false;
            Sound.stopAllSound();
            Sound.playSound("hitros.wav");
        }
        else{
            
        }
        if (keys[Keys.a] && ( menurender.lvl == 0 && menurender.step == 0 ) && !menurender.story.running) {
            if( menu ){
                menu = false;
                game.setVisible(true);
                menurender.setVisible(false);
                Sound.stopAllSound();
                Sound.playSound("hitros.wav");
            }
            else if( menurender.story.exitCode == 0 ){
                Sound.stopAllSound();
                Sound.playSound("teleporter.wav");
                menu = true;
                keys[Keys.a] = false;
                menurender.story.exitCode = 1;
            }
        } else if (keys[Keys.esc] && !menu) {
            menu = true;

            game.setVisible(true);
            menurender.setVisible(false);
        }
    }
}
