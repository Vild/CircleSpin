package circlespin.entity;

import org.lwjgl.input.Keyboard;

import circlespin.Engine;
import circlespin.data.Vec2;
import circlespin.graphic.Texture;
import circlespin.state.GameState;

public class Man extends Entity {
	public Man(double x, double y) {
		super(new Texture("assets/man.png"), x, y, 64 - 0.01, 64 - 0.01 /*
																		 * HAX
																		 * TO
																		 * MAEK
																		 * IT
																		 * WORK
																		 */);
	}

	@Override
	public void Update(double delta) {
		double speed = 400;
		double gravity = delta * speed * 5;
		double dvx = 0;
		double dvy = 0;

		dvy += gravity;

		dvx -= pos.getVx() * delta * 1.15;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && pos.HitGround())
			dvy -= speed * 2;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			dvy += delta * speed;

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			dvx -= delta * speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			dvx += delta * speed;

		GameState gameState = (GameState) Engine.Get().GetState();
		pos.Update(delta, dvx, dvy, gameState.GetWorld().GetHitboxes(),
				new Vec2(0, 0));
	}
}
