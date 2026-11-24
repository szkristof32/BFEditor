package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;

/** Run menu controlling execution and debugging of the active buffer. */
public class RunMenu extends JMenu {

	/** Creates all Run-menu items and keyboard shortcuts. */
	public RunMenu() {
		super("Run");

		JMenuItem executeMenuItem = new JMenuItem(new AbstractAction("Execute") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEngine engine = BFEditor.getInstance().getEngine();
				engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
				engine.execute();
			}
		});
		executeMenuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
		add(executeMenuItem);

		JMenuItem debugMenuItem = new JMenuItem(new AbstractAction("Debug") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEngine engine = BFEditor.getInstance().getEngine();
				engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
				engine.debug();
			}
		});
		debugMenuItem.setAccelerator(KeyStroke.getKeyStroke("control F5"));
		add(debugMenuItem);

		JMenuItem stopMenuItem = new JMenuItem(new AbstractAction("Stop") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().getEngine().stop();
			}
		});
		stopMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift F5"));
		add(stopMenuItem);

		JMenuItem stepMenuItem = new JMenuItem(new AbstractAction("Next instruction") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().getEngine().nextInstruction();
			}
		});
		stepMenuItem.setAccelerator(KeyStroke.getKeyStroke("F8"));
		add(stepMenuItem);
	}

}
