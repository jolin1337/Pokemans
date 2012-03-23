/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import projekt.event.EventHandler;
import projekt.event.Keys;

/**
 *
 * @author Johan Lindskogen
 */
public class Battle extends Render {

    Image dbImage;
    Graphics dbg;
    int menupos = 0;
    public Player me, you;
    
    String[] menuOptions = {"ATTACK","ITEMS","MAGIC","RUN"};
    String actionText;
    boolean subMenu = false;
    private boolean you_turn;
    
    protected boolean temp_var = false; 

    public Battle(Player c1, Player c2) {
        me = c1;
        you = c2;
        you_turn = Math.random()<0.5?true:false;
        actionText = "What will "+(you_turn?you.name:me.name)+" do?";
        setSize(400, 400);
        setBackground(Color.white);
        setFocusable(false);
        /*
         * while (true) { Update(getGraphics()); }
         */
    }

    public void tick(boolean[] keys) {
        if( you_turn ){
            keys[Keys.a] = true;
            menupos = 0;
            if( subMenu )
                menupos = Math.round((float)Math.random()*3); // 0 <= menupos <= 3
        }
        if (keys[Keys.up]) {
            if (menupos == 2 && !menuOptions[0].equals("-")) {
                menupos = 0;
            } else if (menupos == 3 && !menuOptions[1].equals("-")) {
                menupos = 1;
            }
        }
        if (keys[Keys.down]) {
            if (menupos == 0 && !menuOptions[2].equals("-")) {
                menupos = 2;
            } else if (menupos == 1 && !menuOptions[3].equals("-")) {
                menupos = 3;
            }
        }
        if (keys[Keys.left]) {
            if (menupos == 1 && !menuOptions[0].equals("-")) {
                menupos = 0;
            } else if (menupos == 3 && !menuOptions[2].equals("-")) {
                menupos = 2;
            }
        }
        if (keys[Keys.right]) {
            if (menupos == 0 && !menuOptions[1].equals("-")) {
                menupos = 1;
            } else if (menupos == 2 && !menuOptions[3].equals("-")) {
                menupos = 3;
            }
        }
        if (keys[Keys.a] && !temp_var || keys[Keys.a] && you_turn){
            Action(menupos);
            temp_var = true;
        }
        if (!keys[Keys.a] && temp_var)
            temp_var = false;
            
        
        if (subMenu && keys[Keys.b])
            Action(-1);
    }

