package projekt.event;

/**
 *
 * @author Johannes Linden <johannes.linden@gtg.se>
 */
public class FighterDamage {
    double TACKLE = 0;
    double PUNCH = 0;
    double KICK = 0;
    double HEADBUTT = 0;
    
    /**
    *
    * The constructor for FighterDamage
    */
    public FighterDamage(){
    }
    
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
