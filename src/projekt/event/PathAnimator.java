package projekt.event;

import render.Player;

/**
 *
 * @author johannes
 */
public class PathAnimator {
    public Player character;
    
    private int[] points = new int[]{
        15,20,
        8,20,
        8,6,
        112,52,
        105,50,
        96,51,
        103,42,
        105,42,
        105,41,
        106,41,
        106,39,
        112,39,
        112,59,
        8,20,
        15,20
    };
    private float previousPositionX = 0;
    private float previousPositionY = 0;
    private int currentPoint = 0;

    public PathAnimator() {
        
    }

    public boolean[] tick(boolean[] k) {
        if( points.length <= currentPoint)return k;
        resetKeys(k);
        boolean eqX = false;
        if( character.x > points[currentPoint])
            k[Keys.left] = true;
        else if( character.x < points[currentPoint])
            k[Keys.right] = true;
        else
            eqX = true;
        if( character.y > points[currentPoint + 1])
            k[Keys.up] = true;
        else if( character.y < points[currentPoint + 1])
            k[Keys.down] = true;
        else if( eqX == true){
            //currentPoint += 2;
            if(points[currentPoint] == points[0] && points[currentPoint + 1] == points[1])
                currentPoint = 0;
        }
        
        if( ((previousPositionX/16 <= points[currentPoint] +1 && previousPositionX/16 >= points[currentPoint] -1)
                && (previousPositionY/16 <= points[currentPoint + 1] +1 && previousPositionY/16 >= points[currentPoint + 1] -1))){
            currentPoint += 2;
            if(points[currentPoint] == points[0] && points[currentPoint + 1] == points[1])
                currentPoint = 0;
        }
        if(character.getX2()/16 == character.x  && character.getY2()/16 == character.y || ((int)previousPositionX/16 == (int)character.getX2()/16 && (int)previousPositionY/16 == (int)character.getY2()/16 )){
            //k[Keys.down] = !k[Keys.down];
            //k[Keys.up] = !k[Keys.up];
            //k[Keys.left] = !k[Keys.left];
            //k[Keys.right] = !k[Keys.right];
            if(previousPositionX != character.getX2() || previousPositionY != character.getY2()){
                previousPositionX = character.getX2();
                previousPositionY = character.getY2();
            }
            else{
                previousPositionX = character.x*16;
                previousPositionY = character.y*16;
            }
        }
        
        
        if(character.x == 8 && character.y >= 9)
            k[Keys.a] = true;
        return k;
    }
    
    private boolean[] resetKeys(boolean[] k){
        k[Keys.left] = false;
        k[Keys.right] = false;
        k[Keys.up] = false;
        k[Keys.down] = false;
        k[Keys.a] = false;
        k[Keys.b] = false;
        k[Keys.select] = false;
        k[Keys.esc] = false;
        return k;
    }
    
    
}
