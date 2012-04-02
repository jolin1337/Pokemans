package render;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import projekt.event.Keys;

/**
 *
 * @author Johan Lindskogen <johan.lindskogen@gtg.se> / Johannes Lindén <johannes.linden@gtg.se>
 * 
 * Klassen används för att slåss mot en fiende eller en kompis
 * Med hjälp utav denna klassen kan man avgöra vilken av karaktärerna som är mest tränade
 * 
 * Ordförklaringar:
 * ( Spelare = den som spelar spelet )
 * ( objekt = en grupp av data )
 * ( topmenyn = en meny som användaren kan navigera mellan olika alternativ )
 * ( submeny = en annan meny exempelvis Attack som visar allternativ för olika attacker )
 */
public class Battle extends Render {
    /**
     * textDrawer är det allmäna objektet för att skriva ut texter i denna klassen med vårat egengjorda font (se render.PFont klassen)
     */
    PFont textDrawer = new PFont();
    
    /**
     * menupos angiver vilket allternativ som spelaren står över (0 <= menupos < 4 )
     */
    int menupos = 0;
    
    /**
     * me and you: de två Player objekten som skall fightas mot varandra
     */
    public Player me, you;
    
    /**
     * menuOptions innehåller de fyra alternativen i topmenyn för slaget
     */
    String[] menuOptions = {"ATTACK","ITEMS","MAGIC","RUN"};
    
    /**
     * actionText är en enkel och kort förklaring om vad spelaren förväntar sig göra nu
     */
    String actionText;
    
    /**
     * subMenu indikerar om spelaren befinner sig i topmenyn eller i en under liggande meny
     */
    boolean subMenu = false;
    
    /**
     * you_turn avgör vems tur det är att attackera
     */
    private boolean you_turn;
    
    /**
     * temp_var är en spärr så att spelaren inte kan hålla inne 'a' knappen och attackera hela tiden
     */
    protected boolean temp_var = false; 

    /**
     * The battle animations
     */
    private Transition __transition = new Transition();
    private int typeof_transition;
    
    /**
     * Konstruktorn sätter upp vem som skall börja och förbereder spelets start
     * @param c1 Character 1, första spelaren
     * @param c2 Character 2, andra spelaren
     */
    public Battle(Player c1, Player c2) {
        me = c1;                                                    // sätter me variabeln till att bli första spelaren
        you = c2;                                                   // sätter you variabeln till att bli andra spelaren
        you_turn = Math.random()<0.5?true:false;                    // en random funktion avgör om vem som skall börja
        actionText = "What will "+getCurrentPlayer().name+" do?";   // default medelande på skärmen
        
        setSize(400, 400);                                          // storleken på rutan är alltid 400x400 i dimmension
        setBackground(Color.white);                                 // bakgrundsfärgen skall vara vit
        setFocusable(false);                                        // så att keyeventen blir på "the main frame"
        /*
         * while (true) { Update(getGraphics()); }                  // tidigare main-loop för att köra fighten externt
         */
        __transition.Speed = 15;
    }

