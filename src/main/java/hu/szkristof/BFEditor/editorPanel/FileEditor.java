package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.EditHistory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class FileEditor extends JPanel {

	private EditorTab tab;

	private File file;
	private JTextArea textArea;
	private EditHistory editHistory;

	public FileEditor(File file) {
		super(new GridLayout(1, 1));
		this.tab = new EditorTab(this, file != null ? file.getName() : "Untitled");
		this.file = file;
		this.textArea = new JTextArea();
		this.editHistory = new EditHistory();

		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		textArea.setFont(BFEditor.getInstance().getEditorFont());
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					int caretPosition = textArea.getCaretPosition();
					try {
						editHistory.delete(caretPosition - 1, textArea.getText(caretPosition - 1, 1));
					} catch (BadLocationException ex) {
						ex.printStackTrace(System.err);
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\b') return;
				int caretPosition = textArea.getCaretPosition();
				editHistory.insert(caretPosition, String.valueOf(e.getKeyChar()));
			}
		});

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		try {
			reloadFromDisk();
		} catch (IOException e) {
			System.err.println("Error during reloading");
			e.printStackTrace();
		}

		add(scrollPane);
	}

	private void reloadFromDisk() throws IOException {
		if (file == null)
			return;

		try (InputStream stream = new FileInputStream(file)) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				String line;
				StringBuilder content = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					content.append(line).append('\n');
				}
				textArea.setText(content.toString());
				textArea.setCaretPosition(0);
				BFEditor.getInstance().revalidate();
			}
		}
	}

	public EditorTab getTab() {
		return tab;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getBuffer() {
		return textArea.getText();
	}

	public void setBuffer(String buffer) {
		textArea.setText(buffer);
	}

	public EditHistory getEditHistory() {
		return editHistory;
	}
}
