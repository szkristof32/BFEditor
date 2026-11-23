package hu.szkristof.BFEditor.runner;

import javax.swing.*;
import java.io.OutputStream;

public class TextAreaOutputStream extends OutputStream {

	private final JTextArea textArea;
	private final StringBuilder stringBuilder = new StringBuilder();

	public TextAreaOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void write(int b) {
		if (b == '\r')
			return;

		final String toAppend = String.valueOf((char) b);
		SwingUtilities.invokeLater(() -> textArea.append(toAppend));
	}

}
