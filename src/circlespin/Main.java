package circlespin;

import circlespin.state.GameState;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class Main {
	public static void main(String[] args) {
		try {
			AddDir("native");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Engine engine = Engine.Get();
		engine.SetState(new GameState());
		engine.Run();
		System.exit(0);
	}

	public static void AddDir(String s) throws IOException {
		try {
			// This enables the java.library.path to be modified at runtime
			// From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
			//
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = s;
			field.set(null, tmp);
			System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException("Failed to get field handle to set library path");
		}
	}
}