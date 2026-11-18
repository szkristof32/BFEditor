package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.editorPanel.FileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditMenu extends JMenu {

	public EditMenu() {
		super("Edit");

		JMenuItem undoMenuItem = new JMenuItem(new AbstractAction("Undo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
				currentEditor.getEditHistory().undo();
				currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
			}
		});
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Z"));
		add(undoMenuItem);

		JMenuItem redoMenuItem = new JMenuItem(new AbstractAction("Redo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
				currentEditor.getEditHistory().redo();
				currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
			}
		});
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Y"));
		add(redoMenuItem);
		addSeparator();

		JMenuItem cutMenuItem = new JMenuItem(new AbstractAction("Cut") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
				currentEditor.cut();
				currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
			}
		});
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke("control X"));
		add(cutMenuItem);

		JMenuItem copyMenuItem = new JMenuItem(new AbstractAction("Copy") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
				currentEditor.copy();
			}
		});
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke("control C"));
		add(copyMenuItem);

		JMenuItem pasteMenuItem = new JMenuItem(new AbstractAction("Paste") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor currentEditor = BFEditor.getInstance().getEditorPanel().getCurrentEditor();
				currentEditor.paste();
				currentEditor.setBuffer(currentEditor.getEditHistory().getCurrentState());
			}
		});
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke("control V"));
		add(pasteMenuItem);
		addSeparator();

		JMenuItem preferencesMenuItem = new JMenuItem(new AbstractAction("Preferences") {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke("control P"));
		add(preferencesMenuItem);
	}

}
