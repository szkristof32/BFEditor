package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.editorPanel.FileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RunMenu extends JMenu {

	public RunMenu() {
		super("Run");

		JMenuItem executeMenuItem = new JMenuItem(new AbstractAction("Execute") {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		executeMenuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
		add(executeMenuItem);

		JMenuItem debugMenuItem = new JMenuItem(new AbstractAction("Debug") {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		debugMenuItem.setAccelerator(KeyStroke.getKeyStroke("control F5"));
		add(debugMenuItem);

		JMenuItem stopMenuItem = new JMenuItem(new AbstractAction("Stop") {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		stopMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift F5"));
		add(stopMenuItem);

		JMenuItem stepMenuItem = new JMenuItem(new AbstractAction("Step") {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		stepMenuItem.setAccelerator(KeyStroke.getKeyStroke("F8"));
		add(stepMenuItem);
	}

}
