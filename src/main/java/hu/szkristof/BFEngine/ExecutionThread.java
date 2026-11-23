package hu.szkristof.BFEngine;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecutionThread extends Thread {

	private AtomicBoolean running = new AtomicBoolean(true);
	private Queue<BFOperation> operationQueue = new ConcurrentLinkedQueue<>();

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

	public synchronized void setCurrentOperation(BFOperation currentOperation) {
		operationQueue.offer(currentOperation);
		notifyAll();
	}
}
