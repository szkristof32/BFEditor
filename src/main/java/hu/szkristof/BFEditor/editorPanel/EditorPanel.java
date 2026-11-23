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
		for (int i = 0; i < openedFiles.size(); i++) {
			if (openedFiles.get(i).getFile() != null && openedFiles.get(i).getFile().equals(file)) {
				setSelectedIndex(i);
				return;
			}
		}

		FileEditor editor = new FileEditor(file);
		if (!openedFiles.isEmpty() && getCurrentEditor().getFile() == null) {
			openedFiles.clear();
			closeCurrentEditor();
		}
		openedFiles.add(editor);
		addTabAndSwitch(file.getName(), editor);
	}

	public boolean saveCurrentFile() {
		FileEditor editor = getCurrentEditor();
		if (editor.getFile() == null)
			return false;
		saveFile(editor.getFile());
		editor.updateLastSavedHistory();
		return true;
	}

	public void saveFile(File file) {
		String buffer = getCurrentBuffer();
		try (OutputStream out = new FileOutputStream(file)) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
				writer.write(buffer);
				replaceCurrentTab(file);
				getCurrentEditor().setFile(file);
			}
			replaceCurrentTab(file);
		} catch (IOException e) {
			System.err.println("Error during saving");
			e.printStackTrace();
		}
	}

	public boolean shouldAllClose() {
		for (FileEditor editor : openedFiles) {
			if (!editor.getTab().isDirty())
				continue;

			return shouldClose(editor);
		}
		return true;
	}

	public boolean shouldClose(FileEditor editor) {
		boolean isDirty = editor.getTab().isDirty();
		if (!isDirty) return true;

		int result = JOptionPane.showConfirmDialog(BFEditor.getInstance(), "There are unsaved changes. Are you sure you want to proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}

	public void closeCurrentEditor() {
		FileEditor editor = getCurrentEditor();
		closeEditor(editor);
	}

	public void closeEditor(FileEditor editor) {
		if (!shouldClose(editor)) return;
		openedFiles.remove(editor);
		removeTabAt(indexOfComponent(editor));
	}

	private void addTabAndSwitch(String title, FileEditor editor) {
		addTab(title, editor);
		setSelectedIndex(getTabCount() - 1);
		setTabComponentAt(getTabCount() - 1, editor.getTab());
		BFEditor.getInstance().revalidate();
	}

	private void replaceCurrentTab(File file) {
		int selectedIndex = getSelectedIndex();
		EditorTab tab = (EditorTab) getTabComponentAt(selectedIndex);
		tab.setTitle(file.getName());
		tab.setDirty(false);
	}

	public FileEditor getCurrentEditor() {
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1)
			return null;
		Component editor = getComponentAt(selectedIndex);
		if (editor instanceof FileEditor fileEditor) {
			return fileEditor;
		}
		return null;
	}

	public File getCurrentFile() {
		int selectedIndex = getSelectedIndex();
		Component editor = getComponentAt(selectedIndex);
		if (editor instanceof FileEditor fileEditor) {
			return fileEditor.getFile();
		}
		return null;
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
