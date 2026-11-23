package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFAction;
import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.DialogThread;
import hu.szkristof.BFEditor.ShortcutManager;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileMenu extends JMenu {

	public FileMenu() {
		super("File");

		JMenuItem newMenuItem = createMenu("New", KeyStroke.getKeyStroke("control N"), this::newFile);
		add(newMenuItem);

		JMenuItem openMenuItem = createMenu("Open", KeyStroke.getKeyStroke("control O"), this::openFile);
		add(openMenuItem);

		JMenuItem saveMenuItem = createMenu("Save", KeyStroke.getKeyStroke("control S"), this::saveFile);
		add(saveMenuItem);

		JMenuItem saveAsMenuItem = createMenu("Save", KeyStroke.getKeyStroke("control shift S"), this::saveFileAs);
		add(saveAsMenuItem);

		addSeparator();

		JMenuItem exitMenuItem = createMenu("Exit", KeyStroke.getKeyStroke("alt F4"), BFEditor.getInstance()::requestClose);
		add(exitMenuItem);
	}

	private JMenuItem createMenu(String name, KeyStroke keyStroke, BFAction action) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setAccelerator(keyStroke);
		menuItem.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed();
			}
		});
		BFEditor.getInstance().getShortcutManager().registerShortcut(keyStroke, action);
		return menuItem;
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
