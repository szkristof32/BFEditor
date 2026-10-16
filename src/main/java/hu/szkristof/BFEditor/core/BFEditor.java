package hu.szkristof.BFEditor.core;

import javax.swing.*;
import java.awt.*;

public class BFEditor extends JFrame {

	private Font editorFont = null;

	public BFEditor() {
		super("BFEditor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		editorFont = new Font("Monospaced", Font.PLAIN, 12);

		JPanel sidePanel = sidePanel();
		JPanel textBoxPanel = textBoxPanel();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, textBoxPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		add(splitPane, BorderLayout.CENTER);
		setVisible(true);
	}

	private JPanel sidePanel() {
		JPanel sidePanel = new JPanel(new FlowLayout());
		sidePanel.add(new JButton("OK"));
		sidePanel.add(new JButton("Cancel"));
		return sidePanel;
	}

	private JPanel textBoxPanel() {
		JPanel textBoxPanel = new JPanel(new GridLayout(1, 1));

		JTextArea textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setFont(editorFont);

		textBoxPanel.add(textArea);

		return textBoxPanel;
	}

	static void main(String[] args) {
		new BFEditor();
	}

}
