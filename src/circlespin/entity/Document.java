package circlespin.entity;

import circlespin.graphic.Texture;

public class Document extends Entity {

	private double deltas;
	private double defx, defy;

	public Document(double x, double y) {
		super(new Texture("assets/document.png"), x, y, 64, 64);
		this.defx = x;
		this.defy = y;
	}

	@Override
	public void Update(double delta) {
		deltas += delta * 4;
	}

	@Override
	public void Render(double x, double y) {
		x += 8;
		y -= 4;
		super.Render(Math.cos(deltas) + x, Math.sin(deltas) * 10 + y);
	}

	@Override
	public boolean OnHit(Entity entity) {
		return (entity instanceof Player);
	}

}
