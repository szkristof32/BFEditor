package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileMenu extends JMenu {

	public FileMenu() {
		super("File");

		JMenuItem newMenuItem = new JMenuItem(new AbstractAction("New") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().getEditorPanel().newFile();
			}
		});
		add(newMenuItem);

		JMenuItem openMenuItem = new JMenuItem(new AbstractAction("Open") {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					PointerBuffer buffer = stack.callocPointer(512);
					int status = NFD_OpenDialog(buffer, null, (CharSequence) null);
					if (status == NFD_OKAY) {
						BFEditor.getInstance().getEditorPanel().openFile(new File(buffer.getStringASCII()));
					} else if (status == NFD_CANCEL) {
						System.out.println("User cancelled");
					} else {
						System.err.println("Error occurred: " + NFD_GetError());
					}
				}
			}
		});
		add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean success = BFEditor.getInstance().getEditorPanel().saveCurrentFile();
				if (success) return;
				
				try (MemoryStack stack = MemoryStack.stackPush()) {
					PointerBuffer buffer = stack.callocPointer(512);
					int status = NFD_SaveDialog(buffer, null, (CharSequence) null, null);
					if (status == NFD_OKAY) {
						BFEditor.getInstance().getEditorPanel().saveFile(new File(buffer.getStringASCII()));
					} else if (status == NFD_CANCEL) {
						System.out.println("User cancelled");
					} else {
						System.err.println("Error occurred: " + NFD_GetError());
					}
				}
			}
		});
		add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem(new AbstractAction("Save As") {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					PointerBuffer buffer = stack.callocPointer(512);
					int status = NFD_SaveDialog(buffer, null, (CharSequence) null, null);
					if (status == NFD_OKAY) {
						BFEditor.getInstance().getEditorPanel().saveFile(new File(buffer.getStringASCII()));
					} else if (status == NFD_CANCEL) {
						System.out.println("User cancelled");
					} else {
						System.err.println("Error occurred: " + NFD_GetError());
					}
				}
			}
		});
		add(saveAsMenuItem);

		addSeparator();

		JMenuItem exitMenuItem = new JMenuItem(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				BFEditor.getInstance().requestClose();
			}
		});
		add(exitMenuItem);
	}

}
