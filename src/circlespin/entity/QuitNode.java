package circlespin.entity;

import circlespin.Engine;
import circlespin.graphic.Texture;

public class QuitNode extends Entity {

  public QuitNode(double x, double y) {
    super(new Texture("assets/exitnode.png"), x, y, 64, 64);
  }

  @Override
  public void Update(double delta) {
  }

  @Override
  public boolean OnHit(Entity entity) {
    Engine.Get().SetState(null);
    return true;
  }

}
