package circlespin;

import circlespin.state.GameState;
import circlespin.state.State;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Engine {

  private static int width = 1280;
  private static int height = 720;
  private static Engine engine = null;
  private State state = null;
  private static double zoom = 1;

  public Engine() {
    try {
      Display.setDisplayMode(new DisplayMode(width, height));
      Display.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    }

    setupGL();
  }

  private void setupGL() {
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0, width * zoom, height * zoom, 0, 1, -1);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  }

  public static Engine Get() {
    if (engine == null)
      engine = new Engine();
    return engine;
  }

  public void Run() {

    double last = CurNano();
    int fps = 0;
    double lastfps = CurNano();
    GL11.glClearColor(0, 0, 0, 1);

    while (!Display.isCloseRequested()) {
      boolean zoomChanged = false;
      double delta = CurNano() - last;
      last = CurNano();

      if (state == null)
        break;

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        break;


      if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) {
        zoom += 1 * delta;
        zoomChanged = true;
      }

      if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) {
        zoom -= 1 * delta;
        zoomChanged = true;
      }

      if (zoomChanged) {
        zoom = Math.max(Math.min(zoom, 10), 1);
        setupGL();
      }
      state.Update(delta);

      if (state == null)
        break;
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

  public static double GetWidth() { return width * zoom; }
  public static double GetHeight() { return height * zoom; }
}
