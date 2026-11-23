package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFAction;
import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.editorPanel.FileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditMenu extends JMenu {

	public EditMenu() {
		super("Edit");

		JMenuItem undoMenuItem = createMenu("Undo", KeyStroke.getKeyStroke("control Z"), this::undo);
		add(undoMenuItem);

		JMenuItem redoMenuItem = createMenu("Redo", KeyStroke.getKeyStroke("control Y"), this::redo);
		add(redoMenuItem);
		addSeparator();

		JMenuItem cutMenuItem = createMenu("Cut", KeyStroke.getKeyStroke("control X"), this::cut);
		add(cutMenuItem);

		JMenuItem copyMenuItem = createMenu("Copy", KeyStroke.getKeyStroke("control C"), this::copy);
		add(copyMenuItem);

		JMenuItem pasteMenuItem = createMenu("Paste", KeyStroke.getKeyStroke("control V"), this::paste);
		add(pasteMenuItem);
		addSeparator();

		JMenuItem preferencesMenuItem = createMenu("Preferences", KeyStroke.getKeyStroke("control P"), BFEditor.getInstance()::openPreferences);
		add(preferencesMenuItem);
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

	private void undo() {
		FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
		currentEditor.getEditHistory().undo();
		currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
	}

	private void redo() {
		FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
		currentEditor.getEditHistory().redo();
		currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
	}

	private void cut() {
		FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
		currentEditor.cut();
		currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
	}

	private void copy() {
		BFEditor.getInstance().getEditorPanel().getCurrentEditor().copy();
	}

	private void paste() {
		FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
		currentEditor.paste();
		currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
	}

}
