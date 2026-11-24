package hu.szkristof.BFEditor;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.util.nfd.NativeFileDialog.*;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_CANCEL;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_GetError;
import static org.lwjgl.util.nfd.NativeFileDialog.NFD_OKAY;

/** Handles native open-file and save-file dialogs on a daemon thread. */
public class DialogThread extends Thread {

	/** Identifies a pending native file-dialog action. */
	public enum DialogAction {

		None,
		Open,
		Save,
		Invalid

	}

	/** Identifies the dialog worker's current status. */
	public enum DialogStatus {

		None,
		Idle,
		Working

	}

	private final AtomicBoolean running = new AtomicBoolean(true);
	private DialogAction action = DialogAction.None;
	private DialogStatus status = DialogStatus.None;
	private File lastFile = null;

	/** Creates the native dialog worker thread. */
	public DialogThread() {
		setDaemon(true);
	}

	/** Requests that the worker stop and exits its wait loop. */
	public synchronized void stop() {
		running.set(false);
		action = DialogAction.Invalid;
		notifyAll();
	}

	/** Runs native-dialog initialization and processes pending actions. */
	@Override
	public synchronized void run() {
		NFD_Init();

		status = DialogStatus.Idle;

		while (running.get()) {
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

	/** Requests an open-file dialog. */
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

	/** Requests a save-file dialog. */
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

	/**
	 * Waits for the active dialog and gets its selected file.
	 *
	 * @return selected file, or {@code null} if the dialog was cancelled
	 * @throws InterruptedException if waiting is interrupted
	 */
	public synchronized File getLastFile() throws InterruptedException {
		while (status == DialogStatus.Working)
			wait();

		File lastFile = this.lastFile;
		this.lastFile = null;
		return lastFile;
	}
}
