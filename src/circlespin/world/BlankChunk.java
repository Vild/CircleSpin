package circlespin.world;

import circlespin.tile.Tile;

public class BlankChunk extends Chunk {
	public BlankChunk(World world, int width, int height) {
		super(world, width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				tiles[y][x] = Tile.Air;
	}
}
