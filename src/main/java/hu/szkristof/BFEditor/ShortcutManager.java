package hu.szkristof.BFEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Random;

/** Registers keyboard shortcuts on the main application window. */
public class ShortcutManager {

	private final Random random;

	private final InputMap inputMap;
	private final ActionMap actionMap;

	/** Creates a manager backed by the main window's input and action maps. */
	public ShortcutManager() {
		this.random = new Random();

		this.inputMap = BFEditor.getInstance().getRootPane().getInputMap();
		this.actionMap = BFEditor.getInstance().getRootPane().getActionMap();
	}

	/**
	 * Binds a key stroke to an action in the main window.
	 *
	 * @param key key stroke that activates the action
	 * @param action action to invoke
	 */
	public void registerShortcut(KeyStroke key, BFAction action) {
		long mapKey = random.nextLong();
		inputMap.put(key, mapKey);
		actionMap.put(mapKey, new AbstractAction() {
			/** Invokes the action bound to this shortcut. */
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed();
			}
		});
	}

}
