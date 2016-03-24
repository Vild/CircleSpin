package circlespin.world;

import circlespin.Engine;
import circlespin.data.Vec4;
import circlespin.entity.Document;
import circlespin.entity.Entity;
import circlespin.entity.Player;
import circlespin.entity.QuitNode;
import circlespin.physics.AABB;
import circlespin.tile.Tile;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

public class World {

	int width, height;
	int chunkWidth, chunkHeight;
	double x, y;

	Chunk[][] chunks;
	AABB[] hitboxes;
	ArrayList<Entity> entities;

	public Vector<Integer> getDocumentsTaken() {
		return documentsTaken;
	}

	Vector<Integer> documentsTaken;


	public World(File file, double x, double y) {
		this(file, x, y, new Vector<Integer>());
	}

	public World(File file, double x, double y, Vector<Integer> documentsTaken) {
		this.x = x;
		this.y = y;
		this.entities = new ArrayList<>();
		this.documentsTaken = documentsTaken;
		processData(loadFile(file));
	}


	/**
	 * Loads the world for a file
	 * <p>
	 * ABI:
	 * WorldWidth WorldHeight
	 * ChunkWidth ChunkHeight
	 * ;; <--- Splits header from data
	 * XXXX
	 * XXXX
	 * XXXX
	 * XXXX
	 * :: <--- Splits each chunk from eachother, chunk sorting is from left->right, top->bottom
	 * XXXX
	 * XXXX
	 * XXXX
	 * XXXX
	 * <p>
	 * Please note last chunk will be empty so the file does not need to contain it, because it
	 * will skip it anyway.
	 *
	 * @param loadFile
	 */
	private void processData(String loadFile) {
		Entity.ResetIDCounter();
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

		for (int i = 0; i < width * height; i++) {
			Chunk chunk = new Chunk(this, chunkWidth, chunkHeight);

			String data = chunksData[i];

			String[] rows = data.split("\n");

			int y = 0;
			for (String row : rows) {
				for (int j = 0; j < row.length(); j++)
					processTile(chunk, row.charAt(j), j, y, i % width, i
									/ width);
				y++;
			}

			chunks[i / width][i % width] = chunk;
		}
		//chunks[height - 1][width - 1] = new BlankChunk(this, chunkWidth, chunkHeight);
	}

	private void processTile(Chunk chunk, char tile, int x, int y, int wx,
													 int wy) {
		double realX = (wx * chunkWidth + x) * Tile.width + this.x;
		double realY = (wy * chunkHeight + y) * Tile.height + this.y;
		switch (tile) {
			case '#':
				chunk.Set(x, y, Tile.Stone);
				break;
			case '$':
				chunk.Set(x, y, Tile.BlueStone);
				break;
			case 'E':
				chunk.Set(x, y, Tile.Exit);
				break;
			case 'P':
				entities.add(new Player(realX, realY));
				chunk.Set(x, y, Tile.Air);
				break;
			case 'D':
				Document doc = new Document(realX, realY);
				boolean shouldAdd = true;
				for (int i = 0; i < documentsTaken.size() && shouldAdd; i++)
					if (documentsTaken.get(i).intValue() == doc.GetId())
						shouldAdd = false;
				if (shouldAdd)
					entities.add(doc);
				chunk.Set(x, y, Tile.Air);
				break;
			case 'X':
				entities.add(new QuitNode(realX, realY));
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

	public void Update(double delta) {
		final Player player = Get(Player.class);
		for (Entity entity : entities)
			entity.Update(delta);

		ArrayList<Entity> toRemove = new ArrayList<Entity>();

		for (Entity entity : entities)
			if (entity.GetPos().Hit(player.GetPos()))
				if (entity.OnHit(player))
					toRemove.add(entity);

		for (Entity entity : toRemove) {
			if (entity instanceof Document)
				documentsTaken.add(new Integer(entity.GetId()));
			entities.remove(entity);
		}
	}

	public void Render() {
		final Player player = Get(Player.class);
		final double px = player.GetPos().getX() - Engine.GetWidth() / 2 + 32;
		final double py = player.GetPos().getY() - Engine.GetHeight() / 2 + 32;

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				chunks[y][x].Render(x * chunkWidth * Tile.width + this.x - px,
								y * chunkHeight * Tile.height + this.y - py);

		for (Entity entity : entities)
			if (entity instanceof Player)
				entity.Render(Engine.GetWidth() / 2 - 64 / 2, Engine.GetHeight() / 2 - 64 / 2);
			else
				entity.Render(x * chunkWidth * Tile.width + this.x - px
								+ entity.GetPos().getX(), y * chunkHeight * Tile.height
								+ this.y - py + entity.GetPos().getY());


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

	public Chunk[][] GetChunks() {
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

	public <T> T Get(Class clazz) {
		final T[] ret = (T[]) new Object[]{null};
		entities.forEach((entity) -> {
			if (entity.getClass().equals(clazz))
				ret[0] = (T) entity;
		});
		return ret[0];
	}

	public void OnKey(int key, boolean isDown) {
		if (isDown) {
			int x = 0, y = 0;
			loops:
			for (y = 0; y < chunks.length; y++)
				for (x = 0; x < chunks[y].length; x++)
					if (chunks[y][x] instanceof BlankChunk)
						break loops;

			if (y >= chunks.length) // Failed to find it
				return;

			if (key == Keyboard.KEY_UP) {
				if (y >= chunks.length - 1)
					return;

				Chunk blank = chunks[y][x];
				chunks[y][x] = chunks[y + 1][x];
				chunks[y + 1][x] = blank;
			} else if (key == Keyboard.KEY_DOWN) {
				if (y == 0)
					return;

				Chunk blank = chunks[y][x];
				chunks[y][x] = chunks[y - 1][x];
				chunks[y - 1][x] = blank;
			} else if (key == Keyboard.KEY_LEFT) {
				if (x >= chunks[0].length - 1)
					return;

				Chunk blank = chunks[y][x];
				chunks[y][x] = chunks[y][x + 1];
				chunks[y][x + 1] = blank;
			} else if (key == Keyboard.KEY_RIGHT) {
				if (x == 0)
					return;

				Chunk blank = chunks[y][x];
				chunks[y][x] = chunks[y][x - 1];
				chunks[y][x - 1] = blank;
			}
		}
	}
}
