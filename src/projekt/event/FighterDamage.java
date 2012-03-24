package projekt.event;

/**
 * Denna klassen används till att avgöra hur mycket skada som man skall kunna göra under en fight
 * @author Johannes Linden <johannes.linden@gtg.se>
 */
public class FighterDamage {
    /**
     * TACKLE (0) attacken är standard dvs du kan inte missa
     * Man tacklas med axeln och motståndaren får en rejäl stöt som  käns genom hela kroppen
     */
    double TACKLE = 0;
    
    /**
     * PUNCH (1)
     * ett slag kan vara dödligt beroende på var den träffar se till att få denna attacken rätt och du kan vara farlig för vem som helst
     */
    double PUNCH = 0;
    
    /**
     * KICK (2)
     * Svårt att kontrollera kanske men defenetivt inte att underskatta 
     */
    double KICK = 0;
    
    /**
     * HEADBUTT (3)
     * med huvudet först så kan både du och din fiende få väldigt stora problem och för att inte tala om skador
     */
    double HEADBUTT = 0;
    
    /**
    *
    * The constructor for FighterDamage
    */
    public FighterDamage(){
    }
    
    /**
     *  
     * @param i den utvalda attackens index
     * @return returnerar skadan för den utavalda attacken med intex i;
     */
    public double getDamageParam(int i){
        if( i == 0)
            return TACKLE;
        if( i == 1)
            return PUNCH;
        if( i == 2)
            return KICK;
        if( i == 3)
            return HEADBUTT;
        return 0;
    }
    
    /**
     * sätter skadan som skall göras på utvald attack
     * @param i den utvalda attackens index
     * @param v värdet som den utvalda attacken skall ha
     */
    public void setDamageParam(int i, double v){
        if( i == 0)
            TACKLE = v;
        if( i == 1)
            PUNCH = v;
        if( i == 2)
            KICK = v;
        if( i == 3)
            HEADBUTT = v;
    }

    /**
     * 
     * @param i den utvalda attackens index
     * @return en sträng som motsvarar passande namn på attacken
     */
    public String getDamageParamName(int i) {
        if( i == 0)
            return "TACKLE";
        if( i == 1)
            return "PUNCH";
        if( i == 2)
            return "KICK";
        if( i == 3)
            return "HEADBUTT";
        return "";
    }
    
    
}
