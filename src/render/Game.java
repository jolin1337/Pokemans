package render;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import projekt.event.Dialogs;
import projekt.event.Keys;

/**
 * Game är en subklass av Render vilket innebär att det är här all grafik egentligen skrivs ut på skärmen
 * @author johannes
 */
public class Game extends Render {

	public boolean loadWorld = false;
	public Point screenCoords = new Point(0, 0);
	/**
	 * världens nuvarande egenskaper sparas och ändras i denna variabel.
	 */
	public World world;
	/**
	 * focus innehåller information om var karaktären befinner sig och hur världen skall relatera sig till var karaktären är(görs ex med att sätta world.setOffset(int x,int y)
	 */
	public Player focus;
	public BufferedImage before;

	/**
	 * konstruktorn för spelet
	 */
	public Game() {
		requestFocus();
		focus = new Player(15, 17);
		focus.setChar("/res/CharMain/firehero.png");
		this.world = new World(this.getClass().getResource("/res/worlds/world1").getPath());
		Sound.playSound("/res/sounds/teleporter.wav");
		focus.setOnWalkCallback(new OnWalkCallback() {

			public boolean onWalk() {
				return true;
			}

			public boolean onStartWalk() {
				return false;
			}

			public boolean onEndWalk() {
				if (!Game.this.focus.action.isEmpty()) {
					Game.this.focus.freeze = false;
					Game.this.focus.action = "";
				}
				return true;
			}
		});
	}
	int delay = 0;
	private long millidelay;
	public boolean fight =false;
	/**
	 * tick är medlem av Render och anropas varje loopintervall och här kollar vi då om det har tryckts ner någon tangent
	 * @param k boolean[]
	 */
	@Override
	public void tick(boolean[] k) {
		boolean left = (k[Keys.left] || k[KeyEvent.VK_A]) && !focus.freeze;
		boolean right = (k[Keys.right] || k[KeyEvent.VK_D]) && !focus.freeze;
		boolean up = (k[Keys.up] || k[KeyEvent.VK_W]) && !focus.freeze;
		boolean down = (k[Keys.down] || k[KeyEvent.VK_S]) && !focus.freeze;
		boolean a = (k[Keys.a]);
		boolean b = (k[Keys.b]);
		boolean select = (k[Keys.select]);
		
		if (k[Keys.esc] || this.exitCode == 1) {
			this.exitCode = 1;
			return;
		}
		if (b) {
			focus.incr = 2;
		} else {
			focus.incr = 1;
		}

		if (!(left || right || up || down)) {
			//**** Delayen ****//
			delay++;
		} else {
			delay = 0;
		}
		if (delay >= radius) {
			this.focus.frame = 0;
		}
		//**** pickup ****//
		if (a) {
			if (Dialogs.endof && this.preformAction(false)) {
				millidelay = System.currentTimeMillis();
			}
			if (!Dialogs.endof) {//if(millidelay != 0 && System.currentTimeMillis() - millidelay > 100) {
				k[Keys.a] = false;
				this.drawDialog(Dialogs.nextMessage());
				if (Dialogs.endof) {
					this.focus.onWalkCallback.onEndWalk();
					this.preformAction(true);
				}
				millidelay = 0;
			}
			millidelay = System.currentTimeMillis();

		}




		//X-axeln
		if (this.focus.y - this.focus.y2 / radius == 0) {
			if ((left && !right) && (this.focus.x - this.focus.x2 / radius >= 0) && focus.x >= 1) {
				if (this.world.canGo(this.focus.x - 1, this.focus.y)) {
					focus.x--;
				}
				focus.direciton = 1;
			}
			if ((right && !left) && (this.focus.x - this.focus.x2 / radius <= 0) && focus.x2 < world.width * radius - radius) {
				if (this.world.canGo(this.focus.x + 1, this.focus.y)) {
					focus.x++;
				}
				focus.direciton = 2;
			}
		}
		//Y-axeln
		if (this.focus.x - this.focus.x2 / radius == 0) {
			if ((up && !down) && (this.focus.y - this.focus.y2 / radius >= 0) && focus.y > 1) {
				if (this.world.canGo(this.focus.x, this.focus.y - 1)) {
					focus.y--;
				}
				focus.direciton = 3;
			}
			if ((down && !up) && (this.focus.y - this.focus.y2 / radius <= 0) && focus.y >= 1 && focus.y2 < world.height * radius - radius) {
				if (this.world.canGo(this.focus.x, this.focus.y + 1)) {
					focus.y++;
				}
				focus.direciton = 0;
			}
		}
		/* Directions!!!*/
		if ((this.focus.x) * radius - (this.focus.x2) == 0 && !up && !down) {
			this.focus.stand = false;
			if ((left && !right)) {
				//focus.direciton = 1;
			}
			if ((right && !left)) {
				//focus.direciton = 2;
			}
		} else if ((this.focus.y) - (this.focus.y2) / radius == 0 && !right && !up) {
			if ((up && !down)) {
				//focus.direciton = 3;
			}
			if ((down && !up)) {
				//focus.direciton = 0;
			}
		}
	}

