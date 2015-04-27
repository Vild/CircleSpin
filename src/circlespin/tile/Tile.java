package circlespin.tile;

import circlespin.graphic.Texture;
import org.lwjgl.opengl.GL11;

public class Tile {

  public static final Tile Exit = new Tile(new Texture(
      "assets/tiles/exit.png"), false);
  public static final int width = 64;
  public static final int height = 64;
  public static Tile Air = new Tile();
  ;
  public static Tile Stone = new Tile(new Texture("assets/tiles/stone.png"));
  public static Tile BlueStone = new Tile(new Texture(
      "assets/tiles/stone2.png"));
  private static int ID_COUNT = 0;
  int id;
  Texture texture;
  boolean air;
  boolean solid;

  public Tile() {
    this.id = ID_COUNT++;
    this.air = true;
    this.solid = false;
  }

  public Tile(Texture texture) {
    this.id = ID_COUNT++;
    this.texture = texture;
    this.solid = true;
  }

  public Tile(Texture texture, boolean solid) {
    this.id = ID_COUNT++;
    this.texture = texture;
    this.solid = solid;
  }

  public void Render(double x, double y) {
    if (air)
      return;
    texture.bind();

    GL11.glBegin(GL11.GL_QUADS);

    GL11.glTexCoord2d(0, 0);
    GL11.glVertex2d(x, y);

    GL11.glTexCoord2d(1, 0);
    GL11.glVertex2d(x + width, y);

    GL11.glTexCoord2d(1, 1);
    GL11.glVertex2d(x + width, y + height);

    GL11.glTexCoord2d(0, 1);
    GL11.glVertex2d(x, y + height);

    GL11.glEnd();
  }

  public int GetID() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Tile)
      return this.id == ((Tile) other).GetID();
    return false;
  }

  public boolean isSolid() {
    return solid;
  }

}
