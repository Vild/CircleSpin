package circlespin.graphic;

import org.lwjgl.opengl.GL11;

public abstract class Renderable {

  protected Texture texture;
  protected double w, h;

  public Renderable(Texture texture, double w, double h) {
    this.texture = texture;
    this.w = w;
    this.h = h;
  }

  public void Render(double x, double y) {
    texture.bind();
    GL11.glBegin(GL11.GL_QUADS);

    GL11.glTexCoord2d(0, 0);
    GL11.glVertex2d(x, y);

    GL11.glTexCoord2d(1, 0);
    GL11.glVertex2d(x + w, y);

    GL11.glTexCoord2d(1, 1);
    GL11.glVertex2d(x + w, y + h);

    GL11.glTexCoord2d(0, 1);
    GL11.glVertex2d(x, y + h);

    GL11.glEnd();
  }
}
