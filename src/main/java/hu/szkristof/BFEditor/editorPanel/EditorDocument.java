package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.EditHistory;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EditorDocument extends PlainDocument {

	private final EditHistory history;
	private boolean enabled = true;

	public EditorDocument(EditHistory history) {
		this.history = history;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null || str.isEmpty())
			return;
		if (!enabled) {
			super.insertString(offs, str, a);
			return;
		}

		history.insert(offs, str);
		super.insertString(offs, str, a);
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		if (len <= 0)
			return;
		if (!enabled) {
			super.remove(offs, len);
			return;
		}

		String removed = getText(offs, len);
		history.delete(offs, removed);
		super.remove(offs, len);
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

}
