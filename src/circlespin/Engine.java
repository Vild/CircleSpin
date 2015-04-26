package circlespin;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import circlespin.state.GameState;
import circlespin.state.State;

public class Engine {

	private static Engine engine = null;

	public static Engine Get() {
		if (engine == null)
			engine = new Engine();
		return engine;
	}

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

	private State state = null;

	public Engine() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void Run() {
		double last = CurNano();
		int fps = 0;
		double lastfps = CurNano();
		GL11.glClearColor(0, 0, 0, 1);

		while (!Display.isCloseRequested()) {
			double delta = CurNano() - last;
			last = CurNano();

			if (state == null)
				break;

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				break;

			state.Update(delta);

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			state.Render();

			fps++;
			if (CurNano() - lastfps >= 1.) {
				Display.setTitle("FPS: " + fps);
				fps = 0;
				lastfps += 1;
			}
			Display.update();
			Display.sync(144);
		}
		Display.destroy();
	}

	private double CurNano() {
		return System.nanoTime() / 1000000000.0;
	}

	public void SetState(GameState gameState) {
		if (state != null)
			state.OnUnload();
		state = gameState;
		if (state != null)
			state.OnLoad();
	}

	public State GetState() {
		return state;
	}
}
