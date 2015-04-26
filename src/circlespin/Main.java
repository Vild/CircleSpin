package circlespin;

import circlespin.state.GameState;

public class Main {
	public static void main(String[] args) {
		Engine engine = Engine.Get();
		engine.SetState(new GameState());
		engine.Run();
	}

}