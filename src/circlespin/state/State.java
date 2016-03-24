package circlespin.state;

import circlespin.Engine;

public abstract class State {

	public abstract void OnLoad();

	public abstract void Update(double delta);

	public abstract void Render();

	public abstract void OnUnload();

	public abstract void Store(Engine engine);

	public abstract void Restore(Engine engine);
}
