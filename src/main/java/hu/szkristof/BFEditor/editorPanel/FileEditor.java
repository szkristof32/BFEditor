package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileEditor extends JPanel {

	private File file;

	public FileEditor(File file) {
		super(new GridLayout(1, 1));
		this.file = file;

		JTextArea textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setFont(BFEditor.getInstance().getEditorFont());

		add(textArea);
	}

}
