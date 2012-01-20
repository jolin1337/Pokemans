package projekt;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import projekt.event.*;
import render.PFont;
import render.Game;
import render.Render;

/**
 *
 * @author Klaph
 */
public class Menu extends Render {
    //Play
    //Namn
    //SPELA
    //Tutorial där man visar spelets koncept
    //Options 
    //Texthastighet
    //Fullskärmsläge 
    //Kontroller
    //Backa till föregående meny
    //(Quit)

    private final int WIDTH = 405, HEIGHT = 405;
    Image dbImage;
    Graphics dbg;
    Canvas can = new Canvas();
    Game proj = new Game();
    ImageIcon img;
    int step = 0;
    int diff = 0;
    int lvl = 0;
    int innerStep = 0;
    private int max = 3;
    private int innerMax = 0;
    int controll = -1;
    int incr = 10;
    private EventHandler eHandle = new EventHandler();
    private int exit = 0;
    Runner game = null;
    boolean gameStarted = false;
    JFrame ref = null;
    boolean fullscreenError = false;

    Menu(JFrame reff) {
        requestFocus();
        ref = reff;
        //super("Pokemans - Menu");
        init();
    }

    public final void init() {
        try {
            ref.setTitle("Pokemans - Menu");

            proj.removeKeyListener(proj.eHandle);
            java.awt.event.KeyListener[] keyl = null;
            if ((keyl = ref.getKeyListeners()).length > 0) {
                eHandle = (EventHandler) keyl[0];
            } else {
                ref.addKeyListener(eHandle);
            }
            addKeyListener(eHandle);
            proj.addKeyListener(eHandle);
            Color col = new Color(0, 0, 0, 0);

            img = new ImageIcon(getClass().getResource("/res/pokemans.png").getPath());
            setForeground(col);
            setBackground(col);

            //add(proj);
            Dimension size = new Dimension(WIDTH, HEIGHT);
            ref.setSize(size);
            ref.setPreferredSize(size);
            ref.setMinimumSize(size);
            ref.setMaximumSize(size);
            dbImage = createImage(WIDTH, HEIGHT);
            dbg = dbImage.getGraphics();
        } catch (Throwable t) {//ref is null!
        }
    }

