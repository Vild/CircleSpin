package circlespin.tile;

import org.lwjgl.opengl.GL11;

import circlespin.graphic.Renderable;
import circlespin.graphic.Texture;

public class Tile {

	public static Tile Air = new Tile();
	public static Tile Stone = new Tile(new Texture("assets/tiles/stone.png"));

	private static int ID_COUNT = 0;

	int id;
	Texture texture;
	public static final int width = 64;
	public static final int height = 64;
	boolean air;

	public Tile() {
		this.id = ID_COUNT++;
		this.air = true;
	}

	public Tile(Texture texture) {
		this.id = ID_COUNT++;
		this.texture = texture;
		this.air = false;
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
		return !air;
	}

}
