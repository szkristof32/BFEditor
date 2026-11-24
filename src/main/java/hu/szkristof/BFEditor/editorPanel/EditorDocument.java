package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.EditHistory;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/** Swing document that records editor insertions and removals in edit history. */
public class EditorDocument extends PlainDocument {

	private final EditHistory history;
	private final FileEditor editor;
	private boolean enabled = true;

	/**
	 * Creates a document linked to an edit-history and file-editor pair.
	 *
	 * @param history buffer edit history
	 * @param editor associated file editor
	 */
	public EditorDocument(EditHistory history, FileEditor editor) {
		this.history = history;
		this.editor = editor;
	}

	/**
	 * Inserts text and records the change when history tracking is enabled.
	 *
	 * @param offs insertion position
	 * @param str text to insert
	 * @param a text attributes
	 * @throws BadLocationException if {@code offs} is invalid
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null || str.isEmpty())
			return;
		if (!enabled) {
			updateDirtyIndicator();
			super.insertString(offs, str, a);
			return;
		}

		history.insert(offs, str);
		super.insertString(offs, str, a);
		updateDirtyIndicator();
	}

	/**
	 * Removes text and records the change when history tracking is enabled.
	 *
	 * @param offs removal start position
	 * @param len number of characters to remove
	 * @throws BadLocationException if the range is invalid
	 */
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		if (len <= 0)
			return;
		if (!enabled) {
			updateDirtyIndicator();
			super.remove(offs, len);
			return;
		}

		String removed = getText(offs, len);
		history.delete(offs, removed);
		super.remove(offs, len);
		updateDirtyIndicator();
	}

	/**
	 * Enables or disables edit-history recording.
	 *
	 * @param enabled {@code true} to record changes
	 */
	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

	private void updateDirtyIndicator() {
		editor.getTab().setDirty(editor.getLastSavedHistory() != history.getHistoryIndex());
	}

}
