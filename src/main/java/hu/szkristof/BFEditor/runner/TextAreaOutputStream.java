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
		if (b == '\n') {
			stringBuilder.append('\n');
			final String text = stringBuilder.toString();
			SwingUtilities.invokeLater(() -> {
				textArea.append(text);
			});
			stringBuilder.setLength(0);
			return;
		}

		stringBuilder.append((char) b);
	}

}
