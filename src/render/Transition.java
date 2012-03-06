package render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Johan Lindskogen
 */
public final class Transition {

	public static class Type {
		public static final int Fade = 0;
		public static final int FullSlide = 1;
		public static final int Stripes = 2;
		public static final int SlideUp = 3;
		public static final int Blank = 4;
	};
        public int Speed = 5;
        public interface Anim{
            void animate(Graphics g);
        }
        
        public Anim[] Transitions = new Anim[]{
            // fadeOutIn //
            new Anim(){ 
                public void animate(Graphics g){
                    int speed = Speed;
                            index += speed;
                    /*if (index == 90 - (90 % speed)) {
                            index = 90;
                            g.setColor(new Color(0, 0, 0, (int)Math.abs(255 * Math.sin((index+255)*Math.PI/180)) ) );
                            dirBool = true;
                    } else if (dirBool && index < 0) {
                            dirBool = false;
                            index = 0;
                    } else {
                            g.setColor( new Color( 0, 0, 0, (int)Math.abs(255 * Math.sin((index+255)*Math.PI/180)) ) );
                    }*/
                    if (index == 90 - (90 % speed)) {
                        dirBool = true;
                    }
                    else if (dirBool && index >= 180 - (180 % speed)) {
                            dirBool = false;
                            index = 0;
                    }
                    g.setColor( new Color( 0, 0, 0, (int)Math.abs(255 * Math.sin((index)*Math.PI/180)) ) );
                    g.fillRect(x, y, width, height);
                }
            },
            // slideUpDown
            new Anim(){ 
                public void animate(Graphics g){
                    g.setColor(Color.BLACK);
                    int speed = Speed;
                    if (index >= height) {
                            dirBool = true;
                    }
                    if (dirBool) {
                            g.fillRect(0, 0, width, index);
                            g.fillRect(0, width - index, width, index);
                            index -= speed;
                    } else if (index <= height / 2) {
                            g.fillRect(0, 0, width, index);
                            g.fillRect(0, width - index, width, index);
                            index += speed;
                    } else {
                            g.fillRect(0, 0, width, height);
                            index += speed;
                    }
                    if (index <= 0) {
                            index = 0;
                            dirBool = false;
                    }
                }
            },
            // stripes
            new Anim(){ 
                public void animate(Graphics g){
                    int antal = 15;
                    int speed = Speed;
                    g.setColor(Color.BLACK);
                    int lineHeight = height / antal;
                    for (int i = 0; i < antal; i++) {
                            if (i % 2 == 0) {
                                    g.fillRect(0, lineHeight * i, index, lineHeight);
                            } else {
                                    g.fillRect(width - index, lineHeight * i, index, lineHeight);
                            }
                    }

                    index += speed;
                    if (index > 0) {
                            dirBool = true;
                    }
                    if (index >= 400) {
                            dirBool = false;
                            index = 0;
                    }
                }
            },
            // slideup
            new Anim() {
                public void animate(Graphics g) {
                    g.setColor(Color.BLACK);
                    int speed = Speed;
                    if (index + speed < height / 2) {
                            g.fillRect(0, 0, width, height / 2 - index);
                            g.fillRect(0, height / 2 + index, width, height / 2 - index);

                            index += speed;
                            if (index > 0) {
                                    dirBool = true;
                            }
                            if (index + speed >= height / 2) {
                                    dirBool = false;
                                    index = 0;
                            }
                    }
                }
            },
            // Blank
            new Anim() {
                public void animate(Graphics g) {
                    int speed = Speed;
                    if (!dirBool) {
                            index += speed;
                    } else {
                            index -= speed;
                    }
                    if (index == 255 - (255 % speed)) {
                            index = 255;
                            g.setColor(new Color(0, 0, 0));
                            dirBool = true;
                    } else if (dirBool && index < 0) {
                            dirBool = false;
                            index = 0;
                    } else {
                            g.setColor(new Color(0, 0, 0));
                    }
                    g.fillRect(x, y, width, height);
                }
            }
        };
        
	private int x = 0, y = 0, width = 415, height = 415;
	public int index = 0;
	public boolean dirBool = false;
        
        public void setBounds(Rectangle r){
            x = r.x;
            y = r.y;
            width = r.width;
            height = r.height;
        }
        public void setBounds(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
}