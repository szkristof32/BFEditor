package hu.szkristof.BFEditor.runner;

import javax.swing.*;
import java.io.OutputStream;

/** Writes output bytes to a Swing text-area virtual console. */
public class TextAreaOutputStream extends OutputStream {

	private final JTextArea textArea;

	/**
	 * Creates an output stream connected to a text area.
	 *
	 * @param textArea text area receiving output
	 */
	public TextAreaOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * Appends a byte as a character to the virtual console.
	 * Carriage-return bytes are ignored.
	 *
	 * @param b byte or character value to append
	 */
	@Override
	public void write(int b) {
		if (b == '\r')
			return;

		final String toAppend = String.valueOf((char) b);
		SwingUtilities.invokeLater(() -> textArea.append(toAppend));
	}

	/**
	 * Appends up to {@code len} bytes from the specified byte array starting at
	 * offset {@code off} to the virtual console.
	 * Carriage-return bytes are ignored.
	 *
	 * @param b   the data
	 * @param off the start offset in the data
	 * @param len the maximum number of bytes to write
	 */
	@Override
	public void write(byte[] b, int off, int len) {
		for (int i = off; i < Math.min(b.length, len + off); i++) {
			write(b[i]);
		}
	}

}
