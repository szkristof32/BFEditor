package hu.szkristof.BFEditor;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.io.File;

import static org.lwjgl.util.nfd.NativeFileDialog.*;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_CANCEL;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_GetError;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_OKAY;

public class DialogThread extends Thread {

	public enum DialogAction {

		None,
		Open,
		Save

	}

	public enum DialogStatus {

		None,
		Idle,
		Working

	}

	private boolean running = true;
	private DialogAction action = DialogAction.None;
	private DialogStatus status = DialogStatus.None;
	private File lastFile = null;

	public DialogThread() {
		setDaemon(true);
	}

	public synchronized void stop() {
		running = false;
		notifyAll();
	}

	@Override
	public synchronized void run() {
		NFD_Init();

		status = DialogStatus.Idle;

		while (running) {
			while (action == DialogAction.None) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			switch (action) {
				case Open:
					open();
					break;
				case Save:
					save();
					break;
				default:
					break;
			}
		}

		NFD_Quit();
	}

	public synchronized void openFile() {
		action = DialogAction.Open;
		status = DialogStatus.Working;
		notifyAll();
	}

	private void open() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer buffer = stack.callocPointer(512);
			int status = NFD_OpenDialog(buffer, null, (CharSequence) null);
			if (status == NFD_OKAY) {
				lastFile = new File(buffer.getStringASCII());
			} else if (status == NFD_CANCEL) {
				System.out.println("User cancelled");
			} else {
				System.err.println("Error occurred: " + NFD_GetError());
			}
			action = DialogAction.None;
		}
		status = DialogStatus.Idle;
		notifyAll();
	}

	public synchronized void saveFile() {
		action = DialogAction.Save;
		status = DialogStatus.Working;
		notifyAll();
	}

	private void save() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer buffer = stack.callocPointer(512);
			int status = NFD_SaveDialog(buffer, null, (CharSequence) null, null);
			if (status == NFD_OKAY) {
				lastFile = new File(buffer.getStringASCII());
			} else if (status == NFD_CANCEL) {
				System.out.println("User cancelled");
			} else {
				System.err.println("Error occurred: " + NFD_GetError());
			}
			action = DialogAction.None;
		}
		status = DialogStatus.Idle;
		notifyAll();
	}

	public synchronized File getLastFile() throws InterruptedException {
		while (status == DialogStatus.Working)
			wait();

		return lastFile;
	}
}
