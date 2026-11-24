package hu.szkristof.BFEditor;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

/** Handles native open-file and save-file dialogs on a daemon thread. */
public class DialogThread extends Thread {

	/** Identifies a pending native file-dialog action. */
	public enum DialogAction {

		NONE,
		OPEN,
		SAVE,
		INVALID

	}

	/** Identifies the dialog worker's current status. */
	public enum DialogStatus {

		NONE,
		IDLE,
		WORKING

	}

	private final AtomicBoolean running = new AtomicBoolean(true);
	private DialogAction action = DialogAction.NONE;
	private DialogStatus status = DialogStatus.NONE;
	private File lastFile = null;

	/** Creates the native dialog worker thread. */
	public DialogThread() {
		setDaemon(true);
	}

	/** Requests that the worker stop and exits its wait loop. */
	public synchronized void stop() {
		running.set(false);
		action = DialogAction.INVALID;
		notifyAll();
	}

	/** Runs native-dialog initialization and processes pending actions. */
	@Override
	public synchronized void run() {
		NFD_Init();

		status = DialogStatus.IDLE;

		while (running.get()) {
			while (action == DialogAction.NONE) {
				try {
					wait();
				} catch (InterruptedException _) {
					Thread.currentThread().interrupt();
				}
			}

			switch (action) {
				case OPEN:
					open();
					break;
				case SAVE:
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
		action = DialogAction.OPEN;
		status = DialogStatus.WORKING;
		notifyAll();
	}

	private synchronized void open() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer buffer = stack.callocPointer(512);
			int dialogStatus = NFD_OpenDialog(buffer, null, (CharSequence) null);
			switch (dialogStatus) {
				case NFD_OKAY -> lastFile = new File(buffer.getStringASCII());
				case NFD_CANCEL -> System.out.println("User cancelled");
				default -> System.err.println("Error occurred: " + NFD_GetError());
			}
			action = DialogAction.NONE;
		}
		status = DialogStatus.IDLE;
		notifyAll();
	}

	/** Requests a save-file dialog. */
	public synchronized void saveFile() {
		action = DialogAction.SAVE;
		status = DialogStatus.WORKING;
		notifyAll();
	}

	private synchronized void save() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer buffer = stack.callocPointer(512);
			int dialogStatus = NFD_SaveDialog(buffer, null, (CharSequence) null, null);
			switch (dialogStatus) {
				case NFD_OKAY -> lastFile = new File(buffer.getStringASCII());
				case NFD_CANCEL -> System.out.println("User cancelled");
				default -> System.err.println("Error occurred: " + NFD_GetError());
			}
			action = DialogAction.NONE;
		}
		status = DialogStatus.IDLE;
		notifyAll();
	}

	/**
	 * Waits for the active dialog and gets its selected file.
	 *
	 * @return selected file, or {@code null} if the dialog was cancelled
	 * @throws InterruptedException if waiting is interrupted
	 */
	public synchronized File getLastFile() throws InterruptedException {
		while (status == DialogStatus.WORKING)
			wait();

		File result = lastFile;
		lastFile = null;
		return result;
	}
}
