package circlespin.data;

import circlespin.physics.AABB;

import java.io.*;
import java.util.Vector;

public class SaveFile {
	private String file;
	private AABB playerPosition;
	private Vector<Integer> documentsTaken;

	public Vector<Integer> getDocumentsTaken() {
		return documentsTaken;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void Save() throws IOException {
		if (file.isEmpty())
			throw new FileNotFoundException("You need to set the file path first");
	try {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(file)));
		out.writeObject(playerPosition);
		out.writeObject(documentsTaken);
		out.close();
	} catch (IOException e) {
		e.printStackTrace();
		throw new IOException("Failed to save the file!", e);
	}
}

	public void Load() throws IOException {
		if (file.isEmpty())
			throw new FileNotFoundException("You need to set the file path first");
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(file)));
			playerPosition = (AABB) in.readObject();
			documentsTaken = (Vector<Integer>) in.readObject();
			in.close();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Failed to load the file!", e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Invalid save file!");
			System.exit(0);
		}
	}

	public AABB getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(AABB playerPosition) {
		this.playerPosition = playerPosition;
	}

	public void setDocumentsTaken(Vector<Integer> documentsTaken) {
		this.documentsTaken = documentsTaken;
	}
}
