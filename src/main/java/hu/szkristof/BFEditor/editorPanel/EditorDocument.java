package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.EditHistory;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EditorDocument extends PlainDocument {

	private final EditHistory history;
	private final FileEditor editor;
	private boolean enabled = true;

	public EditorDocument(EditHistory history, FileEditor editor) {
		this.history = history;
		this.editor = editor;
	}

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

	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

	private void updateDirtyIndicator() {
		editor.getTab().setDirty(editor.getLastSavedHistory() != history.getHistoryIndex());
	}

}
