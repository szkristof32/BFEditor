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

	public void openFile(File file) {
		FileEditor editor = new FileEditor(file);
		openedFiles.add(editor);
		addTab(file.getName(), editor);
		BFEditor.getInstance().revalidate();
	}

}
