package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class RunnerPanel extends JPanel {

	private JTextArea virtualConsole;
	private TextAreaInputStream input;
	private TextAreaOutputStream output;

	public RunnerPanel() {
		setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton executeButton = new JButton("Execute");
		executeButton.addActionListener(_ -> {
			BFEngine engine = BFEditor.getInstance().getEngine();
			engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
			engine.execute();
		});
		buttonsPanel.add(executeButton);

		JButton debugButton = new JButton("Debug");
		debugButton.addActionListener(_ -> {
			BFEngine engine = BFEditor.getInstance().getEngine();
			engine.reset(BFEditor.getInstance().getEditorPanel().getCurrentBuffer());
			engine.debug();
		});
		buttonsPanel.add(debugButton);

		JButton stopButton = new JButton("Stop");
		buttonsPanel.add(stopButton);

		JButton stepButton = new JButton("Next instruction");
		stepButton.addActionListener(_ -> {
			BFEditor.getInstance().getEngine().step();
		});
		buttonsPanel.add(stepButton);

		add(buttonsPanel, BorderLayout.NORTH);

		virtualConsole = new JTextArea();
		virtualConsole.setBorder(new EmptyBorder(5, 5, 5, 5));
		virtualConsole.setFont(BFEditor.getInstance().getEditorFont());

		JScrollPane scrollPane = new JScrollPane(virtualConsole);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		add(scrollPane, BorderLayout.CENTER);

		input = new TextAreaInputStream(virtualConsole);
		output = new TextAreaOutputStream(virtualConsole);
	}

	public TextAreaInputStream getInput() {
		return input;
	}

	public TextAreaOutputStream getOutput() {
		return output;
	}
}
