package circlespin.world;

import circlespin.Engine;
import circlespin.data.Vec4;
import circlespin.entity.Player;
import circlespin.tile.Tile;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Chunk {

	public static final double LIGHTNING_MAX_DIST = Tile.width * 16 * 0.5;
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
		final Player player = world.Get(Player.class);
		final double px = player.GetPos().getX() - Engine.GetWidth() / 2 + 32;
		final double py = player.GetPos().getY() - Engine.GetHeight() / 2 + 32;
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile != null && !tile.equals(Tile.Air)) {
					double dist = Math.sqrt(
									Math.pow(x + px - player.GetPos().getX() + 32, 2) +
													Math.pow(y + py - player.GetPos().getY() + 32, 2));
					double color = LIGHTNING_MAX_DIST - Math.max(Math.min(dist, LIGHTNING_MAX_DIST), 1);
					color /= LIGHTNING_MAX_DIST;
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
