package circlespin.entity;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import circlespin.Engine;
import circlespin.data.Vec2;
import circlespin.graphic.Texture;
import circlespin.state.GameState;

public class Man extends Entity {
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	private double animationCount = 0;
	private boolean lookLeft = true;

	public Man(double x, double y) {
		super(new Texture("assets/man.png"), x, y, 64 - 1, 64 - 1 /*
																	 * HAX TO
																	 * MAEK IT
																	 * WURK
																	 */);

		textures.add(texture);
		textures.add(new Texture("assets/man2.png"));
		textures.add(new Texture("assets/man3.png"));
		textures.add(new Texture("assets/man4.png"));
		textures.add(new Texture("assets/man5.png"));
		textures.add(new Texture("assets/man6.png"));
	}

	@Override
	public void Update(double delta) {
		animationCount += delta;
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

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			dvx -= delta * speed;
			lookLeft = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			dvx += delta * speed;
			lookLeft = false;
		}

		GameState gameState = (GameState) Engine.Get().GetState();
		pos.Update(delta, dvx, dvy, gameState.GetWorld().GetHitboxes(),
				new Vec2(0, 0));
	}

	@Override
	public void Render(double x, double y) {
		textures.get((int) (Math.floor(animationCount * 8) % 4)).bind();
		// texture.bind();
		GL11.glBegin(GL11.GL_QUADS);

		if (lookLeft)
			GL11.glTexCoord2d(0, 0);
		else
			GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(x, y);

		if (lookLeft)
			GL11.glTexCoord2d(1, 0);
		else
			GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(x + w, y);

		if (lookLeft)
			GL11.glTexCoord2d(1, 1);
		else
			GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(x + w, y + h);

		if (lookLeft)
			GL11.glTexCoord2d(0, 1);
		else
			GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(x, y + h);

		GL11.glEnd();
	}
}
