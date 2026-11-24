package hu.szkristof.BFEditor.runner;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/** Converts key presses in a text area into an input stream for Brainfuck. */
public class TextAreaInputStream extends InputStream {

	private final Queue<Character> characters = new ConcurrentLinkedQueue<>();

	/**
	 * Creates an input stream linked to a text area.
	 *
	 * @param textArea text area supplying characters
	 */
	public TextAreaInputStream(JTextArea textArea) {
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				characters.add(e.getKeyChar());
			}
		});
	}

	/**
	 * Reads the next typed character, waiting until one is available.
	 *
	 * @return character value
	 */
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

	/**
	 * Reads up to {@code len} typed character, waiting until all are available.
	 *
	 * @param b   the buffer into which the data is read
	 * @param off the start offset in array b at wich the data is written
	 * @param len the maximum number of bytes to read
	 * @return the total number of characters read into the buffer, of -1 if there
	 *         is no more data
	 */
	@Override
	public int read(byte[] b, int off, int len) {
		int totalRead = 0;

		for (int i = off; i < Math.min(len - off, b.length); i++) {
			int value = read();
			if (value != -1) {
				b[i] = (byte) value;
				totalRead++;
			}
		}

		return totalRead;
	}

}
