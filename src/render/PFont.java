
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
    private ImageIcon img= new ImageIcon(getClass().getResource("/res/fonts/white.png"));
    
    public PFont(String str){
	text = str;
	ImgString();
    }
    public PFont(String str, Graphics g, int x, int y){
	text = str;
	ImgString();
	PrintAt(g, x, y);
    }
    public PFont PrintAt(Graphics g, int x, int y){
	g.drawImage(Bimg, x, y, null);
	return this;
    }
    public PFont SetString(String str) {
	text = str;
	ImgString();
	return this;
    }
    public PFont SetColor(String str){
	if ( str == "white" || str == "black" || str == "red" || str == "green" )
	    img = new ImageIcon(getClass().getResource("/res/fonts/"+str+".png"));
	else
	    System.out.println("\""+str+"\" is not a valid color!");
	ImgString();
	return this;
    }

    @Override
    public String toString(){
	return text;
    }
    private BufferedImage ImgString(){
	try{
		String []rows=text.split("\n");
		Bimg = new BufferedImage(7*text.length(), 13*rows.length, BufferedImage.TYPE_INT_ARGB);
		for (int i=0,r=0,ii=0; i<text.length(); i++){
			if(text.charAt(i) =='\n'){
			r++;
			ii=i+r;
			continue;
			}
			BufferedImage bi = new BufferedImage(7,13,BufferedImage.TYPE_INT_ARGB);
			bi.createGraphics().drawImage(img.getImage(),-(text.codePointAt(i)-33)*7,0,null);
			Bimg.createGraphics().drawImage(bi,(i-ii)*7,r*13,null);
		}
		return Bimg;
	}
	catch(java.lang.IllegalArgumentException e){
		Bimg = new BufferedImage(7, 13, BufferedImage.TYPE_INT_ARGB);
		return Bimg;
	}
    }

	public int getStringLength() {
		return 7*text.length();
	}
}