    /**
     * tick(boolean[] keys) avgör om användaren försöker påverka spelet och detta sker på rätt sätt
     * @param keys[] alla tangenter som man kan trycka ner i ascii värden
     */
    @Override
    public void tick(boolean[] keys) {
        if( keys[Keys.esc] ){
            __transition.index=0;
            clear();
        }
        if ( __transition.index >0 )
            return;
        if( you_turn ){                                         // om det är motståndarens tur så autogenererar vi hans drag
            keys[Keys.a] = true;                                // han vill trycka på 'a'
            menupos = 0;                                        // när menupos = 0 ( dvs Attack )
            if( subMenu )                                       // nästa gång loopen anropar tick så är vi inne i subMenu
                menupos = Math.round((float)Math.random()*3);   // då vill våran fiende attakera med random attack
        }
        if (keys[Keys.up]) {                                    // om spelaren trycker tangenten uppåt
            if (menupos == 2 && !menuOptions[0].equals("-")) {  // om menupos == 2 och första menyallternativet inte är tomt
                menupos = 0;                                    // navigera till första menyallternativet
            } else if (menupos == 3 && !menuOptions[1].equals("-")) {//om menupos == 3 och andra menyallternativet inte är tomt
                menupos = 1;                                    // navigera till andra menyallternativet
            }
        }
        if (keys[Keys.down]) {                                  // om spelaren trycker ner tangenten neråt
            if (menupos == 0 && !menuOptions[2].equals("-")) {  // om menupos == 0 och tredje allternativet inte är tomt
                menupos = 2;                                    // navigera till tredje menyallternativet
            } else if (menupos == 1 && !menuOptions[3].equals("-")) {// om penupos == 1 och fjärde allternativet inte är tomt
                menupos = 3;                                    // navigera till fjärde allternativet
            }
        }
        if (keys[Keys.left]) {                                  // om spelaren trycker ner tangenten vänster
            if (menupos == 1 && !menuOptions[0].equals("-")) {  // om menupos == 1 och första allternativet inte är tomt
                menupos = 0;                                    // navigera till första allternativet
            } else if (menupos == 3 && !menuOptions[2].equals("-")) {// om menupos == 3 och tredje allternativet inte är tomt
                menupos = 2;                                    // navigera till det tredje allternativet
            }
        }
        if (keys[Keys.right]) {                                 // om spelaren trycker ner tnagenten höger
            if (menupos == 0 && !menuOptions[1].equals("-")) {  // om menupos == 0 och andra allternativet inte är tomt
                menupos = 1;                                    // navigera till menyallternativ två
            } else if (menupos == 2 && !menuOptions[3].equals("-")) {// om menupos == 2 och fjärde allternativet inte är tomt
                menupos = 3;                                    // navigera till det fjärde allternativet
            }
        }
        if (keys[Keys.a] && !temp_var || keys[Keys.a] && you_turn){// om spelaren trycket ner tangenten 'a' och inte håller nere den eller om det är datorn som skall autogenereras
            Action(menupos);                                    // gör nuvarande menyallternativ
            temp_var = true && !you_turn;                                    // temp_var blir true för att undvika en bugg(se variabelns decklaration)
        }
        if (!keys[Keys.a] && temp_var)                          // buggfix 
            temp_var = false;
            
        
        if (subMenu && keys[Keys.b])                            // om spelaren är i en submeny och trycker tangenten 'b' så hoppar vi ur subbmenyn
            Action(-1);                                         // gör bakot handlingen
    }

