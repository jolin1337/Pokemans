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

    public Battle(Player c1, Player c2) {
        me = c1;
        you = c2;
        setSize(400, 400);
        setBackground(Color.white);
        setFocusable(false);
        /*
         * while (true) { Update(getGraphics()); }
         */
    }

    public void tick(boolean[] keys) {

        if (keys[Keys.up]) {
            if (menupos == 2) {
                menupos = 0;
            } else if (menupos == 3) {
                menupos = 1;
            }
        }
        if (keys[Keys.down]) {
            if (menupos == 0) {
                menupos = 2;
            } else if (menupos == 1) {
                menupos = 3;
            }
        }
        if (keys[Keys.left]) {
            if (menupos == 1) {
                menupos = 0;
            } else if (menupos == 3) {
                menupos = 2;
            }
        }
        if (keys[Keys.right]) {
            if (menupos == 0) {
                menupos = 1;
            } else if (menupos == 2) {
                menupos = 3;
            }
        }
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
        g.fillOval(20, 200, 200, 75);
        g.setColor(new Color(0xC0F890));
        g.fillOval(180, 80, 190, 65);
        g.fillOval(25, 205, 190, 65);


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
        new PFont("What will " + me.name + " do?", g, 25, 273);
        g.setColor(blk);
        g.fillRect(200, 250, 200, 150);
        g.setColor(new Color(0x706880));
        g.fillRoundRect(205, 255, 190, 120, 5, 5);
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(210, 260, 180, 110, 5, 5);

        //Menu items
        g.setColor(blk);

        battle.SetString("ATTACK").PrintAt(g, 227, 288);
        battle.SetString("ITEMS").PrintAt(g, 330, 288);
        battle.SetString("MAGIC").PrintAt(g, 227, 329);
        battle.SetString("RUN").PrintAt(g, 330, 329);

        //Cursor position
        int[] curpos[] = {new int[]{215, 285}, new int[]{317, 285}, new int[]{215, 325}, new int[]{317, 325}};
        g.fillPolygon(new int[]{curpos[menupos][0], curpos[menupos][0] + 9, curpos[menupos][0]}, new int[]{curpos[menupos][1], curpos[menupos][1] + 9, curpos[menupos][1] + 18}, 3);
    }
}