	private boolean preformAction(boolean pickNow) {
		int direction = this.focus.direciton;
		int x = 0, y = 0;
		if (direction == 1 || direction == 2) {
			x = (int) direction * 2 - 3;
		} else if (direction == 3 || direction == 0) {
			y = (-2 * direction) / 3 + 1;
		}
		Color temp;
		try {
			temp = this.world.getRGBA((int) this.focus.x2 / radius + x, (int) this.focus.y2 / radius + y);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			// plockar upp något utanför banans kanter
			return false;
		}
		if (temp.getRed() == 255
				&& !(temp.equals(new Color(0xff000000)) || temp.equals(new Color(0xffffffff)))) {
			if (Dialogs.endof && !pickNow) {
				this.focus.action = "dialog";
				Dialogs.initDialog(Dialogs.Begin.sayHello);
				this.focus.freeze = true;
			}
			if (!pickNow) {
				return true;
			}
			Graphics2D ag = this.world.alpha.createGraphics();
			ag.setColor(Color.white);
			ag.fillRect((int) this.focus.x2 / radius + x, (int) this.focus.y2 / radius + y, 1, 1);

			ag = this.world.items.createGraphics();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
			Composite c = ag.getComposite();
			ag.setComposite(composite);
			ag.setColor(new Color(0, 0, 0, 0));
			ag.fillRect(((int) this.focus.x2 + x * radius), ((int) this.focus.y2 + y * radius) - 8, radius, 20);
			ag.setComposite(c);
			int b = temp.getBlue();
			if (b != 0) {
				this.focus.addItem(b);
			}
			return true;
		}else if(temp.getBlue() == 255
				&& !(temp.equals(new Color(0xff000000)) || temp.equals(new Color(0xffffffff)))){		// omPlayer
			if (Dialogs.endof && !pickNow) {
				this.focus.action = "dialog";
				Dialogs.initDialog(Dialogs.Begin.TALK);
				this.focus.freeze = true;
				Player cur = world.players.get(0);
				fight = true;
				if(cur.x>focus.x)
					cur.direciton=1;//Vänster
				else if(cur.x!=focus.x)cur.direciton = 2;//höger
				if(cur.y>focus.y)
					cur.direciton = 3; //upp
				else if(cur.y!=focus.y)cur.direciton = 0; //ner
			}
			return true;
		}
		return false;
	}

	/**
	 *  Denna funktionen ritar allt i hela spelet (inklusive det world genererar) som en bild
	 * @param g Graphics
	 */
	@Override
	public void paint(Graphics g) {
		if (tr == null) {
			tr = new Transition("slideUpDown");
		}
		if ((focus.x * focus.radius != focus.x2)) {
			focus.slowMove(0);
		} else if (focus.y * focus.radius != focus.y2) {
			focus.slowMove(1);
		}
		int frx = this.focus.x;
		int fry = this.focus.y;
		checkWarp();
		try {
			world.paint(g, (int) this.focus.x2, (int) this.focus.y2, WIDTH, HEIGHT);
		} catch (ArrayIndexOutOfBoundsException e) { // positionen är utanför kartan
			ErrorHandler.CharacterBoundary.CharacterOutOfBoundary();
			ErrorHandler.CharacterBoundary.resetCharacterPositionAt(this.focus, frx - 1, fry + 1);
		}
		try {
			if ((int) focus.frame > 3) {
				focus.frame = 0;
			}
			BufferedImage t = focus.c.getSubimage(radius * (int) (focus.incr > 1.0 && focus.frame != 0 ? (focus.frame + 4) : focus.frame), 20 * focus.direciton, radius, 20);
			g.drawImage(t, WIDTH / 2 - t.getWidth() / 2 - radius / 2 + radius, HEIGHT / 2 - t.getHeight() - radius / 5 + radius, this);

		} catch (java.awt.image.RasterFormatException e) {//om bilden clips utanför bredden, höjden eller är negativ 
		}
		world.paintTop(g, (int) this.focus.x2, (int) this.focus.y2, WIDTH, HEIGHT);
		//drawShadowWithString("Version: \u03B1 0.5", 2, 12, Color.white, new Color(0x666666));
		//new PFont("Alpha - version 0.5", g, 2, 12);
		if (this.focus.action.equals("dialog")) {
			g.setColor(new Color(0x666666));
			//g.setFont(new Font("Lucida Typewriter Regular", Font.BOLD, 12));
			try {
				this.drawDialog(Dialogs.message[Dialogs.getIndex()]);
			} catch (NullPointerException e) {
			}
		}
		if (tr.index > 0) {
			if (tr.dirBool) {
				//dbg.drawImage(before, 0, 0, this);
			}
			tr.transition(g);
		}
		focus.freeze = focus.action.equals("dialog") || tr.index > 0;//tr.index>0;
	}

