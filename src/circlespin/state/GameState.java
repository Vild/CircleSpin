package circlespin.state;

import circlespin.Engine;
import circlespin.entity.Player;
import circlespin.graphic.Texture;
import circlespin.tile.Tile;
import circlespin.world.World;
import org.lwjgl.input.Keyboard;
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
		while (Keyboard.next())
			world.OnKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());

		world.Update(delta);
	}

	@Override
	public void Render() {
		final double LIGHTNING_MAX_DIST = Tile.width * 16 * 0.75;

		final Player player = world.Get(Player.class);

		final double px = player.GetPos().getX() - Engine.GetWidth() / 2 + 32;
		final double py = player.GetPos().getY() - Engine.GetHeight() / 2 + 32;
		for (int y = 0; y < Engine.GetHeight() / Tile.height + 2; y++)
			for (int x = 0; x < Engine.GetWidth() / Tile.width + 2; x++) {
				double realX = (x + (player.GetPos().getX() / -100. % 1) - 1) * Tile.width;
				double realY = (y + (player.GetPos().getY() / -100. % 1) - 1) * Tile.height;
				double dist = Math.sqrt(Math.pow(realX + px - player.GetPos().getX() + 32, 2) + Math.pow(realY + py - player.GetPos().getY() + 32, 2));
				double color = LIGHTNING_MAX_DIST - Math.max(Math.min(dist, LIGHTNING_MAX_DIST), 1);
				color /= LIGHTNING_MAX_DIST;
				GL11.glColor3d(color, color, color);
				background.Render(realX, realY);
				GL11.glColor3d(1, 1, 1);
			}

		world.Render();
	}

	@Override
	public void OnUnload() {
		world = null;
	}

	@Override
	public void Store(Engine engine) {
		Player p = world.Get(Player.class);
		engine.GetSaveFile().setPlayerPosition(p.GetPos());
		engine.GetSaveFile().setDocumentsTaken(world.getDocumentsTaken());
	}

	@Override
	public void Restore(Engine engine) {
		world = new World(new File("assets/maps/maps.txt"), 0, 0, engine.GetSaveFile().getDocumentsTaken());
		Player p = world.Get(Player.class);
		p.SetPos(engine.GetSaveFile().getPlayerPosition());
	}

	public World GetWorld() {
		return world;
	}

}
