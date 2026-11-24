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

/**
 * Main singleton application window integrating the editor, runner, menus,
 * preferences dialog, Brainfuck engine, and native file-dialog thread.
 */
public class BFEditor extends JFrame {

	private static BFEditor instance = null;

	private PreferencesDialog preferencesDialog;

	private EditorPanel editorPanel = null;
	private Font editorFont = null;

	private RunnerPanel runnerPanel = null;

	private BFEngine engine = null;
	private DialogThread dialogThread = null;

	/** Creates and displays the main application window. */
	public BFEditor() {
		super("BFEditor");
		instance = this;

		preferencesDialog = new PreferencesDialog();

		dialogThread = new DialogThread();
		dialogThread.start();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
				if (!editorPanel.shouldAllClose())
					return;
				printStream.close();
				BFEditor.this.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				dialogThread.stop();
				try {
					dialogThread.join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				System.exit(0);
			}
		});
	}

	/** Opens the preferences dialog. */
	public void openPreferences() {
		preferencesDialog.setVisible(true);
	}

	/**
	 * Gets the application's active preferences.
	 *
	 * @return active preferences
	 */
	public Preferences getPreferences() {
		return preferencesDialog.getPreferences();
	}

	/**
	 * Gets the Brainfuck interpreter.
	 *
	 * @return the application engine
	 */
	public BFEngine getEngine() {
		return engine;
	}

	/**
	 * Gets the tabbed editor panel.
	 *
	 * @return editor panel
	 */
	public EditorPanel getEditorPanel() {
		return editorPanel;
	}

	/**
	 * Gets the native file-dialog worker thread.
	 *
	 * @return dialog thread
	 */
	public DialogThread getDialogThread() {
		return dialogThread;
	}

	/**
	 * Gets the font used in the editor and virtual console.
	 *
	 * @return editor font
	 */
	public Font getEditorFont() {
		return editorFont;
	}

	/** Requests that the application main window close. */
	public void requestClose() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Gets the singleton application instance.
	 *
	 * @return the application instance, or {@code null} before construction
	 */
	public static BFEditor getInstance() {
		return instance;
	}

	static void main(String[] args) {
		new BFEditor();
	}

}
