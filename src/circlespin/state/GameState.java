package circlespin.state;

import circlespin.Engine;
import circlespin.entity.Man;
import circlespin.graphic.Texture;
import circlespin.tile.Tile;
import circlespin.world.World;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class GameState extends State {
  World world;
  Tile background = new Tile(new Texture("assets/back.png"));

  @Override
  public void OnLoad() {
    world = new World(new File("assets/maps/maps.txt"), 0, 0);
  }

  @Override
  public void Update(double delta) {
    world.Update(delta);
  }

  @Override
  public void Render() {
    final Man man = world.Get(Man.class);
    for (int y = 0; y < Engine.GetHeight() / Tile.height + 2; y++)
      for (int x = 0; x < Engine.GetWidth() / Tile.width + 2; x++)
        background.Render((x + (man.GetPos().getX() / -100. % 1) - 1) * Tile.width,
            (y + (man.GetPos().getY() / -100. % 1) - 1) * Tile.height);

    world.Render();
  }

  @Override
  public void OnUnload() {
    world = null;
  }

  public World GetWorld() {
    return world;
  }

}
