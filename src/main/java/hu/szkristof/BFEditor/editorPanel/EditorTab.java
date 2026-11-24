package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/** Displays an editor tab title, dirty marker, and close button. */
public class EditorTab extends JPanel {

	private FileEditor editor;

	private String title;
	private boolean dirty;

	private JLabel label;

	/**
	 * Creates a tab component for an editor.
	 *
	 * @param editor associated file editor
	 * @param title tab title
	 */
	public EditorTab(FileEditor editor, String title) {
		this.editor = editor;
		this.title = title;
		this.dirty = false;
		this.label = new JLabel(title);

		setLayout(new BorderLayout(15, 0));
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));

		add(label, BorderLayout.CENTER);

		JButton closeButton = new JButton(new AbstractAction("X") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().getEditorPanel().closeEditor(editor);
			}
		});
		closeButton.setMargin(new Insets(0, 5, 0, 5));
		add(closeButton, BorderLayout.EAST);
	}

	/**
	 * Gets the tab title.
	 *
	 * @return tab title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the tab title.
	 *
	 * @param title new title
	 */
	public void setTitle(String title) {
		this.title = title;
		this.label.setText(title);
		BFEditor.getInstance().revalidate();
	}

	/**
	 * Reports whether the buffer has unsaved changes.
	 *
	 * @return {@code true} if the buffer is dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets and displays the unsaved-change marker.
	 *
	 * @param dirty {@code true} to mark the buffer dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		this.label.setText((dirty ? "*" : "") + title);
		BFEditor.getInstance().revalidate();
	}

}
