package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFAction;
import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;

/** Run menu controlling execution and debugging of the active buffer. */
public class RunMenu extends JMenu {

	/** Creates all Run-menu items and keyboard shortcuts. */
	public RunMenu() {
		super("Run");

		JMenuItem executeMenuItem = createMenu("Execute", KeyStroke.getKeyStroke("F5"), this::execute);
		add(executeMenuItem);

		JMenuItem debugMenuItem = createMenu("Debug", KeyStroke.getKeyStroke("control F5"), this::debug);
		add(debugMenuItem);

		JMenuItem stopMenuItem = createMenu("Stop", KeyStroke.getKeyStroke("shift F5"), this::stop);
		add(stopMenuItem);

		JMenuItem stepMenuItem = createMenu("Next instruction", KeyStroke.getKeyStroke("F7"), this::nextInstruction);
		add(stepMenuItem);
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

	private void execute(){
		BFEngine engine = BFEditor.getInstance().getEngine();
		engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
		engine.execute();
	}

	private void debug(){
		BFEngine engine = BFEditor.getInstance().getEngine();
		engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
		engine.debug();
		BFEditor.getInstance().getRunnerPanel().updateRuntimeState();
	}

	private void stop(){
		BFEditor.getInstance().getEngine().stop();
		BFEditor.getInstance().getRunnerPanel().updateRuntimeState();
	}

	private void nextInstruction(){
		BFEditor.getInstance().getEngine().nextInstruction();
		BFEditor.getInstance().getRunnerPanel().updateRuntimeState();
	}

}
