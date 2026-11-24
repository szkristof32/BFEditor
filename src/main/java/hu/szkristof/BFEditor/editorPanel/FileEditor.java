package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.EditHistory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

/** Represents one open file buffer and its editable text area. */
public class FileEditor extends JPanel {

	private EditorTab tab;

	private File file;
	private JTextArea textArea;
	private EditHistory editHistory;

	private int lastSavedHistory = -1;

	/**
	 * Creates an editor for a file or an untitled buffer.
	 *
	 * @param file file to load, or {@code null} for a new untitled buffer
	 */
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
		textArea.setDocument(new EditorDocument(editHistory, this));

		InputMap inputMap = textArea.getInputMap();
		ActionMap actionMap = textArea.getActionMap();

		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control Z"), "undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editHistory.undo();
				setBuffer(editHistory.getCurrentState());
			}
		});
		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control Y"), "redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editHistory.redo();
				setBuffer(editHistory.getCurrentState());
			}
		});
		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control C"), "my_copy", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control V"), "my_paste", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
				setBuffer(editHistory.getCurrentState());
			}
		});
		addShortcut(inputMap, actionMap, KeyStroke.getKeyStroke("control X"), "my_cut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cut();
				setBuffer(editHistory.getCurrentState());
			}
		});

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(Color.white);

		try {
			reloadFromDisk();
			editHistory.setStartingIndex(editHistory.getHistoryIndex());
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
				updateLastSavedHistory();
				BFEditor.getInstance().revalidate();
			}
		}
	}

	private void addShortcut(InputMap inputMap, ActionMap actionMap, KeyStroke keyStroke, Object key, AbstractAction action) {
		inputMap.put(keyStroke, key);
		actionMap.put(key, action);
	}

	/**
	 * Gets the edit-history index associated with the last save.
	 *
	 * @return last-saved history index
	 */
	public int getLastSavedHistory() {
		return lastSavedHistory;
	}

	/** Marks the current state as saved. */
	public void updateLastSavedHistory() {
		lastSavedHistory = editHistory.getHistoryIndex();
		tab.setDirty(false);
	}

	/** Copies the selected text to the system clipboard. */
	public void copy() {
		textArea.copy();
	}

	/** Pastes clipboard text into the buffer. */
	public void paste() {
		textArea.paste();
	}

	/** Cuts selected text into the system clipboard. */
	public void cut() {
		textArea.cut();
	}

	/**
	 * Gets this buffer's tab component.
	 *
	 * @return editor tab
	 */
	public EditorTab getTab() {
		return tab;
	}

	/**
	 * Gets the file associated with this buffer.
	 *
	 * @return associated file, or {@code null} for an untitled buffer
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file associated with this buffer.
	 *
	 * @param file associated file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Gets the editable text area.
	 *
	 * @return text area
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Gets the complete buffer text.
	 *
	 * @return buffer text
	 */
	public String getBuffer() {
		return textArea.getText();
	}

	/**
	 * Replaces the buffer text without recording an edit-history change.
	 *
	 * @param buffer replacement text
	 */
	public void setBuffer(String buffer) {
		EditorDocument document = (EditorDocument) textArea.getDocument();
		int caretPosition = textArea.getCaretPosition();
		document.enable(false);
		textArea.setText(buffer);
		textArea.setCaretPosition(Math.min(caretPosition, buffer.length()));
		document.enable(true);
	}

	/**
	 * Gets this buffer's edit history.
	 *
	 * @return edit history
	 */
	public EditHistory getEditHistory() {
		return editHistory;
	}
}
