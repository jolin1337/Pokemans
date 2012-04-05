package render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import projekt.event.GameEventHandler;

/**
 * Render funktionen är en indirekt använd klass (via Projekt) som gör tillgång till skällva Canvas på JFramen
 * @author johannes
 */
public class Render extends Canvas {

	protected final int WIDTH = 405, HEIGHT = 405;
	/**
	 * om spelet vill stänga ner programmet så sätts denna till 1
	 */
	public int exitCode = 0;
	/**
	 * radius används för att bestämma hur stor den kvadratiska ruta är som karaktären kan stå i
	 */
	public int radius = 16;
	/**
	 *  dbg används som en buffert för att förvara föregående renderade bild utav Canvaset
	 */
	public Graphics dbg;
	/**
	 * dbImage lagrar dbg
	 */
	public Image dbImage;
        
	/**
	 * eHandle används för tangentnedtryckningar frivillig användning
	 */
	public GameEventHandler eHandle;

	/**
	 *  tick används till att kolla om några nertryckningar av användaren är nere(game.tick(e);
	 * @param k boolean[]
	 */
	public void tick(boolean[] k) {
	}

	/**
	 *  konstruktorn för våt canvasobjekt ställer in några standardvärden som förhindrar Exceptions
	 */
	public Render() {
		setBackground(Color.black);
		this.eHandle = new GameEventHandler();
		this.addKeyListener(this.eHandle);
	}

	/**
	 * render är bara en genväg till att måla om hela canvasytan(innehåller:this.repaint())
	 */
	public void render() {
		repaint();
	}
	Transition tr = null;

	/**
	 * en funktion som finns inbyggt i Canvas klassen som anropas när man anropar repaint() g är här den nuvarande målade arean
	 * @param g Graphics
	 */
	@Override
	public void update(Graphics g) {
		if (tr == null)
			tr = new Transition();
		if (dbImage == null || dbImage.getWidth(this) != WIDTH) {
			dbImage = createImage(WIDTH, HEIGHT);
			dbg = dbImage.getGraphics();
		}
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, WIDTH, HEIGHT);

		dbg.setColor(getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, null);
		int i = 0;
	}

	public void setting(int WIDTH, int HEIGHT) {
		dbImage = createImage(WIDTH, HEIGHT);
		dbg = dbImage.getGraphics();
	}

	/**
	 *  Definieras i en annan subklass om vad som skall målas på detta canvas objekt
	 * @param g Graphics
	 */
	@Override
	public void paint(Graphics g) {
	}
}