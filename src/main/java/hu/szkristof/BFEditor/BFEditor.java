package hu.szkristof.BFEditor;

import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class BFEditor extends JFrame {

	private JTextArea textArea;
	private Font editorFont = null;

	private BFEngine engine = null;

	public BFEditor() {
		super("BFEditor");
		this.engine = new BFEngine();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		editorFont = new Font("Monospaced", Font.PLAIN, 12);

		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		JPanel sidePanel = createSidePanel();
		JPanel textBoxPanel = createTextBoxPanel();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, textBoxPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		add(splitPane, BorderLayout.CENTER);
		setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JFrame frame = this;

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);

		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As");
		fileMenu.add(saveAsMenuItem);
		fileMenu.addSeparator();

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setAction(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		return menuBar;
	}

	private JPanel createSidePanel() {
		JPanel sidePanel = new JPanel(new FlowLayout());

		JButton runButton = new JButton("Run");
		runButton.setAction(new AbstractAction("Run") {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.reset(textArea.getText());
				try {
					engine.run();
				} catch (IOException ex) {
					System.err.println("Error during execution");
				}
			}
		});
		sidePanel.add(runButton);

		return sidePanel;
	}

	private JPanel createTextBoxPanel() {
		JPanel textBoxPanel = new JPanel(new GridLayout(1, 1));

		textArea = new JTextArea();
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
