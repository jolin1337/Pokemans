package projekt.story;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import render.PFont;
import render.Player;

/**
 * DisplayPropsFromChar klassen är till för att temporärt kolla vad för stats din karacktär har för tillfälligt
 * @author johannes
 */
public final class DisplayPropsFromChar {
    
    private Player Char = new Player(0, 0, 100);
    private boolean loaded = false;
    private int counter = 0;
    private PFont drawTexts = new PFont();
    public boolean gameEnabled =true;
    public boolean hasManipualted = false;
    
    public int curpos = 0;
    
    public DisplayPropsFromChar(){
        init( null, 0, 0 );
    }
    
    public DisplayPropsFromChar(String url){
        init( url, 0, 0 );
    }
    
    public DisplayPropsFromChar(String url, int x, int y){
        init(url,x,y);
    }
    
    public DisplayPropsFromChar(int x, int y){
        init(null,x,y);
    }
    
    protected final void init(String url, int x, int y){
        Char.incr = 1.0;
        
        setChar(url);
        Char.x=x;
        Char.y=y;
    }
    
    public final boolean setChar(String url){
        Char.setChar(url);
        if(Char.c != null){
            loaded = true;
            return true;
        }
        loaded = false;
        return false;
    }
    
    public final boolean isDisplayable(){
        return loaded;
    }
    
    public final void paint(Graphics g){
        if(g==null)return;
        int radius =16;
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0, 0, (Char.x + 25)*2, Char.y*2);
        
        try{
            g.drawImage(Char.displayName, Char.x - Char.displayName.getWidth(null)/2 + 25, 
                    Char.y - Char.displayName.getHeight(null)/2 - 100, null);
        }catch(Throwable t){
            drawTexts.SetColor("white").SetString(Char.name).PrintAt(g, Char.x, 50);
        }
        
        //BufferedImage tool = Char.c.getSubimage(radius * (int) (Char.incr > 1.0 && Char.frame != 0 ? (Char.frame + 4) : Char.frame), 20 * Char.direciton, radius, 20);
        //g.drawImage(tool, Char.x - 25, Char.y - 25,100,100, null);
        Char.drawCharCenter(g, Char.x + 100 + 25, Char.y + 100 + 25);
        if(counter == 0) {
            if( Char.direciton == 1)
                Char.direciton = 0;
            else if( Char.direciton == 0)
                Char.direciton = 2;
            else if( Char.direciton == 2)
                Char.direciton = 3;
            else if( Char.direciton == 3)
                Char.direciton = 1;
        }
        else if(counter%10 == 1)Char.frame++;
        
        counter++;
        if ( counter >= 100)counter = 0;
        
        g.setColor(Color.white);
        drawTexts.SetColor("Black").SetString("Strength, "+Char.lvl).PrintAt(g, Char.x - 140, Char.y);
        drawTexts.SetColor("green").SetString("Health, "+Char.health).PrintAt(g, Char.x - 140, Char.y + 40);
        drawTexts.SetColor("green").SetString("Something1, "+Char.lvl).PrintAt(g, Char.x + 80, Char.y);
        drawTexts.SetColor("Black").SetString("Something2, "+Char.health).PrintAt(g, Char.x + 80, Char.y + 40);
        
        if( !hasManipualted && gameEnabled ){
            if(gameEnabled){
                drawTexts.SetColor("white").SetString("Manipulate").PrintAt(g, Char.x - drawTexts.getStringLength()/2, Char.y + 100);
                drawTexts.SetColor("red").SetString("\"press a\"").PrintAt(g, Char.x - drawTexts.getStringLength()/2 + 70, Char.y + 100);
            }
        }
        else if(gameEnabled){
            int[] curpos[] = {new int[]{Char.x - 160, Char.y}, new int[]{Char.x - 160, Char.y + 40}, new int[]{Char.x + 60, Char.y}, new int[]{Char.x + 60, Char.y + 40}};
            g.fillPolygon(new int[]{curpos[this.curpos][0], curpos[this.curpos][0] + 9, curpos[this.curpos][0]}, new int[]{curpos[this.curpos][1], curpos[this.curpos][1] + 9, curpos[this.curpos][1] + 18}, 3);
            
            drawTexts.SetColor("white").SetString("Use: ").PrintAt(g, Char.x - 140, Char.y + 100);
            drawTexts.SetColor("red").SetString("arrow keys").PrintAt(g, Char.x - 110, Char.y + 100);
            drawTexts.SetColor("white").SetString("to manipulate the players stats").PrintAt(g, Char.x - 30, Char.y + 100);
            
            if(Char.kills > 0)
                drawTexts.SetColor("black").SetString("You have " + Char.kills +  " points left to spend").PrintAt(g, Char.x - 110, Char.y + 120);
            else
                drawTexts.SetColor("black").SetString("You have no points to spend").PrintAt(g, Char.x - 80, Char.y + 120);
        }
        
        if ((int) Char.direciton > 3) {
            Char.direciton = 0;
        }
        if ((int) Char.frame > 3) {
            Char.frame = 0;
        }
    }

    public final void setName(String duD) {
        Char.name = duD;
    }

    public void setDisplayName(ImageIcon dudText) {
        Char.displayName = dudText.getImage();
    }
}
