package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.DialogThread;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileMenu extends JMenu {

	public FileMenu() {
		super("File");

		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		JMenuItem newMenuItem = new JMenuItem(new AbstractAction("New") {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
		add(newMenuItem);

		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control N"), "new file", new AbstractAction("") {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});

		JMenuItem openMenuItem = new JMenuItem(new AbstractAction("Open") {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
		add(openMenuItem);

		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control O"), "open file", new AbstractAction("") {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});

		JMenuItem saveMenuItem = new JMenuItem(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
		add(saveMenuItem);

		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control S"), "save file", new AbstractAction("") {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

		JMenuItem saveAsMenuItem = new JMenuItem(new AbstractAction("Save As") {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		add(saveAsMenuItem);

		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control shift S"), "save file as", new AbstractAction("") {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});

		addSeparator();

		JMenuItem exitMenuItem = new JMenuItem(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().requestClose();
			}
		});
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		add(exitMenuItem);
	}

	private void addShortcut(InputMap inputMap, ActionMap actionMap, KeyStroke keyStroke, Object key, AbstractAction action) {
		inputMap.put(keyStroke, key);
		actionMap.put(key, action);
	}

	private void newFile() {
		BFEditor.getInstance().getEditorPanel().newFile();
	}

	private void openFile() {
		DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
		dialogThread.openFile();
		try {
			File file = dialogThread.getLastFile();
			if (file != null)
				BFEditor.getInstance().getEditorPanel().openFile(file);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void saveFile() {
		boolean success = BFEditor.getInstance().getEditorPanel().saveCurrentFile();
		if (success) return;

		DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
		dialogThread.saveFile();
		try {
			File file = dialogThread.getLastFile();
			if (file != null)
				BFEditor.getInstance().getEditorPanel().saveFile(file);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void saveFileAs() {
		DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
		dialogThread.saveFile();
		try {
			File file = dialogThread.getLastFile();
			if (file != null)
				BFEditor.getInstance().getEditorPanel().saveFile(file);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
