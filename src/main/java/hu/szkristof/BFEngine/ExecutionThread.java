package hu.szkristof.BFEngine;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/** Executes queued Brainfuck operations on a dedicated control thread. */
public class ExecutionThread extends Thread {

	private AtomicBoolean running = new AtomicBoolean(true);
	private Queue<BFOperation> operationQueue = new ConcurrentLinkedQueue<>();

	/** Processes queued operations while the thread remains active. */
	@Override
	public synchronized void run() {
		while (running.get()) {
			while (operationQueue.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			BFOperation currentOperation = operationQueue.poll();
			try {
				currentOperation.invoke();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds an operation to the queue and wakes the control thread.
	 *
	 * @param currentOperation operation to execute
	 */
	public synchronized void pushOperation(BFOperation currentOperation) {
		operationQueue.offer(currentOperation);
		notifyAll();
	}
}
