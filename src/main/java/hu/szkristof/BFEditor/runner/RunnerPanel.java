package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.TextAreaOutputStream;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;

public class RunnerPanel extends JPanel {

	private JTextArea virtualConsole;
	private TextAreaOutputStream output;

	public RunnerPanel() {
		setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton executeButton = new JButton("Execute");
		executeButton.addActionListener(_ -> {
			BFEngine engine = BFEditor.getInstance().getEngine();
			engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
			try {
				engine.execute();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		buttonsPanel.add(executeButton);

		JButton debugButton = new JButton("Debug");
		buttonsPanel.add(debugButton);

		JButton stopButton = new JButton("Stop");
		buttonsPanel.add(stopButton);

		JButton stepButton = new JButton("Step");
		buttonsPanel.add(stepButton);

		add(buttonsPanel, BorderLayout.NORTH);

		virtualConsole = new JTextArea();
		virtualConsole.setBorder(new EmptyBorder(5, 5, 5, 5));
		virtualConsole.setFont(BFEditor.getInstance().getEditorFont());

		add(virtualConsole, BorderLayout.CENTER);

		output = new TextAreaOutputStream(virtualConsole);
	}

	public TextAreaOutputStream getOutput() {
		return output;
	}
}
