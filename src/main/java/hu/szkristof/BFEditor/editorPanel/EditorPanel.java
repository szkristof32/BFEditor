package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JTabbedPane {

	private List<FileEditor> openedFiles = new ArrayList<>();

	public EditorPanel() {
	}

	public void newFile() {
		FileEditor editor = new FileEditor(null);
		openedFiles.add(editor);
		addTabAndSwitch("Untitled", editor);
	}

	public void openFile(File file) {
		FileEditor editor = new FileEditor(file);
		openedFiles.add(editor);
		addTabAndSwitch(file.getName(), editor);
	}

	private void addTabAndSwitch(String title, FileEditor editor) {
		addTab(title, editor);
		setSelectedIndex(getTabCount() - 1);
		BFEditor.getInstance().revalidate();
	}

}
