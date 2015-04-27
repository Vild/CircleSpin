package circlespin.entity;

import circlespin.graphic.Renderable;
import circlespin.graphic.Texture;
import circlespin.physics.AABB;

public abstract class Entity extends Renderable {

  protected AABB pos;

  public Entity(Texture texture, double x, double y, double w, double h) {
    super(texture, w, h);
    this.pos = new AABB(x, y, w, h, 0, 0, 40);
  }

  public abstract void Update(double delta);

  public AABB GetPos() {
    return pos;
  }

  public boolean OnHit(Entity entity) {
    return false;
  }
}