    private void Update(Graphics g) {
        if (dbImage == null || dbImage.getWidth(this) != this.getSize().width) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        dbg.setColor(getForeground());
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        Color blk = new Color(0x444444),
                ylw = new Color(0xF8F8D8),
                hltgrn = new Color(0x70F8A8),
                hltylw = new Color(0xF8E038),
                hltred = new Color(0xF85838);

        int x = 270, y = 200;
        double meRatio = (double) me.health / (double) me.maxHealth;
        double youRatio = (double) you.health / (double) you.maxHealth;
        PFont normal = new PFont("RANDOM1"),
                battle = new PFont("RANDOM2");
        normal.SetColor("black");
        battle.SetColor("black");
        g.setColor(blk);
        g.fillRoundRect(10, 10, 120, 34, 5, 5); //Motståndarens bakgrundsruta
        g.fillRoundRect(270, 200, 120, 44, 5, 5);	//Spelarens bakgrundsruta
        g.setColor(ylw);
        g.fillRoundRect(12, 12, 116, 30, 5, 5); //Motståndarens förgrundsruta
        g.fillRoundRect(x + 2, y + 2, 116, 40, 5, 5); //Spelarens förgrundsruta

        //Motståndarens healthbar
        g.setColor(blk);
        normal.SetString(you.name).PrintAt(g, 20, 17);
        normal.SetString("Lv " + you.lvl).PrintAt(g, 97, 17);
        g.fillRect(20, 30, 104, 6);
        g.setColor((youRatio > 0.5 ? hltgrn : (youRatio > 0.1 ? hltylw : hltred)));
        g.fillRect(22, 32, (int) Math.round(youRatio * 100), 2);

        //Spelarens healthbar
        g.setColor(blk);
        normal.SetString(me.name).PrintAt(g, x + 10, y + 7);
        normal.SetString("Lv " + me.lvl).PrintAt(g, x + 87, y + 7);
        g.fillRect(x + 10, y + 20, 104, 6);
        g.setColor((meRatio > 0.5 ? hltgrn : (meRatio > 0.1 ? hltylw : hltred)));
        g.fillRect(x + 12, y + 22, (int) Math.round(meRatio * 100), 2);
        g.setColor(blk);
        String str = me.health + "/ " + me.maxHealth;
        normal.SetString(str).PrintAt(g, (x + 107) - g.getFontMetrics().stringWidth(str), y + 29);

        //Ovaler, marken
        g.setColor(new Color(0x80E078));
        g.fillOval(175, 75, 200, 75);
        g.fillOval(-20, 200, 240, 115);
        g.setColor(new Color(0xC0F890));
        g.fillOval(180, 80, 190, 65);
        g.fillOval(-15, 205, 230, 105);
        
        // Bilder från player me
        int temp_direkt = me.direciton;
        me.direciton = 2;
        me.drawChar(g, -40, getHeight() - 310, 210, 200, this);
        me.direciton = temp_direkt;
        
        // Bilder från player you
        temp_direkt = you.direciton;
        you.direciton = 1;
        you.drawChar(g, getWidth() - 170, 20, 100, 100, this);
        you.direciton = temp_direkt;
        
        //Statusmenu + Actionmenu
        g.setColor(blk);
        g.fillRect(0, 250, 400, 150);
        g.setColor(new Color(0xC8A848));
        g.fillRoundRect(5, 254, 250, 120, 10, 10);
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(10, 259, 240, 110, 5, 5);
        g.setColor(new Color(0x285068));
        g.fillRoundRect(12, 261, 236, 106, 5, 5);
        g.setColor(new Color(0xFFFFFF));
        new PFont(actionText, g, 25, 273);
        g.setColor(blk);
        g.fillRect(200, 250, 200, 150);
        g.setColor(new Color(0x706880));
        g.fillRoundRect(205, 255, 190, 120, 5, 5);
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(210, 260, 180, 110, 5, 5);

        if( !actionText.contains("used") ){
            //Menu items
            g.setColor(blk);

            battle.SetString(menuOptions[0]).PrintAt(g, 227, 288);
            battle.SetString(menuOptions[1]).PrintAt(g, 330, 288);
            battle.SetString(menuOptions[2]).PrintAt(g, 227, 329);
            battle.SetString(menuOptions[3]).PrintAt(g, 330, 329);

            //Cursor position
            int[] curpos[] = {new int[]{215, 285}, new int[]{317, 285}, new int[]{215, 325}, new int[]{317, 325}};
            g.fillPolygon(new int[]{curpos[menupos][0], curpos[menupos][0] + 9, curpos[menupos][0]}, new int[]{curpos[menupos][1], curpos[menupos][1] + 9, curpos[menupos][1] + 18}, 3);
        }
        else{
            // Epic punch
            Color p = new Color(255,55,55);
            if( you_turn ){
                g.setColor(p);
                g.fillRect(150, getHeight() - 240, 40, 40);
                p = p.darker();
                g.setColor(p);
                g.fillRect(130, getHeight() - 240, 40, 40);
                p = p.darker();
                p = p.darker();
                g.setColor(p);
                g.fillRect(140, getHeight() - 260, 40, 40);
            }
            else{
                g.setColor(p);
                g.fillRect(getWidth() - 170, 30, 20, 20);
                p = p.darker();
                g.setColor(p);
                g.fillRect(getWidth() - 160, 30, 20, 20);
                p = p.darker();
                p = p.darker();
                g.setColor(p);
                g.fillRect(getWidth() - 164, 40, 20, 20);
            }
        }
        
    }
    void Action(int selection){
        if (subMenu && selection!=-1) {
            if( actionText.contains("Attack") ){
                if( menupos == 0 ){         // TACKLE
                    actionText = (you_turn?you.name:me.name) + " used TACKLE!";
                    if( you_turn )
                        me.health -= 10;
                    else
                        you.health -= 10;
                }
                else if( menupos > 0 && menupos < 4 ){    // other
                    actionText = (you_turn?you.name:me.name) + " used " + (you_turn?you:me).damages.getDamageParamName(menupos) + "!";
                    (you_turn?me:you).health -= Math.random()*(you_turn?you:me).damages.getDamageParam(menupos)*(you_turn?you:me).lvl/(you_turn?me:you).lvl;
                }
                try {
                    paint(getGraphics());
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {}
                catch(NullPointerException e){}
                you_turn = !you_turn;   // ändrar turen
                if( you.health <= 0 )
                    exitCode = 2;
                else if( me.health <= 0 )
                    exitCode = 1;
            }
            selection = -1;
        }
        menupos = 0;
        if (selection == -1){
            subMenu = false;
            menuOptions[0] = "ATTACK";
            menuOptions[1] = "ITEMS";
            menuOptions[2] = "MAGIC";
            menuOptions[3] = "RUN";
            actionText = "What will "+(you_turn?you.name:me.name)+" do?";
            return;
        }
        subMenu= true;
        switch(selection){
            case 0: // Attack
                menuOptions[0] = "TACKLE";
                menuOptions[1] = "PUNCH";
                menuOptions[2] = "KICK";
                menuOptions[3] = "HEADBUTT";
                actionText = "Attack, you say?";
                break;
            case 1: // Items
                menuOptions[0] = "HP-POTION";
                menuOptions[1] = "HUGGER";
                menuOptions[2] = "-";
                menuOptions[3] = "-";
                actionText = "Items, you say?";
                break;
            case 2: // Magic
                menuOptions[0] = "CURSE";
                menuOptions[1] = "FIRE";
                menuOptions[2] = "ICE";
                menuOptions[3] = "HEAL";
                actionText = "Magic, you say?";
                break;
            case 3: // Run
                exitCode = 1;
                break;
            default:
        }
    }
    
    public void clear(){
        exitCode = 0;
        subMenu = false;
        menuOptions[0] = "ATTACK";
        menuOptions[1] = "ITEMS";
        menuOptions[2] = "MAGIC";
        menuOptions[3] = "RUN";
        actionText = "What will "+(you_turn?you.name:me.name)+" do?";
        menupos = 0;
    }
}