    /**
     * Denna funktionen anropas bara när denna klassen skall bli uppdaterad
     * då förhinndrar den glapp av data som visas på skärmen samtidigt
     * @param g Grafik objektet som visas på Canvas componenten
     */
    private void Update(Graphics g) {
        if (dbImage == null || dbImage.getWidth(this) != this.getSize().width) {    // om det är första gången vi anropar denna klassen
            dbImage = createImage(this.getSize().width, this.getSize().height);     // sätter dbImage som är deklarerad i parent klassen
            dbg = dbImage.getGraphics();                                            // sätter dbg så att vi kan använda componenten över allt
        }
        dbg.setColor(getBackground());                                              // sätter bakgrundsfärgen till nuvarande penselfärg
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);            // målar allt svart
        dbg.setColor(getForeground());                                              // förgrundsfägren är default när vi anropar våran paint(Graphics g) funktion
        paint(dbg);                                                                 // annropar våran paint funktion
        g.drawImage(dbImage, 0, 0, this);                                           // uppdatera Canvascomponents graphics
    }

    /**
     * Denna funktionen ritar ut allt som spelaren kan se i ett graphics objekt (g)
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        Color blk = new Color(0x444444),                                // initializerar olika färger som skall användas 
                ylw = new Color(0xF8F8D8),
                hltgrn = new Color(0x70F8A8),
                hltylw = new Color(0xF8E038),
                hltred = new Color(0xF85838);

        int x = 270, y = 200;                                           // sätter positionen för spelarens relativa komponenter, såsom health bar/ status osv
        double meRatio = (double) me.health / (double) me.maxHealth;    // förändringsfaktorn på nuvarande health och max health för me Playern
        double youRatio = (double) you.health / (double) you.maxHealth; // förändringsfaktorn på nuvarande health och max health för you Playern
        textDrawer.SetString(".");                                      // rensa textDrawers buffert
        textDrawer.SetColor("black");                                   // sätt textDrawers font-color till svart
        g.setColor(blk);                                                // sätt Graphics färg till "svart"
        g.fillRoundRect(10, 10, 120, 34, 5, 5);                         //Motståndarens bakgrundsruta
        g.fillRoundRect(270, 200, 120, 44, 5, 5);                       //Spelarens bakgrundsruta
        g.setColor(ylw);
        g.fillRoundRect(12, 12, 116, 30, 5, 5);                         //Motståndarens förgrundsruta
        g.fillRoundRect(x + 2, y + 2, 116, 40, 5, 5);                   //Spelarens förgrundsruta

        //Motståndarens healthbar
        g.setColor(blk);                                                // sätt Graphics färg till "svart" igen
        textDrawer.SetString(you.name).PrintAt(g, 20, 17);              // rita ut motståndarens namn
        textDrawer.SetString("Lv " + you.lvl).PrintAt(g, 92, 17);       // och motståndarens lvl
        g.fillRect(20, 30, 104, 6);                                     // måla ut motståndarens grafiska healthbars bakgrund
        g.setColor((youRatio > 0.5 ? hltgrn : (youRatio > 0.1 ? hltylw : hltred)));// sätt färgen beroende på hur mycket health du har kvar
        g.fillRect(22, 32, (int) Math.round(youRatio * 100), 2);        // måla ut motståndarens grafiska healthbars färj

        //Spelarens healthbar
        g.setColor(blk);                                                // gör samma sak med spelarens healthbar
        textDrawer.SetString(me.name).PrintAt(g, x + 10, y + 7);
        textDrawer.SetString("Lv " + me.lvl).PrintAt(g, x + 87, y + 7);
        g.fillRect(x + 10, y + 20, 104, 6);
        g.setColor((meRatio > 0.5 ? hltgrn : (meRatio > 0.1 ? hltylw : hltred)));
        g.fillRect(x + 12, y + 22, (int) Math.round(meRatio * 100), 2);
        g.setColor(blk);
        String str = me.health + "/ " + me.maxHealth;                   // skriv ut hp i siffror
        textDrawer.SetString(str).PrintAt(g, (x + 107) - g.getFontMetrics().stringWidth(str), y + 29);

        //Ovaler, marken
        g.setColor(new Color(0x80E078));                                // måla ut ovalen på båda spelarnas yta
                                                                    // den yttre ovalen skall vara mörkare grön
        g.fillOval(175, 75, 200, 75);
        g.fillOval(-20, 200, 240, 115);
        g.setColor(new Color(0xC0F890));
                                                                    // den innre ovalen skall vara ljusgrön
        g.fillOval(180, 80, 190, 65);
        g.fillOval(-15, 205, 230, 105);
        
        // Bilder från player me
        int temp_direkt = me.direciton;                             // spara en temporär riktningsvariabel för att undvika rotations buggar i spelet
        me.direciton = 2;                                           // vänd spelaren mot motståndaren
        me.drawChar(g, -40, getHeight() - 310, 210, 200, this);     // måla karaktären ( förstorat ) så att den syns så bra som möjligt
        me.direciton = temp_direkt;                                 // återställ rotationen på spelaren
        
        // Bilder från player you
        temp_direkt = you.direciton;                                // samma sak med motståndaren fast med anndra koordinater
        you.direciton = 1;
        you.drawChar(g, getWidth() - 170, 20, 100, 100, this);
        you.direciton = temp_direkt;
        
        if( __transition.index > 0 ) {
            __transition.Transitions[getCurrentTransition()].animate(g);
        }
        
        //Statusmenu + Actionmenu
        g.setColor(blk);                                            // sätt färjen till "svart"
        g.fillRect(0, 250, 400, 150);                               // måla bakgrunden av panelen
        g.setColor(new Color(0xC8A848));                            // måla någon border med rundade hörn
        g.fillRoundRect(5, 254, 250, 120, 10, 10);                  
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(10, 259, 240, 110, 5, 5);
        g.setColor(new Color(0x285068));
        g.fillRoundRect(12, 261, 236, 106, 5, 5);                   // tillslut arean av status menyn
        g.setColor(new Color(0xFFFFFF));
        textDrawer.SetColor("white");                               // måla ut status medelandet
        textDrawer.SetString(actionText).PrintAt(g, 25, 273);       // till vänster
        textDrawer.SetColor("black");
        g.setColor(blk);
        g.fillRect(200, 250, 200, 150);                             // några borders till Actionmenyn
        g.setColor(new Color(0x706880));
        g.fillRoundRect(205, 255, 190, 120, 5, 5);
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(210, 260, 180, 110, 5, 5);                  // och sedan arean av Actionmenyn osv

        if ( __transition.index <= 0 ) {                         // om spelarna inte befinner sig i attack mode så målar vi ut menyallternativen
            //Menu items
            g.setColor(blk);                                        // nästan svart markör

            textDrawer.SetString(menuOptions[0]).PrintAt(g, 227, 288);
            textDrawer.SetString(menuOptions[1]).PrintAt(g, 330, 288);
            textDrawer.SetString(menuOptions[2]).PrintAt(g, 227, 329);
            textDrawer.SetString(menuOptions[3]).PrintAt(g, 330, 329);

            //Cursor position
            int[] curpos[] = {new int[]{215, 285}, new int[]{317, 285}, new int[]{215, 325}, new int[]{317, 325}};
            g.fillPolygon(new int[]{curpos[menupos][0], curpos[menupos][0] + 9, curpos[menupos][0]}, new int[]{curpos[menupos][1], curpos[menupos][1] + 9, curpos[menupos][1] + 18}, 3);
        }
        
    }
    
    /**
     * Action är en funktion som gör någonting med spalrna, beroende på vad de klickar på exempelvis navigerar den i menysystemet osv
     * @param selection avgör vilket menyallternativ som spelaren har valt
     */
    void Action(int selection){
        if (subMenu && selection!=-1) {                                             // om vi är i en submeny och vi inte klickar 'b'
            if( actionText.contains("Attack") ){                                    // om spelaren har klickat på attack
                if( menupos == 0 ){                                                 // TACKLE ( använder )
                    if( you_turn )                                                  // om det är motståndarens tur
                        me.health -= getCurrentPlayer().damages.getDamageParam(0);                                            // nuvarande konstant värde (10hp)
                    else                                                            // annars är det spelarens tur
                        you.health -= 10;                                           // nuvarande konstant värde (10HP)
                    actionText = getCurrentPlayer().name + " used TACKLE!\n. \n " + // ändrar status medelandet( viktigt att den innehåller nyckelordet "used"
                            getCurrentPlayerInverse().name + " has lost 10 HP";
                }
                else if( menupos > 0 && menupos < 4 ){                              // om det inte är tackle vi har angivit så blir det denna formell som avgör dmg på motståndaren
                                                                                    // dmg är variabeln som blir så mycket skada som spelaren tar
                    int dmg = (int)(Math.random()*getCurrentPlayer().damages.getDamageParam(menupos)*getCurrentPlayer().lvl/getCurrentPlayerInverse().lvl);
                    getCurrentPlayerInverse().health -= dmg;                        // tar bort skadan från spelaren
                                                                                    // sätter status medelandet med tecken på att spelaren skadade sin motståndare ( vickitgt att ha med nyckelhordet "used" )
                    actionText = getCurrentPlayer().name + " used " + getCurrentPlayer().damages.getDamageParamName(menupos) + "!\n. \n " + 
                            (dmg>0?getCurrentPlayerInverse().name + " has lost " + dmg + "HP":" missed the attack");
                }
                __transition.Speed = menupos;
                __transition.Transitions[getCurrentTransition()].animate(getGraphics());                                               // visa cool punch bild
                you_turn = !you_turn;                                               // ändrar turen
                if( you.health <= 0 )                                               // kollar om you har dött under slaget
                    exitCode = 2;                                                   // sätter en exitCode(se render.Render) till 2
                else if( me.health <= 0 )                                           // kollar om me har dött under slaget
                    exitCode = 1;                                                   // sätter en exitCode(se render.Render) till 1
            }
            selection = -1;                                                         // återgå till huvudmenyn
        }
        menupos = 0;                                                                // sätter menupos att peka på första menyallternativet
        if (selection == -1){                                                       // om vi skall återgå till huvudmenyn
            subMenu = false;                                                        // sätter submenu till false
            menuOptions[0] = "ATTACK";                                              // sätter default värden på huvudmenyn
            menuOptions[1] = "ITEMS";
            menuOptions[2] = "MAGIC";
            menuOptions[3] = "RUN";
            actionText = "What will "+getCurrentPlayer().name+" do?";               // återställer status medelandet
            return;                                                                 // avbryter action
        }
        subMenu= true;                                                              // nu kommer vi in i en subbmeny
        switch(selection){                                                          // vad har vi valt för submeny tro?
            case 0: // Attack
                for( int i = 0; i< menuOptions.length; i++ )                        // loopar igenom alla menyallternativ
                    menuOptions[i] = getCurrentPlayer().damages.getDamageParamName(i);// sätter menyallternativ nummer i
                actionText = "Attack, you say?";                                    // status medelande med ett vicktigt nyckelord "Attack"
                break;
            case 1: // Items
                menuOptions[0] = "HP-POTION";                                       // sätter menyallternativ n
                menuOptions[1] = "HUGGER";
                menuOptions[2] = "-";
                menuOptions[3] = "-";
                actionText = "Items, you say?";                                     // status medelande med ett vicktigt nyckelord "Items"
                break;
            case 2: // Magic
                menuOptions[0] = "CURSE";                                           // sätter menyallternativ n
                menuOptions[1] = "FIRE";
                menuOptions[2] = "ICE";
                menuOptions[3] = "HEAL";
                actionText = "Magic, you say?";                                     // status medelande med ett vicktigt nyckelord "Magic"
                break;
            case 3: // Run
                exitCode = Math.random()>0.5?1:0;                                   // om man har valt att fly från denna fight
                if(exitCode == 0)
                    actionText = getCurrentPlayer().name + " \n try to run but faild!";
                else
                    actionText = getCurrentPlayer().name + " used run";
                you_turn = !you_turn;
                try {
                    print(getGraphics());
                    Thread.sleep(1000);
                } 
                catch( InterruptedException ex ) {}
                catch( NullPointerException ex ) {}
                break;
            default:
        }
    }
    
    /**
     * återställer hela denna klassen så att den kan användas flera gånger i spelet
     */
    public void clear(){
        __transition.index = 0;
        exitCode = 0;                                                               // exitCode == 0
        subMenu = false;                                                            // topmenyn
        menuOptions[0] = "ATTACK";                                                  // default topmenyallternativ
        menuOptions[1] = "ITEMS";
        menuOptions[2] = "MAGIC";
        menuOptions[3] = "RUN";
        actionText = "What will "+getCurrentPlayer().name+" do?";                   // defautl status medelande
        menupos = 0;                                                                // första menyallternativet
    }
    
    /**
     * 
     * @return Returnerar nuvarande Player, dvs vilkens tur är det?
     */
    public final Player getCurrentPlayer(){
        return (you_turn?you:me);
    }
    
    /**
     * Gör samma sak som getCurrentPlayer() men inverterat
     * @return Returnerar nuvarande Player, dvs vilkens tur är det inte?
     */
    public final Player getCurrentPlayerInverse(){
        return (you_turn?me:you);
    }
    
    public final int getCurrentTransition(){
        return (you_turn?Transition.Type.FightMe:Transition.Type.FightYou);
    }
    
    public final int getCurrentTransitionInverse(){
        return (you_turn?Transition.Type.FightYou:Transition.Type.FightMe);
    }
}   

