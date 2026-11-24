package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Displays runner controls, the virtual console, and debug runtime state.
 */
public class RunnerPanel extends JPanel {

	private final JSplitPane splitPane;

	private final JTextArea virtualConsole;
	private final transient TextAreaInputStream input;
	private final transient TextAreaOutputStream output;

	private final RuntimeStatePanel runtimeStatePanel;

	/** Creates the runner controls and virtual-console panel. */
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
			BFEditor.getInstance().getEngine().nextInstruction();
			updateRuntimeState();
		});
		buttonsPanel.add(stepButton);

		add(buttonsPanel, BorderLayout.NORTH);

		virtualConsole = new JTextArea();
		virtualConsole.setBorder(new EmptyBorder(5, 5, 5, 5));
		virtualConsole.setFont(BFEditor.getInstance().getEditorFont());

		JScrollPane scrollPane = new JScrollPane(virtualConsole);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

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

	/** Refreshes runtime-state visibility and contents after an engine action. */
	public void updateRuntimeState() {
		try {
			// make sure engine is up to date
			Thread.sleep(10);
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
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

	/**
	 * Gets the stream receiving virtual-console input.
	 *
	 * @return virtual-console input stream
	 */
	public TextAreaInputStream getInput() {
		return input;
	}

	/**
	 * Gets the stream writing to the virtual console.
	 *
	 * @return virtual-console output stream
	 */
	public TextAreaOutputStream getOutput() {
		return output;
	}
}
