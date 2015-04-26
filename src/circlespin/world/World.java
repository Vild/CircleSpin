package circlespin.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import circlespin.Engine;
import circlespin.data.Vec4;
import circlespin.entity.Man;
import circlespin.physics.AABB;
import circlespin.tile.Tile;

public class World {

	int width, height;
	int chunkWidth, chunkHeight;
	double x, y;

	Chunk[][] chunks;
	AABB[] hitboxes;

	Man man;

	public World(File file, double x, double y) {
		this.x = x;
		this.y = y;
		processData(loadFile(file));
	}

	private void processData(String loadFile) {
		String[] header = loadFile.split("\n;;\n");
		String[] headerLine = header[0].split("\n");
		String[] worldSize = headerLine[0].trim().split(" ");
		width = Integer.parseInt(worldSize[0]);
		height = Integer.parseInt(worldSize[1]);

		chunks = new Chunk[height][width];

		String[] chunkSize = headerLine[1].trim().split(" ");
		chunkWidth = Integer.parseInt(chunkSize[0]);
		chunkHeight = Integer.parseInt(chunkSize[1]);

		String[] chunksData = header[1].split("\n::\n");

		for (int i = 0; i < chunksData.length; i++) {
			Chunk chunk = new Chunk(chunkWidth, chunkHeight);

			String data = chunksData[i];

			String[] rows = data.split("\n");

			int y = 0;
			for (String row : rows) {
				for (int j = 0; j < row.length(); j++) {
					processTile(chunk, row.charAt(j), j, y);

				}
				y++;
			}

			chunks[i / width][i % width] = chunk;
		}

	}

	private void processTile(Chunk chunk, char tile, int x, int y) {
		switch (tile) {
		case '#':
			chunk.Set(x, y, Tile.Stone);
			break;
		case 'P':
			man = new Man(x * Tile.width + this.x, y * Tile.height + this.y);
			chunk.Set(x, y, Tile.Air);
			break;
		default:
			chunk.Set(x, y, Tile.Air);
		}

	}

	private String loadFile(File file) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = in.readLine()) != null)
				sb.append(line + "\n");

			in.close();

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return "";
		}
	}

	public void Render() {
		double px = man.GetPos().getX() - Engine.WIDTH / 2 + 32;
		double py = man.GetPos().getY() - Engine.HEIGHT / 2 + 32;

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				chunks[y][x].Render(x * chunkWidth * Tile.width + this.x - px,
						y * chunkHeight * Tile.height + this.y
								- py);
		man.Render(Engine.WIDTH / 2 - 64 / 2, Engine.HEIGHT / 2 - 64 / 2);
	}

	public double GetX() {
		return x;
	}

	public void SetX(double x) {
		this.x = x;
	}

	public double GetY() {
		return y;
	}

	public void SetY(double y) {
		this.y = y;
	}

	public int GetWidth() {
		return width;
	}

	public int GetHeight() {
		return height;
	}

	public int GetChunkWidth() {
		return chunkWidth;
	}

	public int GetChunkHeight() {
		return chunkHeight;
	}

	public Chunk[][] getChunks() {
		return chunks;
	}

	public ArrayList<Vec4> GetHitboxes() {
		ArrayList<Vec4> hitboxes = new ArrayList<Vec4>();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				hitboxes.addAll(chunks[y][x].GetHitboxes(x * chunkWidth
						* Tile.width + this.x, y * chunkHeight * Tile.height
						+ this.y));
		return hitboxes;
	}

	public Man GetMan() {
		return man;
	}

}
