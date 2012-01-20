package primitives;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import render.PFont;
/**
 *
 * @author johannes
 */
public class House {
	int width=5,height=3,deep=4;
	int position[]		= new int[]{0,0};
	double shear		= 0.5;
	BufferedImage front	= null;
	BufferedImage left	= null;
	BufferedImage right	= null;
	BufferedImage top	= null;
	
	int doorPos[]		= new int[]{2, 0};
	boolean dimension3=true;
	public House(){}
	public House(int x,int y){
		position[0]=x;
		position[1]=y;
		ImageIcon i=new ImageIcon(getClass().getResource("/res/front.png"));
		front =new BufferedImage(i.getIconWidth(),i.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		front.getGraphics().drawImage(i.getImage(), 0, 0, null);
		
		i=new ImageIcon(getClass().getResource("/res/side.png"));
		left =new BufferedImage(i.getIconWidth(),i.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		left.getGraphics().drawImage(i.getImage(), 0, 0, null);
		right =new BufferedImage(i.getIconWidth(),i.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		right.getGraphics().drawImage(i.getImage(), 0, 0, null);
	}
	private int projekt(int x, int y,int f){
		
		return 0;
	}
	public void paint3D(Graphics g,double x2, double y2,int width,int height,int radius){
		Graphics2D g2=((Graphics2D)g);
		AffineTransform t=g2.getTransform();
		
		double k=shear;
		double dx = x2-position[0]*radius;
		double dy = y2-position[1]*radius;
		double sc=dy/(radius);
		int w=(int)(this.width*radius-sc), d=(int)(deep*radius-sc),h=(int)(this.height*radius-sc);
		double s=shear;
		int dist=(int)(dx*(dx+w));
		int x=(int)(-x2+width/2+this.position[0]*radius+(int)(dx*dy/width)+(position[0]*radius-(x2))/y2),y=(int)(height/2-y2+position[1]*radius-dy/2-d*shear);//-y2 + height/2+this.position[1]*radius;
		double scale;
		if(dist!=0)
			x+=(-x2+width/2) * this.position[1]*radius/(height*dist);
		scale=-((double)x2-w/2-this.position[0]*radius)/((double)width*2);
		if(dx>=0&&dx<=w)
			scale=0;
		g.drawRect(x, y, w, h);
		
		if(dx>=w)
			g2.translate(x,y);
		else g2.translate(x+w,y);
		g2.scale(scale, 1);
		g2.shear(0, s);
		if(left==null&&dx>=w)
			g.drawRect(0,0, d, h);
		else if(left!=null)
			g.drawImage(left, 0, 0, d, h, null);
		else if(right==null)
			g.drawRect(0,0, d, h);
		else
			g.drawImage(right, 0, 0, d, h, null);
		g2.setTransform(t);
		if(front==null){
			g.drawRect((int)(x+d*scale), (int)(y+d*s), w, h);
			PFont p=new PFont("Under Construction").SetColor("green");
			int l=p.getStringLength();
			p.PrintAt(g,(int)(x+d*scale)+(w-l)/2, (int)(y+d*s)+h/2);
		}
		else{
			g.drawImage(front, (int)(x+d*scale), (int)(y+d*s), w, h, null);
		}
		if(dx<w)
			g2.translate(x,y);
		else g2.translate(x+w,y);
		g2.scale(scale, 1);
		g2.shear(0, s);
		
		if(left==null&&dx<w)
			g.drawRect(0,0, d, h);
		else if(left!=null&&dx<w)
			g.drawImage(left, 0, 0, d, h, null);
		else if(right==null)
			g.drawRect(0,0, d, h);
		else
			g.drawImage(right, 0, 0, d, h, null);
		g2.setTransform(t);
		g.setColor(Color.green);
		g2.translate(x, y);
		g2.shear(scale*2, 0);
		g.fillRect(0, 0, w, (int)(d*s));
		g2.setTransform(t);
	}
}
