package hu.szkristof.BFEngine;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.Preferences;
import hu.szkristof.BFEditor.PreferencesDialog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BFEngineTest {

    private static Preferences preferences;

    @BeforeAll
    static void createPreferencesSingleton() throws Exception {
        preferences = new Preferences();

        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        BFEditor editor = (BFEditor) unsafe.allocateInstance(BFEditor.class);
        PreferencesDialog dialog = (PreferencesDialog) unsafe.allocateInstance(PreferencesDialog.class);

        Field preferencesField = PreferencesDialog.class.getDeclaredField("preferences");
        preferencesField.setAccessible(true);
        preferencesField.set(dialog, preferences);

        Field preferencesDialogField = BFEditor.class.getDeclaredField("preferencesDialog");
        preferencesDialogField.setAccessible(true);
        preferencesDialogField.set(editor, dialog);

        Field instanceField = BFEditor.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, editor);
    }

    private TestResult execute(String source, String input) throws Exception {
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(outputBytes, true, StandardCharsets.UTF_8);
        BFEngine engine = new BFEngine(
                output,
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
        );
        engine.reset(source);

        Method executeImpl = BFEngine.class.getDeclaredMethod("executeImpl");
        executeImpl.setAccessible(true);
        executeImpl.invoke(engine);

        return new TestResult(engine, outputBytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    void getOutputReturnsConfiguredOutput() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes);
        BFEngine engine = new BFEngine(output, new ByteArrayInputStream(new byte[0]));

        assertSame(output, engine.getOutput());
    }

    @Test
    void incrementsMemoryAndOutputsCharacter() throws Exception {
        TestResult result = execute("++++++++[>++++++++<-]>+.", "");

        assertEquals("A" + System.lineSeparator(), result.output());
    }

    @Test
    void decrementsMemoryAndOutputsCharacter() throws Exception {
        TestResult result = execute("++++++-.", "");

        assertEquals("\u0005" + System.lineSeparator(), result.output());
    }

    @Test
    void movesPointerAndWritesAtNewCell() throws Exception {
        TestResult result = execute(">+++.", "");

        assertEquals("\u0003" + System.lineSeparator(), result.output());
    }

    @Test
    void readsOneCharacterFromInput() throws Exception {
        TestResult result = execute(",.", "Z");

        assertEquals("Z" + System.lineSeparator(), result.output());
    }

    @Test
    void skipsLoopWhenCellIsZero() throws Exception {
        TestResult result = execute("[>++++++<-]>+++++. ", "");

        assertEquals("\u0005" + System.lineSeparator(), result.output());
    }

    @Test
    void executesLoopUntilCellIsZero() throws Exception {
        TestResult result = execute("++[.-]", "");

        assertEquals("\u0002\u0001" + System.lineSeparator(), result.output());
    }

    @Test
    void supportsNestedLoops() throws Exception {
        TestResult result = execute("++[>++[>+<-]++<-]>.", "");

        assertEquals("\u0002" + System.lineSeparator(), result.output());
    }

    @Test
    void resetRestoresCountersAndSource() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BFEngine engine = new BFEngine(
                new PrintStream(bytes, true, StandardCharsets.UTF_8),
                new ByteArrayInputStream(new byte[0])
        );
        engine.reset("++.");
        invokeExecuteImpl(engine);
        engine.reset("+++.");
        invokeExecuteImpl(engine);

        assertEquals(
                "\u0002" + System.lineSeparator() + "\u0003" + System.lineSeparator(),
                bytes.toString(StandardCharsets.UTF_8)
        );
    }

    @Test
    void capOverflowKeepsMaximumValue() throws Exception {
        preferences.setMemoryCellOverflowBehaviour(Preferences.MemoryCellOverflowBehaviour.Cap);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            BFEngine engine = new BFEngine(
                    new PrintStream(bytes, true, StandardCharsets.UTF_8),
                    new ByteArrayInputStream(new byte[0])
            );
            String increments = "+".repeat(300);
            engine.reset(increments + ".");
            invokeExecuteImpl(engine);

            assertEquals("\u00ff" + System.lineSeparator(), bytes.toString(StandardCharsets.UTF_8));
        } finally {
            preferences.setMemoryCellOverflowBehaviour(Preferences.MemoryCellOverflowBehaviour.WrapAround);
        }
    }

    private void invokeExecuteImpl(BFEngine engine) throws Exception {
        Method executeImpl = BFEngine.class.getDeclaredMethod("executeImpl");
        executeImpl.setAccessible(true);
        executeImpl.invoke(engine);
    }

    private record TestResult(BFEngine engine, String output) {
    }
}
