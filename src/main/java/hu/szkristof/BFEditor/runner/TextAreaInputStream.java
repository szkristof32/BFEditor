package hu.szkristof.BFEditor.runner;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TextAreaInputStream extends InputStream {

	private Queue<Character> characters = new ConcurrentLinkedQueue<>();

	public TextAreaInputStream(JTextArea textArea) {
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				characters.add(e.getKeyChar());
			}
		});
	}

	@Override
	public int read() {
		while (characters.isEmpty()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}
		return characters.poll();
	}

}
