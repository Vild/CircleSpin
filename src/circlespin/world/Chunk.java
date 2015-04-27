package circlespin.world;

import circlespin.Engine;
import circlespin.data.Vec4;
import circlespin.entity.Man;
import circlespin.tile.Tile;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Chunk {

  protected World world;
  protected int width, height;
  protected Tile[][] tiles;

  public Chunk(World world, int width, int height) {
    tiles = new Tile[height][width];
    this.world = world;
    this.width = width;
    this.height = height;
  }

  public void Set(int x, int y, Tile tile) {
    tiles[y][x] = tile;
  }

  public void Render(double x, double y) {
    final Man man = world.Get(Man.class);
    final double px = man.GetPos().getX() - Engine.GetWidth() / 2 + 32;
    final double py = man.GetPos().getY() - Engine.GetHeight() / 2 + 32;
    for (Tile[] row : tiles) {
      for (Tile tile : row) {
        if (tile != null && !tile.equals(Tile.Air)) {
          GL11.glColor3d(0.1, 0.1, 0.1);
          tile.Render(x, y);
          double dist = Math.sqrt(
              Math.pow(x + px - man.GetPos().getX() + 32, 2) +
                  Math.pow(y + py - man.GetPos().getY() + 32, 2));
          double color = 64*16 - Math.max(Math.min(dist, 64*16), 1);
          color /= 64*16;
          GL11.glColor3d(color, color, color);
          tile.Render(x, y);
          GL11.glColor3d(1, 1, 1);

        }
        x += Tile.width;
      }
      x -= width * Tile.width;
      y += Tile.height;
    }
  }

  public Tile[][] GetTiles() {
    return tiles;
  }

  public ArrayList<Vec4> GetHitboxes(double x, double y) {
    ArrayList<Vec4> hitboxes = new ArrayList<Vec4>();
    for (Tile[] row : tiles) {
      for (Tile tile : row) {
        if (tile != null && tile.isSolid())
          hitboxes.add(new Vec4(x, y, Tile.width, Tile.height));
        x += Tile.width;
      }
      x -= width * Tile.width;
      y += Tile.height;
    }
    return hitboxes;
  }

  public int GetWidth() {
    return width;
  }

  public int GetHeight() {
    return height;
  }
}
