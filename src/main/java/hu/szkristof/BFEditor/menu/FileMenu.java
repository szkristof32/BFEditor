package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFAction;
import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.DialogThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/** File menu providing new, open, save, save-as, and exit actions. */
public class FileMenu extends JMenu {

	/** Creates all File-menu items and keyboard shortcuts. */
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
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
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
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
		}
	}

	private void saveFileAs() {
		DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
		dialogThread.saveFile();
		try {
			File file = dialogThread.getLastFile();
			if (file != null)
				BFEditor.getInstance().getEditorPanel().saveFile(file);
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
		}
	}

}
