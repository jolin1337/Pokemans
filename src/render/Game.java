package render;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import projekt.Runner;
import projekt.event.Dialogs;
import projekt.event.Keys;
import projekt.story.DisplayPropsFromChar;

/**
 * Game är en subklass av Render vilket innebär att det är här all grafik
 * egentligen skrivs ut på skärmen
 *
 * @author johannes
 */
public class Game extends Render {

    public boolean loadWorld = false;
    public Point screenCoords = new Point(0, 0);
    private DisplayPropsFromChar props = new DisplayPropsFromChar( "/res/CharMain/firehero.png", WIDTH /2 - 25, HEIGHT/2 );
    public boolean paintProps = false;
    private PFont drawFont = new PFont("");
    
    /**
     * världens nuvarande egenskaper sparas och ändras i denna variabel.
     */
    public World world;
    /**
     * focus innehåller information om var karaktären befinner sig och hur
     * världen skall relatera sig till var karaktären är(görs ex med att sätta
     * world.setOffset(int x,int y)
     */
    public Player focus;
    public BufferedImage before;

    /**
     * konstruktorn för spelet
     */
    public Game() {
        props.setName("Dud");
        props.setDisplayName(new ImageIcon(getClass().getResource("/res/intro/dud.png")));
        
        focus = new Player(15, 17);
        focus.setChar("/res/CharMain/firehero.png");
        focus.name="Dud";
        
        this.world = new World("/res/worlds/world1");
        focus.setOnWalkCallback(new OnWalkCallback() {

            public boolean onWalk() {
                return true;
            }

            public boolean onStartWalk() {
                return false;
            }

            public boolean onEndWalk() {
                if (!Game.this.focus.action.isEmpty()) {
                    if(Game.this.focus.action.equals("dialog-fight"))
                        Game.this.fight = true;
                    else
                        Game.this.focus.freeze = false;
                    Game.this.focus.action = "";
                    
                }
                return true;
            }
        });
    }
    int delay = 0;
    private long millidelay;
    public boolean fight = false;
    public Insets ins = null;

    /**
     * tick är medlem av Render och anropas varje loopintervall och här kollar
     * vi då om det har tryckts ner någon tangent
     *
     * @param k boolean[]
     */
    @Override
    public void tick(boolean[] k) {
        boolean left = (k[Keys.left] || k[KeyEvent.VK_A]) && !focus.freeze;
        boolean right = (k[Keys.right] || k[KeyEvent.VK_D]) && !focus.freeze;
        boolean up = (k[Keys.up] || k[KeyEvent.VK_W]) && !focus.freeze;
        boolean down = (k[Keys.down] || k[KeyEvent.VK_S]) && !focus.freeze;
        boolean a = (k[Keys.a]);
        boolean b = (k[Keys.b]);
        boolean select = (k[Keys.select]);

        if (k[Keys.esc] || this.exitCode == 1) {
            this.exitCode = 1;
            return;
        }
        if (b) {
            focus.incr = 2;
        } else {
            focus.incr = 1;
        }

        if (!(left || right || up || down)) {
            //**** Delayen ****//
            delay++;
        } else {
            delay = 0;
        }
        if (delay >= radius) {
            this.focus.frame = 0;
        }
        //**** pickup ****//
        if (a) {
            if (Dialogs.endof && this.preformAction(false)) {
                millidelay = System.currentTimeMillis();
            }
            if (!Dialogs.endof) {//if(millidelay != 0 && System.currentTimeMillis() - millidelay > 100) {
                k[Keys.a] = false;
                this.drawDialog(Dialogs.nextMessage());
                if (Dialogs.endof) {
                    this.focus.onWalkCallback.onEndWalk();
                    this.preformAction(true);
                }
                millidelay = 0;
            }
            millidelay = System.currentTimeMillis();

        }




        //X-axeln
        if (this.focus.y - this.focus.y2 / radius == 0) {
            if ((left && !right) && (this.focus.x - this.focus.x2 / radius >= 0) && focus.x >= 1) {
                if (this.world.canGo(this.focus.x - 1, this.focus.y)) {
                    focus.x--;
                }
                focus.direciton = 1;
            }
            if ((right && !left) && (this.focus.x - this.focus.x2 / radius <= 0) && focus.x2 < world.width * radius - radius) {
                if (this.world.canGo(this.focus.x + 1, this.focus.y)) {
                    focus.x++;
                }
                focus.direciton = 2;
            }
        }
        //Y-axeln
        if (this.focus.x - this.focus.x2 / radius == 0) {
            if ((up && !down) && (this.focus.y - this.focus.y2 / radius >= 0) && focus.y > 1) {
                if (this.world.canGo(this.focus.x, this.focus.y - 1)) {
                    focus.y--;
                }
                focus.direciton = 3;
            }
            if ((down && !up) && (this.focus.y - this.focus.y2 / radius <= 0) && focus.y >= 1 && focus.y2 < world.height * radius - radius) {
                if (this.world.canGo(this.focus.x, this.focus.y + 1)) {
                    focus.y++;
                }
                focus.direciton = 0;
            }
        }
        /*
         * Directions!!!
         */
        if ((this.focus.x) * radius - (this.focus.x2) == 0 && !up && !down) {
            this.focus.stand = false;
            if ((left && !right)) {
                //focus.direciton = 1;
            }
            if ((right && !left)) {
                //focus.direciton = 2;
            }
        } else if ((this.focus.y) - (this.focus.y2) / radius == 0 && !right && !up) {
            if ((up && !down)) {
                //focus.direciton = 3;
            }
            if ((down && !up)) {
                //focus.direciton = 0;
            }
        }
        
        if( select ){
            paintProps=!paintProps;
            k[Keys.select] = false;
        }
    }

