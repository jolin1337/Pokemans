package projekt.story;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import projekt.event.Dialogs;
import projekt.event.GameEventHandler;
import projekt.event.Keys;
import render.PFont;
import render.Player;
import render.Render;
import render.Transition;
/**
 * Denna klassen är en story som skall visas i början av spelet
 * spelaren får en övergripande blick på vem Dud är och vilka problem han måste möta
 * @author johannes
 */
public class Story extends Render{
    public final int WIDTH = 400, HEIGHT = 400;
    
    int time = 0;
    
    /**
     * running är true ända tills Storyn skall stängas av då blir den false
     */
    public boolean running;
    
    /**
     * transition är true när vi skall byta från en text till en annan annars är den false
     */
    public boolean transition = false;
    
    /**
     * step_in_story symboliserar vilken text som skall visas och vilka som har visas dvs vart är historien nu?
     */
    public int step_in_story = 0;
    
    /**
     * strings innehåller alla "sidor" av text
     */
    public String[] strings = Dialogs.Story.story1.split("\t");
    /**
     * strings innehåller nuvarande "sida"
     */
    public PFont currentString = new PFont();
    
    /**
     * gör övergångarna mellan textsidorna
     */
    public Transition t;
    
    private Player dud = new Player(0,0);
    private ImageIcon dudText = new ImageIcon(getClass().getResource("/res/intro/dud.png"));
    
    /**
     * Template bilder för bakgrunden
     */
    ImageIcon[] img = new ImageIcon[]{
        new ImageIcon(getClass().getResource("/res/worlds/world1/layout.png")),
        new ImageIcon(getClass().getResource("/res/worlds/world11/layout.png")),
        new ImageIcon(getClass().getResource("/res/worlds/world1/layout.png")),
        new ImageIcon(getClass().getResource("/res/worlds/world187/layout.png"))
    };
    
    private DisplayPropsFromChar props = new DisplayPropsFromChar( "/res/CharMain/firehero.png", WIDTH /2, HEIGHT/2 );
    
    /**
     * Konstruktorn för story
     * @param run - sätter om introt skall starta direkt?(true) eller inte?(false)
     */
    public Story(boolean run){
        running = run;  // sätter denna klassens funktionalitet i rörelse
        init();         // kör default settings på resten
    }
    
    /**
     * Default Konstruktorn för story
     */
    public Story(){
        running = false;    // fryser denna klassens funktionalitet
        init();             // kör default settings på resten
    }
    
    /**
     * Standard settings 400x400 stor ruta med en svart bakgrund
     */
    private void init(){
        t = new Transition();// init transitions dvs förbereder klassen Transition
        t.Speed = 4;        // sänker hastigheten lite
        
        setSize(400, 400);  // storleken på denna canvas
        setBackground(Color.black); // bakgrunden blir svart
        setForeground(Color.black);
        //setFocusable(false);        //
        
        running = true;
        currentString.setPreferedWidth(200);
        currentString.SetString(strings[step_in_story]);
        
        dud.setChar("/res/CharMain/firehero.png");
        props.setName("DUD");
        props.setDisplayName(dudText);
        props.gameEnabled=false;
    }
    
    /**
     * Denna funktionen anropas sekventiellt med nuvarande nertryckta tangenter
     * @param keys är en array av alla tangenter som är möjliga att tryckas ner om keys[i] == true så trycker spelaren ner tangenten med ascii värdet i 
     */
    @Override
    public void tick(boolean[] keys){
        if( keys[Keys.a]){
            transition = true;
        }
        if( keys[Keys.a] && step_in_story + 1 == strings.length){
            running = false;
        }
    }
           
    
    /**
     * så länge running är true så skriver den ut en story
     * @param g canvas att måla på
     */ 
    @Override
    public void paint(Graphics g) {
        if(strings.length -1 > step_in_story){
            if(t.dirBool && t.index == 90- (90 % 4)){   // half way there
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                }
                step_in_story ++;
                currentString.SetString(strings[step_in_story]);
                time=0;
            }
            g.drawImage(img[step_in_story].getImage(), 100, 0, null);
            g.setColor(new Color(0,0,0,200));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.fillRect(0, HEIGHT/2 - 200/2 + 10, time -20, 190);
            g.setColor(new Color(0,240,0,20));
            g.fillRect(0, HEIGHT/2 - 200/2 + 10, time -20, 190);
            for(int i=245;i < time*10;i+=10){
                if(i>= 255 * 10) 
                    g.setColor(new Color(255, 255 - 20,0,200));
                else
                    g.setColor(new Color(i/10,i/10 - 20,0,200));
                g.fillRect((i-240)/10, HEIGHT/2 - 200/2 +20, 1,170);
            }
            //g.drawString(">", 30,  20 + HEIGHT - (int)((time + 100)*(Math.sin(time/2*Math.PI/180))));
            int y=18 + HEIGHT - (int)((time + 100)*(Math.sin(time/2*Math.PI/180)));
           // g.fillRect(10, y - 5, 10, 10);
            g.setColor(new Color(0,240,0));
            
            currentString.PrintAt(g, time -220, 140 );
            if (t.index >= 0 && transition == true)         // do animate
                t.Transitions[Transition.Type.Fade].animate(g);
            if((t.index >= 180 - (180 % t.Speed) && t.dirBool ) || ( t.index == 0 && transition ) )                // cancel animations
                transition=false;
            if(time/2 < 130)
                time +=10;
            else{
                int b=(int)(System.currentTimeMillis())%600;
                if(b*-1 <= 300)b=0;
                else b=255;
                g.setColor(new Color(0,180,0,b));
                g.fillPolygon(new int[]{    // xPoints
                    30,
                    20,
                    20
                },new int[]{                // yPoints
                    y,
                    y - 10,
                    y + 10
                },3);
            }
        }
        else{
            g.drawImage(img[step_in_story].getImage(), WIDTH/2-img[step_in_story].getIconWidth()/2, HEIGHT/2-img[step_in_story].getIconHeight()/2, null);
            
            props.paint(g);
        }
    }
    
    public static void main(String[] args) {
        JFrame f=new JFrame("Pokemans - Story");
        Story s = new Story();
        f.setBounds(0, 0, s.WIDTH, s.HEIGHT);
        f.setResizable(false);
        f.add(s);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.show();
        GameEventHandler e= new GameEventHandler();
        f.addKeyListener(e);
        s.addKeyListener(e);
        while(s.running){
            s.tick(e.keys);
            s.repaint();
        }
        f.dispose();
    }
}
