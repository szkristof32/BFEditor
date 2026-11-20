package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.EditHistory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class FileEditor extends JPanel {

	private EditorTab tab;

	private File file;
	private JTextArea textArea;
	private EditHistory editHistory;

	private int lastSavedHistory = -1;

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

	public int getLastSavedHistory() {
		return lastSavedHistory;
	}

	public void updateLastSavedHistory() {
		lastSavedHistory = editHistory.getHistoryIndex();
		tab.setDirty(false);
	}

	public void copy() {
		textArea.copy();
	}

	public void paste() {
		textArea.paste();
	}

	public void cut() {
		textArea.cut();
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
		EditorDocument document = (EditorDocument) textArea.getDocument();
		document.enable(false);
		textArea.setText(buffer);
		document.enable(true);
	}

	public EditHistory getEditHistory() {
		return editHistory;
	}
}
