package circlespin.world;

import java.util.ArrayList;

import circlespin.data.Vec4;
import circlespin.tile.Tile;

public class Chunk {

	private int width, height;
	private Tile[][] tiles;

	public Chunk(int width, int height) {
		tiles = new Tile[height][width];
		this.width = width;
		this.height = height;
	}

	public void Set(int x, int y, Tile tile) {
		tiles[y][x] = tile;
	}

	public void Render(double x, double y) {
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile != null && !tile.equals(Tile.Air))
					tile.Render(x, y);
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
