package hu.szkristof.BFEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks buffer insertions and deletions for undo and redo operations.
 */
public class EditHistory {

	private String currentState = "";
	private final List<Diff> diffs = new ArrayList<>();
	private int startingIndex = 0;
	private int historyIndex = -1;

	/** Undo the latest change unless the history has reached its starting state. */
	public void undo() {
		if (historyIndex <= startingIndex)
			return;
		revertDiff(diffs.get(historyIndex));
		historyIndex--;
	}

	/** Redo the next previously undone change, if one exists. */
	public void redo() {
		if (historyIndex == diffs.size() - 1)
			return;
		executeDiff(diffs.get(historyIndex + 1));
		historyIndex++;
	}

	/**
	 * Records and applies a text deletion.
	 *
	 * @param start deletion start position
	 * @param remove text removed from the buffer
	 */
	public void delete(int start, String remove) {
		Diff diff = new Diff(start, remove, -1, null);
		if (historyIndex != diffs.size() - 1) {
			if (diffs.size() > historyIndex + 1) {
				diffs.subList(historyIndex + 1, diffs.size()).clear();
			}
		}
		diffs.add(diff);
		executeDiff(diff);
		historyIndex++;
	}

	/**
	 * Records and applies a text insertion.
	 *
	 * @param start insertion start position
	 * @param insert text inserted into the buffer
	 */
	public void insert(int start, String insert) {
		Diff diff = new Diff(-1, null, start, insert);
		if (historyIndex != diffs.size() - 1) {
			if (diffs.size() > historyIndex + 1) {
				diffs.subList(historyIndex + 1, diffs.size()).clear();
			}
		}
		diffs.add(diff);
		executeDiff(diff);
		historyIndex++;
	}

	private void executeDiff(Diff diff) {
		String temp = currentState;
		if (diff.remove() != null)
			temp = temp.substring(0, diff.removeStart()) + temp.substring(diff.removeStart() + diff.remove().length());
		if (diff.add() != null)
			temp = temp.substring(0, diff.addStart()) + diff.add() + temp.substring(diff.addStart());
		currentState = temp;
	}

	private void revertDiff(Diff diff) {
		String temp = currentState;
		if (diff.add() != null)
			temp = temp.substring(0, diff.addStart()) + temp.substring(diff.addStart() + diff.add().length());
		if (diff.remove() != null)
			temp = temp.substring(0, diff.removeStart()) + diff.remove() + temp.substring(diff.removeStart());
		currentState = temp;
	}

	/**
	 * Sets the history index corresponding to the last saved state.
	 *
	 * @param startingIndex saved-state history index
	 */
	public void setStartingIndex(int startingIndex) {
		this.startingIndex = startingIndex;
	}

	/**
	 * Gets the current buffer text.
	 *
	 * @return text produced by the edit history
	 */
	public String getCurrentState() {
		return currentState;
	}

	/**
	 * Gets the current position in the edit history.
	 *
	 * @return current history index
	 */
	public int getHistoryIndex() {
		return historyIndex;
	}

	private record Diff(int removeStart, String remove, int addStart, String add) {
	}

}