    public void event() {
        if (eHandle.release) {			// gör så att man bara kan ta ett steg i taget
            if (lvl == 0) // om du är på yttre menyn dvs inte options
            {
                diff = step;			// sätt diff till samma som nuvarande cur position
            } else {
                diff = innerStep;		// diff till samma som nuvarande cur position
            }
        }
        if (controll == -1) {			// 
            if (eHandle.keys[KeyEvent.VK_ESCAPE]) // om escape är nertryckt
            {
                if (lvl == 0 && exit == 0) {							// om du är högst upp i hirearkin så stänger vi programmet, typ
                    exit = 1;
                    exitCode = 1;
                } else {											//annars så går vi upp i hirearkin
                    exit = 2;
                    lvl = 0;
                    innerStep = 0;
                }
            } else if (eHandle.keys[Keys.down] || eHandle.keys[KeyEvent.VK_S]) {	// om pil ner eller tangent S är nertryckt
                if (lvl == 0 && diff == step) // om om du är på lvl 0 och step är samma sak som diff
                {
                    step++;									// gå ner i menyn
                } else if (diff == innerStep) // om du är inne i options
                {
                    innerStep++;								// gå ner i options
                }
            } else if (eHandle.keys[Keys.up] || eHandle.keys[KeyEvent.VK_W]) {	// om pil upp eller tangent W är nertryckt
                if (lvl == 0 && diff == step) // om lvl är 0 och diff är resettad
                {
                    step--;									// går vi upp i menyn
                } else if (diff == innerStep) // om vi är inner i options
                {
                    innerStep--;								// gå upp i menyn
                }
            } else if (eHandle.keys[Keys.a] && lvl == 0) {					//
                lvl += step;
                if (lvl == 0) {
                    //exit=1;
                    //eHandle.keys[Keys.a]=false;
                    //game=new Runner();
                    //game.game.focus.incr=(double)incr/10;
                    //exit=1;
                    gameStarted = true;
                    //game.game.exitCode==
                }
            } else if (eHandle.keys[Keys.b]) {
                lvl = 0;
                fullscreenError = false;
            }
        }
        if (step < 0) {
            step = max;
        } else if (step > max) {
            step = 0;
        }
        if (innerStep < 0) {
            innerStep = innerMax;
        } else if (innerStep > innerMax) {
            innerStep = 0;
        }
        if (eHandle.release) {
            exit = 0;
        }

        if (innerStep == 2 && diff == 2 && lvl != 0) {
            if (eHandle.keys[Keys.left]) {
                incr--;
            }
            if (eHandle.keys[Keys.right]) {
                incr++;
            }
            eHandle.keys[Keys.right] = false;
            eHandle.keys[Keys.left] = false;
            if (incr > 10) {
                incr = 10;
            }
            if (incr < 5) {
                incr = 5;
            }
        }
        if (lvl != 0 && (eHandle.keys[Keys.a]) || (controll >= 0 && controll <= 6)) {
            if (innerStep == 1 || (controll >= 0 && controll <= 6)) {
                if (eHandle.keys[Keys.a] || eHandle.keys[KeyEvent.VK_ESCAPE]) {
                    eHandle.previus = -1;
                    eHandle.keys[Keys.a] = false;
                    eHandle.keys[KeyEvent.VK_ESCAPE] = false;
                    if (controll == -1) {
                        controll = 0;
                    } else {
                        controll = -1;
                    }
                }
                if (eHandle.previus != -1) {
                    switch (controll) {
                        case 0:				//up key
                            Keys.up = eHandle.previus;
                            controll++;
                            break;
                        case 1:				//Left key
                            Keys.left = eHandle.previus;
                            controll++;
                            break;
                        case 2:				//Down key
                            Keys.down = eHandle.previus;
                            controll++;
                            break;
                        case 3:				//Right key
                            Keys.right = eHandle.previus;
                            controll++;
                            break;
                        case 4:				//A key
                            Keys.a = eHandle.previus;
                            eHandle.keys[Keys.a] = false;
                            controll++;
                            break;
                        case 5:				//B key
                            Keys.b = eHandle.previus;
                            eHandle.keys[Keys.b] = false;
                            controll = -1;
                            break;
                    }
                    eHandle.previus = -1;
                }
            } else if (innerStep == 0 && diff == 0) {
                fullscreenError = true;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (exit != 1 && !gameStarted) {
            proj.paint(g);
            g.setColor(new Color(0xFFFDD0));
            //g.fillRoundRect(2, 2, 400, 400, 25, 25);
            PFont pf = new PFont("The adventures of Dud");
            g.setColor(new Color(100, 100, 0, 100));
            g.fillRect(215 - 4, 110 - 4, 165 + 8, 40 + 8);
            g.setColor(new Color(0xCCFF90));
            g.fillRect(215, 110, 165, 40);
            g.drawImage(img.getImage(), 0, 0, WIDTH,
                    (int) (img.getIconHeight() * ((double) WIDTH / (double) img.getIconWidth())), null);
            pf.SetColor("green").PrintAt(g, 225, 120);
            int x = 30, y = 200;
            if (lvl == 0) {
                g.setColor(new Color(100, 100, 0, 100));
                g.fillRect(x - 25 - 4, y - 20 - 4, 110 + 8, 120 + 8);
                g.setColor(new Color(0xFFFDD0));
                g.fillRect(x - 25, y - 20, 110, 120);
                pf.SetString("START").SetColor("black").PrintAt(g, x, y);
                pf.SetString("TUTORIAL").PrintAt(g, x, y + 20);
                pf.SetString("OPTIONS").PrintAt(g, x, y + 40);
                pf.SetString("QUIT").PrintAt(g, x, y + 60);
            } else if (lvl == 1) {
                g.setColor(new Color(100, 100, 0, 100));
                g.fillRect(x - 25 - 4, y - 20 - 4, 340 + 8, 120 + 8);

                g.setColor(new Color(0xFFFDD0));
                g.fillRect(x - 25, y - 20, 340, 120);

                pf.SetString("TUTORIAL TEXT HERE!!!!").PrintAt(g, x, y);
            } else if (lvl == 2) {
                innerMax = 2;

                if (controll != -1 && innerStep == 1) {
                    g.setColor(new Color(100, 100, 0, 100));
                    g.fillRect(x - 25 - 4, y + 3 - 4, 100 + 8, 40 + 8);

                    g.setColor(new Color(0xFFFDD0));
                    g.fillRect(x - 25, y + 3, 100, 40);

                    String[] contr = new String[]{"UP", "LEFT", "DOWN", "RIGHT", "A - key", "B - key"};
                    pf.SetString(contr[controll]).PrintAt(g, x, y + 20);
                } else {
                    g.setColor(new Color(100, 100, 0, 100));
                    g.fillRect(x - 25 - 4, y - 4, 110 + 8, 90 + 8);
                    g.setColor(new Color(0xFFFDD0));
                    g.fillRect(x - 25, y, 110, 90);
                    if (fullscreenError) {
                        pf.SetColor("red").SetString("FULLSCREEN").PrintAt(g, x, y + 20);
                    } else {
                        pf.SetString("FULLSCREEN").PrintAt(g, x, y + 20);
                    }
                    pf.SetColor("black");
                    pf.SetString("CONTROLS").PrintAt(g, x, y + 40);
                    pf.SetString("LINE SPEED").PrintAt(g, x, y + 60);
                    if (innerStep == 2 && diff == 2) {
                        g.setColor(new Color(100, 100, 0, 100));
                        g.fillRect(x + 85 + 4, y - 4, 30, 90 + 8);
                        g.setColor(new Color(0xFFFDD0));
                        g.fillRect(x + 85, y, 30, 90);
                        pf.SetString(" - " + (double) incr / 10).PrintAt(g, x + 65, y + 60);
                    }
                }
            } else if (lvl == 3) {
                exit = 1;
                exitCode = 1;
            }
            g.setColor(new Color(0x000080));
            //x=20;
            //y=60;
            int offX = 20;
            int dist = 19;
            if (lvl == 0) {
                g.fillPolygon(new int[]{x - offX, x - offX + 5, x - offX}, new int[]{y + step * dist, y + 5 + step * dist, y + 10 + step * dist}, 3);
            } else if (lvl == 2 && controll == -1) {
                g.fillPolygon(new int[]{x - offX, x - offX + 5, x - offX}, new int[]{y + innerStep * dist + 20, y + 5 + innerStep * dist + 20, y + 10 + innerStep * dist + 20}, 3);
            }
        } else {
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

    }
}
