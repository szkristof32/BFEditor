package hu.szkristof.BFEditor.core;

import javax.swing.*;
import java.awt.*;

public class BFEditor extends JFrame {

	public BFEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(new JButton("OK"));
		buttonPanel.add(new JButton("Cancel"));
		JPanel textBoxPanel = new JPanel(new FlowLayout());
		textBoxPanel.add(new JLabel("Name:"));
		textBoxPanel.add(new JTextField(20));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel, textBoxPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		add(splitPane, BorderLayout.CENTER);

		setVisible(true);
	}

	static void main(String[] args) {
		new BFEditor();
	}

}
