package circlespin.swing;

import circlespin.Engine;
import circlespin.data.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainWindow extends JFrame {
	private Engine engine;
	private Canvas canvas;
	private MenuListener menuListener;

	public MainWindow(Engine engine, int width, int height) {
		this.engine = engine;
		menuListener = new MenuListener();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("CircleSpin");
		setResizable(false);

		add(canvas = new Canvas());
		canvas.setSize(width, height);


		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenuItem item;
		menuBar.add(item = new JMenuItem("Save"));
		item.addActionListener(menuListener);
		menuBar.add(item = new JMenuItem("Load"));
		item.addActionListener(menuListener);
		menuBar.add(item = new JMenuItem("Quit"));
		item.addActionListener(menuListener);

		setSize(width, height + menuBar.getHeight());

		canvas.setFocusable(true);
		setVisible(true);
	}

	public Canvas GetCanvas() {
		return canvas;
	}

	private class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JMenuItem item = (JMenuItem) actionEvent.getSource();

			engine.RunThis(new Runnable() {
				public void run() {

					SaveFile saveFile = engine.GetSaveFile();

					boolean done = false;
					switch (item.getText()) {
						case "Save":
							while (!done) {
								saveFile.setFile((String) JOptionPane.showInputDialog(null, "Where do you want to save?", "Save path", JOptionPane.QUESTION_MESSAGE, null, null, saveFile.getFile()));
								try {
									engine.Store();
									saveFile.Save();
									done = true;
								} catch (FileNotFoundException e) {
									done = JOptionPane.showConfirmDialog(null, "Invalid file path!\nDo you want to try again?", "Error", JOptionPane.YES_NO_OPTION) == 1 /* No */;
								} catch (IOException e) {
									e.printStackTrace();
									done = JOptionPane.showConfirmDialog(null, "Failed to save!\nDo you want to try again?", "Error", JOptionPane.YES_NO_OPTION) == 1 /* No */;
								}
							}
							break;
						case "Load":
							while (!done) {
								saveFile.setFile((String) JOptionPane.showInputDialog(null, "What file do you want to load?", "Load path", JOptionPane.QUESTION_MESSAGE, null, null, saveFile.getFile()));
								try {
									saveFile.Load();
									engine.Restore();
									done = true;
								} catch (FileNotFoundException e) {
									done = JOptionPane.showConfirmDialog(null, "Invalid file path!\nDo you want to try again?", "Error", JOptionPane.YES_NO_OPTION) == 1 /* No */;
								} catch (IOException e) {
									e.printStackTrace();
									done = JOptionPane.showConfirmDialog(null, "Failed to save!\nDo you want to try again?", "Error", JOptionPane.YES_NO_OPTION) == 1 /* No */;
								}
							}
							break;
						case "Quit":
							setVisible(false);
							break;
					}
				}
			});
		}
	}
}
