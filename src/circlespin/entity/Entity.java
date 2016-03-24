package circlespin.entity;

import circlespin.graphic.Renderable;
import circlespin.graphic.Texture;
import circlespin.physics.AABB;

import java.io.Serializable;

public abstract class Entity extends Renderable {

	private static int idCounter = 0;
	protected int id;
	protected AABB pos;

	public Entity(Texture texture, double x, double y, double w, double h) {
		super(texture, w, h);
		id = idCounter++;
		this.pos = new AABB(x, y, w, h, 0, 0, 40);
	}

	public abstract void Update(double delta);

	public int GetId() {
		return id;
	}

	public AABB GetPos() {
		return pos;
	}

	public void SetPos(AABB pos) {
		this.pos = pos;
	}

	public boolean OnHit(Entity entity) {
		return false;
	}

	public static void ResetIDCounter() {
		idCounter = 0;
	}
}
