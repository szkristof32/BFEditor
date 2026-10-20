package hu.szkristof.BFEditor;

import hu.szkristof.BFEditor.editorPanel.EditorPanel;
import hu.szkristof.BFEditor.menu.MenuBar;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static org.lwjgl.util.nfd.NativeFileDialog.NFD_Init;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_Quit;

public class BFEditor extends JFrame {

	private static BFEditor instance = null;

	private EditorPanel editorPanel = null;
	private Font editorFont = null;

	private BFEngine engine = null;

	public BFEditor() {
		super("BFEditor");
		instance = this;
		this.engine = new BFEngine();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		editorFont = new Font("Monospaced", Font.PLAIN, 12);

		setJMenuBar(new MenuBar());

		JPanel sidePanel = createSidePanel();
		editorPanel = new EditorPanel();
		editorPanel.newFile();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, editorPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		add(splitPane, BorderLayout.CENTER);
		setVisible(true);
	}

	private JPanel createSidePanel() {
		JPanel sidePanel = new JPanel(new FlowLayout());

		JButton runButton = new JButton(new AbstractAction("Run") {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.reset(editorPanel.getCurrentBuffer());
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

	public EditorPanel getEditorPanel() {
		return editorPanel;
	}

	public Font getEditorFont() {
		return editorFont;
	}

	public void requestClose() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public static BFEditor getInstance() {
		return instance;
	}

	static void main(String[] args) {
		NFD_Init();
		new BFEditor();
		NFD_Quit();
	}

}