	public void drawDialog(String message) {
		int b = 5;
		int m = 8;
		int y = this.getHeight() / 4;
		this.dbg.fillRect(0, y * 3, WIDTH, y + HEIGHT % 4);
		this.dbg.setColor(Color.black);
		this.dbg.drawRect(b, y * 3 + b, WIDTH - 2 * b, y + HEIGHT % 4 - 2 * b);

		this.dbg.setFont(new Font(Font.DIALOG, Font.BOLD, 10));
		String[] lines = message.split("\n");
		/*for (int i = 0; i < lines.length; i++) {
		FontMetrics fm = this.getFontMetrics(this.dbg.getFont());
		int width = fm.stringWidth(lines[i]);
		if (width < this.getWidth() - (2 * b)) */
		new PFont(message, this.dbg, b + b, 3 * y + b + m);
		//}
	}

	public void drawShadowWithString(String string, int x, int y, Color c1, Color c2) {
		this.dbg.setColor(c2);
		this.dbg.drawString(string, x + 1, y);
		this.dbg.drawString(string, x - 1, y);
		this.dbg.drawString(string, x, y + 1);
		this.dbg.drawString(string, x, y - 1);
		this.dbg.setColor(c1);
		this.dbg.drawString(string, x, y);
	}

	public void checkWarp() {
		int frx = this.focus.x;
		int fry = this.focus.y;
		boolean fading = false;
		if (world.isPoortal((int) (this.focus.x2 / radius - 0.1 + 1), (int) (this.focus.y2 / radius - 0.1 + 1)) && !this.focus.freeze) {
			loadWorld = true;
			focus.action = "world";
			focus.freeze = true;

			try {
				Robot rbt = new Robot();
				before = rbt.createScreenCapture(new Rectangle(screenCoords.x, screenCoords.y, 406, 400));// 2,35
			} catch (AWTException ex) {
			}
			tr.transition(dbg);
		}
		if (loadWorld && tr.dirBool) {
			loadWorld = false;
			try {
				try {
					this.focus.addWorld(this.world.copy());
				} catch (CloneNotSupportedException ex) {
					//Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
				}
				String pa = "";
				try {
					pa = getClass().getResource("/res/worlds/world" + world.pos[2]).getPath();
				} catch (NullPointerException e) {
					this.exitCode = 1;
					System.out.println("unable to locate the world: /res/worlds/world" + world.pos[2]);
				}
				int x = this.world.pos[0];
				int y = this.world.pos[1];
				if (this.focus.getWorldFromPath(pa) == -1) {
					this.world.setWorld(pa, this.focus.x, this.focus.y);
				} else {

					this.world.pos[0] = -1;
					this.world.pos[1] = -1;
					this.world.pos[2] = -1;
					this.world = this.focus.getWorld(this.focus.getWorldFromPath(pa));
				}
				this.focus.transport(x, y);
				if (this.world.isPoortal(x, y)) {
					try {
						if (this.world.canGo(this.focus.x, this.focus.y + 1) && !this.world.isPoortal(this.focus.x, this.focus.y + 1)) {
							this.focus.y++;
						} else {
							throw new Error("" + (this.focus.y + 1));
						}
					} catch (Throwable t1) {
						try {
							if (this.world.canGo(this.focus.x, this.focus.y - 1) && !this.world.isPoortal(this.focus.x, this.focus.y - 1)) {
								this.focus.y--;
							} else {
								throw new Error("" + (this.focus.y - 1));
							}
						} catch (Throwable t2) {
							try {
								if (this.world.canGo(this.focus.x - 1, this.focus.y) && !this.world.isPoortal(this.focus.x - 1, this.focus.y)) {
									this.focus.x--;
								} else {
									throw new Error("" + (this.focus.y - 1));
								}
							} catch (Throwable t3) {
								if (this.world.canGo(this.focus.x + 1, this.focus.y) && !this.world.isPoortal(this.focus.x + 1, this.focus.y)) {
									this.focus.x++;
								} else {
									throw new ArrayIndexOutOfBoundsException();
								}
							}
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException b) {
				ErrorHandler.CharacterBoundary.CharacterOutOfBoundary();
				ErrorHandler.CharacterBoundary.resetCharacterPositionAt(this.focus, frx - 1, fry + 1);
				this.focus.freeze = false;
			}
		}
	}
}
