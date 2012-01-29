package projekt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import projekt.event.Keys;
import render.Battle;
import render.Player;
import render.Game;

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
    private boolean menu = true;
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

        menurender = new Menu(this);
        game = new Game();
        game.ins = getInsets();
        boss = new Player(10, 17);
        boss.setChar("/res/CharMain/boss.png");
        boss.direciton = 2;
        fight = new Battle(game.focus, boss);

        this.addKeyListener(game.eHandle);
        add(game);
        add(menurender);
        add(fight);
        game.setVisible(false);
        fight.setVisible(false);
        fight.setBounds(0, 0, WIDTH, HEIGHT);
        game.setBounds(0, 0, WIDTH, HEIGHT);
        menurender.setBounds(0, 0, WIDTH, HEIGHT);

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
                if (menu) {
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
        if (menu) {
            menurender.event();
        } else if (game.fight) {
            fight.tick(keys);
        } else {
            game.tick(keys);
        }
        if (game.fight) {
            game.setVisible(false);
            menurender.setVisible(false);
            fight.setVisible(true);
            menu = false;
        }
        if (keys[Keys.a] && menurender.lvl == 0 && menurender.step == 0 && menu) {
            menu = false;
            game.setVisible(true);
            menurender.setVisible(false);
            //remove(menurender);
            //add(game);
        } else if (keys[Keys.esc] && !menu) {
            menu = true;

            game.setVisible(true);
            menurender.setVisible(false);
        }
    }
}
