package hu.szkristof.BFEditor.runner;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextAreaStreamsTest {

    @Test
    void outputStreamWritesCharactersToTextArea() throws Exception {
        JTextArea textArea = new JTextArea();
        TextAreaOutputStream output = new TextAreaOutputStream(textArea);

        SwingUtilities.invokeAndWait(() -> {
            output.write('O');
            output.write('K');
            output.write('\n');
        });
        SwingUtilities.invokeAndWait(() -> {});

        output.close();

        assertEquals("OK\n", textArea.getText());
    }

    @Test
    void outputStreamIgnoresCarriageReturns() throws Exception {
        JTextArea textArea = new JTextArea();
        TextAreaOutputStream output = new TextAreaOutputStream(textArea);

        SwingUtilities.invokeAndWait(() -> {
            output.write('A');
            output.write('\r');
            output.write('\n');
        });
        SwingUtilities.invokeAndWait(() -> {});
        
        output.close();

        assertEquals("A\n", textArea.getText());
    }
}
