package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.DialogThread;
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
				DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
				dialogThread.openFile();
				try {
					File file = dialogThread.getLastFile();
					if (file != null)
						BFEditor.getInstance().getEditorPanel().openFile(file);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean success = BFEditor.getInstance().getEditorPanel().saveCurrentFile();
				if (success) return;

				DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
				dialogThread.saveFile();
				try {
					File file = dialogThread.getLastFile();
					if (file != null)
						BFEditor.getInstance().getEditorPanel().saveFile(file);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem(new AbstractAction("Save As") {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogThread dialogThread = BFEditor.getInstance().getDialogThread();
				dialogThread.saveFile();
				try {
					File file = dialogThread.getLastFile();
					if (file != null)
						BFEditor.getInstance().getEditorPanel().saveFile(file);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
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
