package circlespin.state;

import java.io.File;

import circlespin.world.World;

public class GameState extends State {

	World world;

	@Override
	public void OnLoad() {
		world = new World(new File("assets/maps/maps.txt"), 0, 0);
	}

	@Override
	public void Update(double delta) {
		world.GetMan().Update(delta);
	}

	@Override
	public void Render() {
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
