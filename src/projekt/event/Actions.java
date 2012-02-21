package projekt.event;

/**
 *
 * @author johannes
 */
public interface Actions{
    boolean onWalk(int x,int y);
    boolean onAkeyDown(int x,int y);
    boolean onBkeyDown(int x,int y);
}