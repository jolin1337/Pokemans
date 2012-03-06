package render;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import render.OnWalkCallback;
import render.World;

/**
 * denna funktionen konrollerar karaktärens rörelser och data ex Grafik på hur karaktären skall se ut i olika vinlklar osv
 * @author johannes
 */
public class Player {

    public OnWalkCallback onWalkCallback = null;
    public boolean freeze = false;
    /**
     * en string som beskriver olika händelser som karaktären gör i spelet
     */
    public String log = "";
    /**
     * karaktärens punkt som den skall gå till i x
     */
    public int x = 0;
    /**
     * karaktärens punkt som den skall gå till i y
     */
    public int y = 0;
    /**
     * karaktärens punkt som den nuvarnade befinner sig på i x led
     */
    protected float x2 = 0;
    /**
     * karaktärens punkt som den nuvarnade befinner sig på i y led
     */
    protected float y2 = 0;
    /**
     * definierar hur stor area karaktären tar upp (kvadratisk)
     */
    protected int radius = 10;
    /**
     * en array av alla items karaktären har plockat upp
     */
    private int[] items;
    private ArrayList<World> worlds;
    private String[] fights;
    /**
     * direction säger åt vilket håll som karaktären är påväg emot
     */
    public int direciton;
    /**
     * c innehåller alla bilder på karaktären
     */
    public BufferedImage c;
    public BufferedImage cr;
    public Image displayName;
    /**
     * avgör hur snabb karaktären förflyttarsig mellan ruta till ruta
     */
    public double incr = 1;
    /**
     * blir 1 eller 2 om det händer någonting med förflyttningen av gubben
     */
    int bug = -1;
    /**
     * animationen på vilken bild som skall visas
     */
    public boolean stand = true;
    public float frame = 0;
    public int health = 100, maxHealth = 100;
    public String name = "RANDOM";
    public int lvl = 1;
    /**
     * om det pågår en dialog med karaktären
     */
    String action = "";

    /**
     * Konstruktor för Player som tar emot x,y-position. Hämtar röd & blå-värde i b & r.
     * @param xpos
     * @param ypos
     * @param r
     * @param b 
     */
    public Player(int xpos, int ypos, int r, int b) {
        radius = 16;
        x = xpos;
        y = ypos;
        x2 = x * 16;
        y2 = y * 16;
        items = new int[255];
    }

    /**
     * konstruktorn för Player har 3 arg x är start positionen i x led och arg y är startpositionen i y led medans arg r är stroleken på karaktärens ruta
     * @param x int
     * @param y int
     * @param r int
     */
    public Player(int x, int y) {
        int r = 16;
        this.x = x;
        this.y = y;
        this.x2 = x * r;
        this.y2 = y * r;
        this.radius = r;
        this.items = new int[255];
        this.worlds = new ArrayList<World>();
    }

    public boolean addItem(int b) {
        int i = 0;
        while (i < 255 && this.items[i] != 0) {
            i++;
        }
        if (i < 255 && i == 0) {
            this.items[i] = b;
        } else {
            return false;
        }
        return true;
    }

    public boolean deleteItem(int i) {
        if (this.items[i] == 0) {
            return false;
        }
        this.items[i] = 0;
        return true;
    }

    public int getItem(int i) {
        return this.items[i];
    }

    public int relplaceItem(int i, int b) {
        int cur = this.items[i];
        this.items[i] = b;
        return cur;
    }

