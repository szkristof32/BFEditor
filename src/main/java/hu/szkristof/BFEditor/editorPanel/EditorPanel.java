package hu.szkristof.BFEditor.editorPanel;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
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

	public void saveFile(File file) {
		String buffer = getCurrentBuffer();
		try (OutputStream out = new FileOutputStream(file)) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
				writer.write(buffer);
				replaceCurrentTab(file);
			}
		} catch (IOException e) {
			System.err.println("Error during saving");
			e.printStackTrace();
		}
	}

	private void addTabAndSwitch(String title, FileEditor editor) {
		addTab(title, editor);
		setSelectedIndex(getTabCount() - 1);
		BFEditor.getInstance().revalidate();
	}

	private void replaceCurrentTab(File file) {
		int selectedIndex = getSelectedIndex();
		setTitleAt(selectedIndex, file.getName());
	}

	public String getCurrentBuffer() {
		int selectedIndex = getSelectedIndex();
		Component editor = getComponentAt(selectedIndex);
		if (editor instanceof FileEditor fileEditor) {
			return fileEditor.getBuffer();
		}
		return "";
	}

}
