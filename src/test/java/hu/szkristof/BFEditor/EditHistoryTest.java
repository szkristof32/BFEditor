package hu.szkristof.BFEditor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EditHistoryTest {

    @Test
    void insertUpdatesCurrentStateAndHistoryIndex() {
        EditHistory history = new EditHistory();

        history.insert(0, "abc");

        assertEquals("abc", history.getCurrentState());
        assertEquals(0, history.getHistoryIndex());
    }

    @Test
    void deleteRemovesText() {
        EditHistory history = new EditHistory();
        history.insert(0, "hello");

        history.delete(1, "ell");

        assertEquals("ho", history.getCurrentState());
    }

    @Test
    void undoRevertsLatestChange() {
        EditHistory history = new EditHistory();
        history.insert(0, "a");
        history.insert(1, "b");

        history.undo();

        assertEquals("a", history.getCurrentState());
    }

    @Test
    void redoReappliesUndoneChange() {
        EditHistory history = new EditHistory();
        history.insert(0, "a");
        history.insert(1, "b");
        history.undo();

        history.redo();

        assertEquals("ab", history.getCurrentState());
    }

    @Test
    void undoStopsAtStartingIndex() {
        EditHistory history = new EditHistory();
        history.insert(0, "initial");
        history.setStartingIndex(history.getHistoryIndex());

        history.undo();

        assertEquals("initial", history.getCurrentState());
    }

    @Test
    void newChangeDiscardsRedoBranch() {
        EditHistory history = new EditHistory();
        history.insert(0, "a");
        history.insert(1, "b");
        history.undo();

        history.insert(1, "c");
        history.redo();

        assertEquals("ac", history.getCurrentState());
    }
}
