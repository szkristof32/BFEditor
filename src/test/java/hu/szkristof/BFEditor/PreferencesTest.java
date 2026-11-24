package hu.szkristof.BFEditor;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreferencesTest {

    @Test
    void memoryCellCountDefaultsTo1024() {
        assertEquals(1024, new Preferences().getMemoryCellCount());
    }

    @Test
    void memoryCellCountCanBeChanged() {
        Preferences preferences = new Preferences();
        preferences.setMemoryCellCount(2048);
        assertEquals(2048, preferences.getMemoryCellCount());
    }

    @Test
    void overflowBehaviourDefaultsToWrapAround() {
        assertEquals(
                Preferences.MemoryCellOverflowBehaviour.WRAP_AROUND,
                new Preferences().getMemoryCellOverflowBehaviour()
        );
    }

    @Test
    void overflowBehaviourCanBeChanged() {
        Preferences preferences = new Preferences();
        preferences.setMemoryCellOverflowBehaviour(Preferences.MemoryCellOverflowBehaviour.CAP);
        assertEquals(Preferences.MemoryCellOverflowBehaviour.CAP, preferences.getMemoryCellOverflowBehaviour());
    }

    @Test
    void defaultMemoryCellValueCanBeChanged() {
        Preferences preferences = new Preferences();
        preferences.setDefaultMemoryCellValue(255);
        assertEquals(255, preferences.getDefaultMemoryCellValue());
    }

    @Test
    void deserializeReturnsDefaultsWhenPreferencesFileIsMissing() {
        File preferencesFile = new File("preferences.dat");
        File backup = new File("preferences.dat.unit-test-backup");
        if (preferencesFile.renameTo(backup)) {
            try {
                assertEquals(1024, Preferences.deserialize().getMemoryCellCount());
            } finally {
                backup.renameTo(preferencesFile);
            }
        } else {
            assertTrue(!preferencesFile.exists() || preferencesFile.delete(),
                    "Could not isolate preferences.dat");
            try {
                assertEquals(1024, Preferences.deserialize().getMemoryCellCount());
            } finally {
                if (preferencesFile.exists()) {
                    assertTrue(preferencesFile.delete());
                }
            }
        }
    }
}
