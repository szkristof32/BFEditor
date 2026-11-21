package hu.szkristof.BFEditor;

import hu.szkristof.BFEditor.editorPanel.EditorPanel;
import hu.szkristof.BFEditor.menu.MenuBar;
import hu.szkristof.BFEditor.runner.RunnerPanel;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BFEditor extends JFrame {

	private static BFEditor instance = null;

	private PreferencesDialog preferencesDialog;

	private EditorPanel editorPanel = null;
	private Font editorFont = null;

	private RunnerPanel runnerPanel = null;

	private BFEngine engine = null;
	private DialogThread dialogThread = null;

	public BFEditor() {
		super("BFEditor");
		instance = this;

		preferencesDialog = new PreferencesDialog();

		dialogThread = new DialogThread();
		dialogThread.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		editorFont = new Font("Monospaced", Font.PLAIN, 12);

		setJMenuBar(new MenuBar());

		editorPanel = new EditorPanel();
		editorPanel.newFile();

		runnerPanel = new RunnerPanel();

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, runnerPanel);
		splitPane.setDividerLocation(400);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);

		setVisible(true);

		PrintStream printStream = new PrintStream(runnerPanel.getOutput(), true);

		engine = new BFEngine(printStream, runnerPanel.getInput());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				printStream.close();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				dialogThread.stop();
				try {
					dialogThread.join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void openPreferences() {
		preferencesDialog.setVisible(true);
	}

	public Preferences getPreferences() {
		return preferencesDialog.getPreferences();
	}

	public BFEngine getEngine() {
		return engine;
	}

	public EditorPanel getEditorPanel() {
		return editorPanel;
	}

	public DialogThread getDialogThread() {
		return dialogThread;
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
		new BFEditor();
	}

}
