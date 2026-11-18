package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RunnerPanel extends JPanel {

	private JTextArea virtualConsole;

	public RunnerPanel() {
		setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton executeButton = new JButton("Execute");
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
		virtualConsole.setText("Hello World");

		add(virtualConsole, BorderLayout.CENTER);
	}

}
