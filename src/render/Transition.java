package render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Johan Lindskogen
 */
public final class Transition {
        int sleep;

	public static class Type {
		public static final int Fade = 0;
		public static final int FullSlide = 1;
		public static final int Stripes = 2;
		public static final int SlideUp = 3;
		public static final int Blank = 4;
                public static final int FightYou = 5;
                public static final int FightMe = 6;
	};
        public int Speed = 5;
        public interface Anim{
            void animate(Graphics g);
        }
        
        public Anim[] Transitions = new Anim[]{
            // fadeOutIn //
            new Anim(){ 
                int s=-1;
                public void animate(Graphics g){
                    if(g == null){
                        index += Speed;
                        if(s == -1)
                            s = sleep;
                        if(index <= 90 - (90 % Speed) || index >= 90 - (90 % Speed) + sleep)
                            index += Speed;
                        else
                            sleep-=Speed;
                        if (index == 90 - (90 % Speed)) {
                            dirBool = true;
                        }
                        else if (dirBool && index >= 180 - (180 % Speed) + sleep) {
                                dirBool = false;
                                index = 0;
                                sleep = s;
                        }
                        return;
                    }
                    int speed = Speed;
                    if(s == -1)
                        s = sleep;
                    if(index <= 90 - (90 % speed) || index >= 90 - (90 % speed) + sleep)
                        index += speed;
                    else
                        sleep-=speed;
                    if (index == 90 - (90 % speed)) {
                        dirBool = true;
                    }
                    else if (dirBool && index >= 180 - (180 % speed) + sleep) {
                            dirBool = false;
                            index = 0;
                            sleep = s;
                    }
                    g.setColor( new Color( 0, 0, 0, (int)Math.abs(255 * Math.sin((index)*Math.PI/180)) ) );
                    g.fillRect(x, y, width, height);
                }
            },
            // slideUpDown
            new Anim(){ 
                public void animate(Graphics g){
                    if(g == null){
                        index += Speed;
                        if (index >= height) {
                            dirBool = true;
                        }
                        if (index <= 0) {
                            index = 0;
                            dirBool = false;
                        }
                        return;
                    }
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
                    if(g == null){
                        index += Speed;
                        if (index > 0) {
                                dirBool = true;
                        }
                        if (index >= 400) {
                                dirBool = false;
                                index = 0;
                        }
                        return;
                    }
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
                    if(g == null){
                        index += Speed;
                        
                        if (index + Speed < height / 2) {
                            if (index > 0) {
                                    dirBool = true;
                            }
                            if (index + Speed >= height / 2) {
                                    dirBool = false;
                                    index = 0;
                            }
                        }
                        return;
                    }
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
                    if(g == null){
                        index += Speed;
                        return;
                    }
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
            },
            // FightYou animation 1, 0
            new Anim() {
                public void animate(Graphics g) {
                    Fight(g,0, Speed);
                }
            },
            // FightMe animation 1, 0
            new Anim() {
                public void animate(Graphics g) {
                    Fight(g,1, Speed);
                }
            }
        };
        private void Fight(Graphics g,int who, int how){
            int speed = 5;
            index += speed;
            if(g == null)
                return;
            g.setColor(Color.red);
            if( who%2 == 0 ){           // You turn
                if( how%4 == 0 ) {
                    g.fillRect(350 - (int)(index*1.5), index + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 - (10 - index/speed)), index - 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 + (10 + index/speed)), index - 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 - (10 - index/speed)), index + 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 + (10 + index/speed)), index + 10*index/speed + 50, index/speed, index/speed);
                }
                else if ( how%4 == 1 ){
                    g.fillRect(350 - (int)(index*1.5), index + 50, index/speed, index/speed);
                }
                else if ( how%4 == 2 ){
                    g.fillOval(350 - (int)(index*1.5), index + 50, index/speed, index/speed);
                }
                else{
                    g.fillRect(350 - (int)(index*1.5), index + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 - 10*index/speed), index - 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 + 10*index/speed), index - 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 - 10*index/speed), index + 10*index/speed + 50, index/speed, index/speed);
                    g.fillRect(350 - (int)(index*1.5 + 10*index/speed), index + 10*index/speed + 50, index/speed, index/speed);
                }
            }
            else if( who%2 == 1){           // Me turn
                if( how%4 == 0 ) {
                    g.fillRect((int)(index*1.5), 300 - index, 400/index, 1500/index);
                    g.fillRect((int)(index*1.5 - (10 - index/speed)), 300 - index - 10*index/speed, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 + (10 + index/speed)), 300 - index - 10*index/speed, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 - (10 - index/speed)), 300 - index + 10*index/speed, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 + (10 + index/speed)), 300 - index + 10*index/speed, 1500/index, 1500/index);
                }
                else if ( how%4 == 1 ){
                    g.fillRect((int)(index*1.5), 300 - index + 50, 1500/index, 1500/index);
                }
                else if ( how%4 == 2 ){
                    g.fillOval((int)(index*1.5), 300 - index + 50, 1500/index, 1500/index);
                }
                else{
                    g.fillRect((int)(index*1.5), index + 50, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 - 10*index/speed), 300 - index - 10*index/speed + 50, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 + 10*index/speed), 300 - index - 10*index/speed + 50, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 - 10*index/speed), 300 - index + 10*index/speed + 50, 1500/index, 1500/index);
                    g.fillRect((int)(index*1.5 + 10*index/speed), 300 - index + 10*index/speed + 50, 1500/index, 1500/index);
                }
            }
            if( index >= 400 )
                index=0;
        }
        
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