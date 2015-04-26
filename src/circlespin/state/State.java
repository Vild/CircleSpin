package circlespin.state;

public abstract class State {

	public abstract void OnLoad();

	public abstract void Update(double delta);

	public abstract void Render();

	public abstract void OnUnload();

}
