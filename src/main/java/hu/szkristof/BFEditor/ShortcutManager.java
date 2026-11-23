package hu.szkristof.BFEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ShortcutManager {

	private Random random;

	private InputMap inputMap;
	private ActionMap actionMap;

	public ShortcutManager() {
		this.random = new Random();

		this.inputMap = BFEditor.getInstance().getRootPane().getInputMap();
		this.actionMap = BFEditor.getInstance().getRootPane().getActionMap();
	}

	public void registerShortcut(KeyStroke key, BFAction action) {
		long mapKey = random.nextLong();
		inputMap.put(key, mapKey);
		actionMap.put(mapKey, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed();
			}
		});
	}

}
