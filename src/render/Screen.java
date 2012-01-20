package render;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * Screen har koll på skärmens storlek så som om den skall vara fullscreen eller bara en vis dimmension.
 * @author johannes
 */
public class Screen {

	/**
	 * GraphicDevice används för att få kontroll över hela skärmen istället för bara JFramen.
	 */
	private GraphicsDevice vc;
	/**
	 *  dim är storleken på fönstret. (om det är fullscreen så måste naturligtvis denna vara sätt till datorns skärmstorlek)
	 */
	public Dimension dim;
	/**
	 * Avgör om JFramen skall ta upp hela dataskärmen.
	 */
	DisplayMode dm;

	/**
	 * konstruktorn till Screen sätter default dimensionen till 800,600px om du inte definierar annat.
	 */
	public Screen() {
		dim = new Dimension(800, 600);
		dm = new DisplayMode(dim.width, dim.height, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice();
	}

	/**
	 * Ett annat sätt att definera dimmensionerna på är genom att initializera Screen med width,height(rekomenderas
	 * @param width bredden
	 * @param height höjden
	 */
	public Screen(int width, int height) {
		dim = new Dimension(width, height);
		dm = new DisplayMode(dim.width, dim.height, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice();
	}

	/**
	 *sätter skärmen i fullscreen läget
	 * @param window JFrame
	 * @return true om inget går fel
	 */
	public boolean setFullScreen(JFrame window) {
		window.setUndecorated(true);
		window.setResizable(false);
		vc.setFullScreenWindow(window);
		if (this.dm != null && vc.isDisplayChangeSupported()) {
			try {
				vc.setDisplayMode(this.dm);
			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 *återställer fullscreen läget
	 * @param b true om fönstret skall stängas annars gömmas
	 * @return true om inget går fel
	 */
	public boolean restoreFullScreen(boolean b) {
		Window w = vc.getFullScreenWindow();
		if (w != null && b) {
			w.dispose();
		}
		vc.setFullScreenWindow(null);
		return true;
	}

	/**
	 *sätter skärmen i normal läget med width och height värdena som gränser
	 * @param window JFrame
	 * @return true om inget går fel
	 */
	public boolean setScreen(JFrame window) {
		window.setSize(dim.width, dim.height);
		//window.setUndecorated(true);
		window.setResizable(false);
		window.setVisible(true);
		return true;
	}

	/**
	 *återställer normal läget
	 * @param window om fönstret skall stängas eller bara gömmas
	 * @param b om fönstret skall stängas annars gömmas
	 * @return true om inget går fel
	 */
	public boolean restoreScreen(JFrame window, boolean b) {
		window.setVisible(false);
		if (window != null && b) {
			window.dispose();
		}
		return true;
	}

	/**
	 * @return nuvarande fönstret
	 */
	public Window getFullScreenWindow() {
		return vc.getFullScreenWindow();
	}
}
