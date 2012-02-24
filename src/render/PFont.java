package render;

/**
 *
 * @author Johan Lindskogen
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class PFont {

    private BufferedImage Bimg;
    private String text;
    private ImageIcon img = new ImageIcon(getClass().getResource("/res/fonts/white.png"));
    private int _preferedWidth = 400;

    public PFont(String str) {
        text = str;
        ImgString();
    }

    public PFont() {
    }

    public PFont(String str, Graphics g, int x, int y) {
        text = str;
        ImgString();
        PrintAt(g, x, y);
    }

    public PFont PrintAt(Graphics g, int x, int y) {
        g.drawImage(Bimg, x, y, null);
        return this;
    }

    public PFont SetString(String str) {
        text = str;
        ImgString();
        return this;
    }

    public PFont SetColor(String str) {
        if ( str.equals("white") || str.equals("black") || str.equals("red") || str.equals("green") ) {
            img = new ImageIcon(getClass().getResource("/res/fonts/" + str + ".png"));
        } else {
            System.out.println("\"" + str + "\" is not a valid color!");
        }
        ImgString();
        return this;
    }

    @Override
    public String toString() {
        return text;
    }

    private BufferedImage ImgString() {
        if(text.isEmpty())return null;
        try {
            String[] rows = text.split("\n");
            String end = "";
            for(int i=0;i<rows.length;i++){
                if( rows[i].length() > _preferedWidth / 7 ) {
                    end = rows[i].substring( 0, _preferedWidth / 7 );
                    if ( end.lastIndexOf(" ") != -1)
                        end = end.substring(0, end.lastIndexOf(" "));
                    rows[i] = rows[i].replaceFirst( end, end + "\n" );
                    int x = 0;end="";
                    while(rows.length > x){
                        end += rows[x] + "\n";
                        x++;
                    }
                    rows = end.split("\n");
                    i--;
                }
                else{
                    int x = 0;end="";
                    while(rows.length > x){
                        end += rows[x] + "\n";
                        x++;
                    }
                }
            }
            Bimg = new BufferedImage(getStringLength() + 8, 13 * rows.length, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0, r = 0, ii = 0; i < end.length(); i++) {
                if (end.charAt(i) == '\n') {
                    r++;
                    String s = end.substring(0,i);
                    end = end.replace(s + "\n", s);
                    ii = i;
                    continue;
                }
                // Målar en bokstav i bi och skär av resten
                BufferedImage bi = new BufferedImage(7, 13, BufferedImage.TYPE_INT_ARGB);
                bi.createGraphics().drawImage(img.getImage(), -(end.codePointAt(i) - 33) * 7, 0, null);
                // målar ut bokstaven i Bimg för utskrift
                if(r == 0)
                    Bimg.createGraphics().drawImage(bi, (i - ii) * 7 + 4, r * 13, null);
                else
                    Bimg.createGraphics().drawImage(bi, (i - ii) * 7 - 3, r * 13, null);
            }
            return Bimg;
        } catch (java.lang.IllegalArgumentException e) {
            Bimg = new BufferedImage(7, 13, BufferedImage.TYPE_INT_ARGB);
            Bimg.createGraphics().drawImage(img.getImage(), ("@".codePointAt(0) - 33) * 7, 0, null);
            return Bimg;
        }
    }

    public int getStringLength() {
        return 7 * text.length();
    }
    
    public String getString(){
        return this.text;
    }
    
    public int getPreferedWidth(){
        return _preferedWidth;
    }
    
    public PFont setPreferedWidth(int w){
        _preferedWidth = w;
        return this;
    }
}
