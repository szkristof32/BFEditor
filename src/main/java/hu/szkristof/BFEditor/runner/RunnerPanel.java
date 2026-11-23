package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RunnerPanel extends JPanel {

	private JSplitPane splitPane;

	private JTextArea virtualConsole;
	private TextAreaInputStream input;
	private TextAreaOutputStream output;

	private RuntimeStatePanel runtimeStatePanel;

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
			updateRuntimeState();
		});
		buttonsPanel.add(debugButton);

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(_ -> {
			BFEditor.getInstance().getEngine().stop();
			updateRuntimeState();
		});
		buttonsPanel.add(stopButton);

		JButton stepButton = new JButton("Next instruction");
		stepButton.addActionListener(_ -> {
			BFEditor.getInstance().getEngine().step();
			updateRuntimeState();
		});
		buttonsPanel.add(stepButton);

		add(buttonsPanel, BorderLayout.NORTH);

		virtualConsole = new JTextArea();
		virtualConsole.setBorder(new EmptyBorder(5, 5, 5, 5));
		virtualConsole.setFont(BFEditor.getInstance().getEditorFont());

		JScrollPane scrollPane = new JScrollPane(virtualConsole);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		runtimeStatePanel = new RuntimeStatePanel();
		runtimeStatePanel.setVisible(false);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.add(scrollPane);
		splitPane.add(runtimeStatePanel);

		add(splitPane, BorderLayout.CENTER);

		input = new TextAreaInputStream(virtualConsole);
		output = new TextAreaOutputStream(virtualConsole);
	}

	public void updateRuntimeState() {
		try {
			// make sure engine is up to date
			Thread.sleep(10);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		BFEngine engine = BFEditor.getInstance().getEngine();

		if (!engine.isRunning()) {
			runtimeStatePanel.setVisible(false);
			BFEditor.getInstance().revalidate();
			return;
		}

		runtimeStatePanel.setVisible(true);
		splitPane.setDividerLocation(640);
		runtimeStatePanel.updatePanel();
		BFEditor.getInstance().revalidate();
	}

	public TextAreaInputStream getInput() {
		return input;
	}

	public TextAreaOutputStream getOutput() {
		return output;
	}
}