    private boolean preformAction(boolean pickNow) {
        int direction = this.focus.direciton;
        int x = 0, y = 0;
        if (direction == 1 || direction == 2) {
            x = (int) direction * 2 - 3;
        } else if (direction == 3 || direction == 0) {
            y = (-2 * direction) / 3 + 1;
        }
        Color temp;
        try {
            x=(int) this.focus.x2 / radius + x;
            y=(int) this.focus.y2 / radius + y;
            temp = this.world.getRGBA(x,y);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            // plockar upp något utanför banans kanter
            return false;
        }
        if (temp.getRed() == 0 && !this.world.isPoortal(x,y) && !this.world.isWorldRise(x, y)
                && !(temp.equals(new Color(0xff000000)) || temp.equals(new Color(0xffffffff)))) {
            if (Dialogs.endof && !pickNow) {
                this.focus.action = "dialog";
                Player p = this.world.getPlayer(x, y);
                Dialogs.initDialog("\t"+Dialogs.itemDialog[p.lvl]);
                this.focus.freeze = true;
            }
            if (!pickNow) {
                return true;
            }
            Graphics2D ag = this.world.alpha.createGraphics();
            ag.setColor(Color.white);
            ag.fillRect(x, y, 1, 1);

            ag = this.world.items.createGraphics();
            AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
            Composite c = ag.getComposite();
            ag.setComposite(composite);
            ag.setColor(new Color(0, 0, 0, 0));
            ag.fillRect(((int) this.focus.x2 + (int)(x - this.focus.x2/radius) * radius), ((int) this.focus.y2 + (int)(y - this.focus.y2/radius) * radius) - 8, radius, 20);
            ag.setComposite(c);
            int b = temp.getBlue();
            if (b != 0) {
                this.focus.addItem(b);
            }
            Player p = this.world.getPlayer(x, y);
            if(p.log.isEmpty())
                p.log="hide";
            else p.log += " hide";
            return true;
        } else if (temp.getRed() != 0 && !this.world.isPoortal(x,y) && !this.world.isWorldRise(x, y)
                && !(temp.equals(new Color(0xff000000)) || temp.equals(new Color(0xffffffff)))) {		// omPlayer
            if (Dialogs.endof && !pickNow) {
                Player cur = world.getPlayer(x,y);
                this.focus.action = (cur.lvl%2 == 0?"dialog":"dialog-fight");
                //fight = true;
                if (cur.x > focus.x) {
                    cur.direciton = 1;//Vänster
                } else if (cur.x != focus.x) {
                    cur.direciton = 2;//höger
                }
                if (cur.y > focus.y) {
                    cur.direciton = 3; //upp
                } else if (cur.y != focus.y) {
                    cur.direciton = 0; //ner
                }
                try{
                    Dialogs.initDialog("\t"+Dialogs.characterDialog[cur.lvl]);
                }catch(IndexOutOfBoundsException e){
                    Dialogs.endof=true;
                    Dialogs.nextMessage();
                    this.focus.action = "";
                    return false;
                }
                this.focus.freeze = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Denna funktionen ritar allt i hela spelet (inklusive det world genererar)
     * som en bild
     *
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        if(g==null)return;
        if (tr == null) {
            tr = new Transition();
            tr.Speed = 6;
        }
        if ((focus.x * focus.radius != focus.x2)) {
            focus.slowMove(0);
        } else if (focus.y * focus.radius != focus.y2) {
            focus.slowMove(1);
        }
        int frx = this.focus.x;
        int fry = this.focus.y;
        checkWarp();
        try {
            world.paint(g, (int) this.focus.x2, (int) this.focus.y2, WIDTH, HEIGHT);
        } catch (ArrayIndexOutOfBoundsException e) { // positionen är utanför kartan
            ErrorHandler.CharacterBoundary.CharacterOutOfBoundary();
            ErrorHandler.CharacterBoundary.resetCharacterPositionAt(this.focus, frx - 1, fry + 1);
        }
        try {
            if ((int) focus.frame > 3) {
                focus.frame = 0;
            }
            BufferedImage t = focus.c.getSubimage(radius * (int) (focus.incr > 1.0 && focus.frame != 0 ? (focus.frame + 4) : focus.frame), 20 * focus.direciton, radius, 20);
            g.drawImage(t, WIDTH / 2 - t.getWidth() / 2 - radius / 2 + radius, HEIGHT / 2 - t.getHeight() - radius / 5 + radius, this);

        } catch (java.awt.image.RasterFormatException e) {//om bilden clips utanför bredden, höjden eller är negativ 
        }
        world.paintTop(g, (int) this.focus.x2, (int) this.focus.y2, WIDTH, HEIGHT);
        //drawShadowWithString("Version: \u03B1 0.5", 2, 12, Color.white, new Color(0x666666));
        //new PFont("Alpha - version 0.5", g, 2, 12);
        if (this.focus.action.startsWith("dialog")) {
            g.setColor(new Color(0x666666));
            //g.setFont(new Font("Lucida Typewriter Regular", Font.BOLD, 12));
            try {
                this.drawDialog(Dialogs.message[Dialogs.getIndex()]);
            } catch (NullPointerException e) {
            }
        }
        if (tr.index > 0) {
            if (tr.dirBool) {
                //dbg.drawImage(before, 0, 0, this);
            }
            tr.Transitions[Transition.Type.Fade].animate(g);
        }
        focus.freeze = focus.action.startsWith("dialog") || tr.index > 0 || paintProps;
        
        if(paintProps){
            props.paint(dbg);
        }
    }

    public void drawDialog(String message) {
        int b = 5;
        int m = 8;
        int y = HEIGHT / 4;
        this.dbg.fillRect(0, y * 3, WIDTH, y );
        this.dbg.setColor(Color.black);
        this.dbg.drawRect(b, y * 3 + b, WIDTH - 3 * b, (int)(y - ( projekt.Menu.Applet ? 3.4 : 7  ) * b) );

        //this.dbg.setFont(new Font(Font.DIALOG, Font.BOLD, 10));
        //String[] lines = message.split("\n");
        /*
         * for (int i = 0; i < lines.length; i++) { FontMetrics fm =
         * this.getFontMetrics(this.dbg.getFont()); int width =
         * fm.stringWidth(lines[i]); if (width < this.getWidth() - (2 * b))
         */
        drawFont.SetString(message).PrintAt(this.dbg, b + b, 3 * y + b + m);
        //}
    }

    public void drawShadowWithString(String string, int x, int y, Color c1, Color c2) {
        this.dbg.setColor(c2);
        this.dbg.drawString(string, x + 1, y);
        this.dbg.drawString(string, x - 1, y);
        this.dbg.drawString(string, x, y + 1);
        this.dbg.drawString(string, x, y - 1);
        this.dbg.setColor(c1);
        this.dbg.drawString(string, x, y);
    }

    public void checkWarp() {
        int frx = this.focus.x;
        int fry = this.focus.y;
        boolean fading = false;
        if (world.isPoortal((int) (this.focus.x2 / radius - 0.1 + 1), (int) (this.focus.y2 / radius - 0.1 + 1)) && !this.focus.freeze) {
            loadWorld = true;
            focus.action = "world";
            focus.freeze = true;

            try {
                if (ins != null) {
                    Robot rbt = new Robot();
                    before = rbt.createScreenCapture(new Rectangle(screenCoords.x + ins.left, screenCoords.y + ins.top, 406, 400));// 2,35
                }
            } catch (AWTException ex) {
            }
            tr.Transitions[Transition.Type.Fade].animate(dbg);
        }
        if (loadWorld && tr.dirBool) {
            loadWorld = false;
            try {
                try {
                    if(this.focus.getWorldFromPath(this.world.path) == -1)
                        this.focus.addWorld(this.world.copy());
                } catch (CloneNotSupportedException ex) {
                    //Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                String pa = "";
                try {
                    pa = getClass().getResource("/res/worlds/world" + world.pos[2]).getPath();
                } catch (NullPointerException e) {
                    this.exitCode = 1;
                    System.out.println("unable to locate the world: /res/worlds/world" + world.pos[2]);
                }
                int x = this.world.pos[0];
                int y = this.world.pos[1];
                if (this.focus.getWorldFromPath(pa) == -1) {
                    this.world = new World(pa);
                } else {

                    this.world.pos[0] = -1;
                    this.world.pos[1] = -1;
                    this.world.pos[2] = -1;
                    this.world = this.focus.getWorld(this.focus.getWorldFromPath(pa));
                }
                cleanup();
                this.focus.transport(x, y); // flytta playern till x,y coord
                if (this.world.isPoortal(x, y)) {
                    try {
                        if (this.world.canGo(this.focus.x, this.focus.y + 1) && !this.world.isPoortal(this.focus.x, this.focus.y + 1)) {
                            this.focus.y++;
                        } else {
                            throw new Error("" + (this.focus.y + 1));
                        }
                    } catch (Throwable t1) {
                        try {
                            if (this.world.canGo(this.focus.x, this.focus.y - 1) && !this.world.isPoortal(this.focus.x, this.focus.y - 1)) {
                                this.focus.y--;
                            } else {
                                throw new Error("" + (this.focus.y - 1));
                            }
                        } catch (Throwable t2) {
                            try {
                                if (this.world.canGo(this.focus.x - 1, this.focus.y) && !this.world.isPoortal(this.focus.x - 1, this.focus.y)) {
                                    this.focus.x--;
                                } else {
                                    throw new Error("" + (this.focus.y - 1));
                                }
                            } catch (Throwable t3) {
                                if (this.world.canGo(this.focus.x + 1, this.focus.y) && !this.world.isPoortal(this.focus.x + 1, this.focus.y)) {
                                    this.focus.x++;
                                } else {
                                    throw new ArrayIndexOutOfBoundsException();
                                }
                            }
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException b) {
                ErrorHandler.CharacterBoundary.CharacterOutOfBoundary();
                ErrorHandler.CharacterBoundary.resetCharacterPositionAt(this.focus, frx - 1, fry + 1);
                this.focus.freeze = false;
            }
        }
    }
    public void cleanup(){
        // när du är i första världen så måste vi ränsa lite saker
        if(this.world.path.contains("world1") && !this.world.path.contains("world11")) {
            
            // remove character 
            this.world.removePlayer(this.world.getPlayer(12, 17));
            
            // redraw alpha map
            Graphics2D ag = this.world.alpha.createGraphics();
            ag.setColor(Color.white);
            ag.fillRect(12, 17, 1, 1);
            
            // reseting health
            this.focus.health = 100;
        }
    }
}
