package circlespin.state;

import java.io.File;

import circlespin.Engine;
import circlespin.graphic.Texture;
import circlespin.tile.Tile;
import circlespin.world.World;

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
		for (int y = 0; y < Engine.HEIGHT / Tile.height + 2; y++)
			for (int x = 0; x < Engine.WIDTH / Tile.width + 2; x++)
				background.Render(x * Tile.width
						+ (world.GetMan().GetPos().getX() /100. % 1), y
						* Tile.height
						+ (world.GetMan().GetPos().getY() /100. % 1));
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
