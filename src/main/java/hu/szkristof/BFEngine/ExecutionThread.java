package hu.szkristof.BFEngine;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecutionThread extends Thread {

	private AtomicBoolean running = new AtomicBoolean(true);
	private BFOperation currentOperation = null;

	@Override
	public synchronized void run() {
		while (running.get()) {
			while (currentOperation == null) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			System.out.println(currentOperation);
			try {
				currentOperation.invoke();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			currentOperation = null;
		}
	}

	public synchronized void setCurrentOperation(BFOperation currentOperation) {
		this.currentOperation = currentOperation;
		notifyAll();
	}
}