    /**
     * initialiserar all Grafik på karaktären med hjäl utav en string som innehåller alla path:er till bilderna
     * @param img array av strings
     */
    public Player setChar(String img) {
        ImageIcon t = new ImageIcon(getClass().getResource(img));
        c = new BufferedImage(t.getIconWidth(), t.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        c.getGraphics().drawImage(t.getImage(), 0, 0, null);
        return this;
    }

    public Player setChar(String img, int width, int height) {
        ImageIcon t = new ImageIcon(getClass().getResource(img));
        c = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        c.getGraphics().drawImage(t.getImage(), 0, 0, null);
        return this;
    }
    
    public Player setChar(BufferedImage img) {
        c = img;
        return this;
    }

    public void setRunningPose(String img) {
        ImageIcon t = new ImageIcon(getClass().getResource(img));
        cr = new BufferedImage(t.getIconWidth(), t.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        cr.getGraphics().drawImage(t.getImage(), 0, 0, null);
    }

    /**
     * clear avrundar karaktärens nuvarande position så att det blir ett heltal
     */
    public void clear() {
        this.x2 = (int) (this.x2 + 0.5);
        this.y2 = (int) (this.y2 + 0.5);
    }

    /**
     * avrundar s till närmsta heltal
     * @param s talet
     * @return det avrundade talet
     */
    public int round(double s) {
        if (s % 1 < 0.5) {
            return (int) (s + 0.5);
        }
        return (int) (s);
    }

    /**
     * Clearar karaktärens nuvarande position på en specifik axel s=0 x-axen s=1 y-axen s=-1 inget annars båda
     * @see #clear()
     * @param s avgör vilken axel du skall cleara karaktären på
     */
    public void clear(int s) {
        if (s == 0) {
            this.x2 = this.round(this.x2);
        } else if (s == 1) {
            this.y2 = this.round(this.y2);
        } else {
            this.x2 = this.round(this.x2);
            this.y2 = this.round(this.y2);
        }
    }

    /**
     *  rör sakta karaktären mot this.x och this.y
     * @param res avgör vilken axel du skall flytta karaktären på 
     */
    public void slowMove(int res) {
        //System.out.println(this.x*radius+","+(int)this.x2+";"+this.y*radius+","+(int)this.y2);
        if ((this.x * radius == ((int) this.x2 + 1) || this.y * radius == ((int) this.y2) + 1)) {
            //this.clear();
            //this.frame=0;
            x2 = x * radius;
            y2 = y * radius;
            this.onWalkCallback.onEndWalk();
            //return;
        }
        this.onWalkCallback.onWalk();
        double in = 0.09 * incr;
        if (res == -1) {
            return;
        } else if (res == 0) {
            if (this.x * radius > this.x2) {
                this.x2 += incr;
                this.direciton = 2;
                frame += in;
            } else if (this.x * radius < this.x2) {
                this.x2 -= incr;
                this.direciton = 1;
                frame += in;
            }
        } else if (res == 1) {
            if (this.y * radius > this.y2) {
                this.y2 += incr;
                this.direciton = 0;
                frame += in;
            } else if (this.y * radius < this.y2) {
                this.y2 -= incr;
                this.direciton = 3;
                frame += in;
            }
        } else {
            if (this.x * radius > this.x2) {
                this.x2 += incr;
                //this.direciton=2;
            } else if (this.x * radius < this.x2) {
                this.x2 -= incr;
                //this.direciton=0;
            }
            if (this.y * radius > this.y2) {
                this.y2 += incr;
                //this.direciton=3;
            } else if (this.y * radius < this.y2) {
                this.y2 -= incr;
                //this.direciton=1;
            }
        }
    }

    /**
     * sätter positionen av karaktären utan smothness
     * @param res avgör vilken axel du skall flytta karaktären på
     */
    public void staticMove(int res) {
        if (this.x * radius == (int) (this.x2) && this.y * radius == (int) (this.y2)) {
            this.clear();
            return;
        }
        if (res == -1) {
            return;
        } else if (res == 0) {
            if (this.x * radius > this.x2) {
                this.x2 += radius;
            } else if (this.x * radius < this.x2) {
                this.x2 -= radius;
            }
        } else if (res == 1) {
            if (this.y * radius > this.y2) {
                this.y2 += radius;
            } else if (this.y * radius < this.y2) {
                this.y2 -= radius;
            }
        } else {
            if (this.x * radius > this.x2) {
                this.x2 += radius;
            } else if (this.x * radius < this.x2) {
                this.x2 -= radius;
            }
            if (this.y * radius > this.y2) {
                this.y2 += radius;
            } else if (this.y * radius < this.y2) {
                this.y2 -= radius;
            }
        }
        this.clear(res);
    }

    public void transport(int x, int y) {
        this.x = x;
        this.x2 = x * this.radius;
        this.y = y;
        this.y2 = y * this.radius;
    }

    public boolean onPosition(int x, int y) {
        return (this.x == x && this.y == y);
    }

    public boolean teleport(int xstart, int ystart, int xstop, int ystop) {
        if (this.x == xstart && this.y == ystart) {
            this.x = xstop;
            this.y = ystop;
            this.staticMove(0);
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public float getX2() {
        return this.x2;
    }

    /**
     * @return
     */
    public float getY2() {
        return this.y2;
    }

    /**
     * @param x positionen på x
     */
    public void setX2(float x) {
        this.x2 = x;
    }

    /**
     * @param y positionen på y
     */
    public void setY2(float y) {
        this.y2 = y;
    }

    boolean addWorld(World world) {
        int ex = this.getWorldFromPath(world.path);
        if (ex == -1) {
            this.worlds.add(world);
            return true;
        }
        this.worlds.set(ex, world);
        return false;
    }

    public World getWorld(int index) {
        return this.worlds.get(index);
    }

    public int getWorldFromPath(String p) {
        int length = this.worlds.size();
        for (int i = 0; i < length; i++) {
            if (this.worlds.get(i).path.equals(p)) {
                return i;
            }
        }
        return -1;
    }

    public void setOnWalkCallback(OnWalkCallback callback) {
        this.onWalkCallback = callback;
    }

    void drawChar(Graphics g) {
            if((int)frame > 3)frame=0;
            BufferedImage t;
                
            try{
                t = c.getSubimage(radius * (int) (incr > 1.0 && frame != 0 ? (frame + 3)  : frame  ), 20 * direciton, radius, 20);
            }catch(Exception e){
                t = c.getSubimage( 0, 0, c.getWidth(), c.getHeight() );
            }
            g.drawImage(t, (int)x2, (int)y2-(t.getHeight() == 16?0:7), null);
    }
}