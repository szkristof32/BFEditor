package hu.szkristof.BFEngine;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.Preferences;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class BFEngine {

	private String source;
	private PrintStream output;
	private InputStream input;

	private char[] memory;
	private int dataPointer;
	private int programCounter;

	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private ExecutionThread executionThread;

	private Preferences preferences;

	public BFEngine() {
		this(System.out, System.in);
	}

	public BFEngine(PrintStream output, InputStream input) {
		this.preferences = BFEditor.getInstance().getPreferences();

		this.memory = new char[preferences.getMemoryCellCount()];
		this.output = output;
		this.input = input;

		this.executionThread = new ExecutionThread();
		this.executionThread.start();

		reset();
	}

	public PrintStream getOutput() {
		return output;
	}

	public void reset() {
		Preferences prefs = BFEditor.getInstance().getPreferences();

		this.programCounter = 0;
		this.dataPointer = 0;

		if (memory.length != prefs.getMemoryCellCount())
			this.memory = new char[prefs.getMemoryCellCount()];
		Arrays.fill(this.memory, (char) prefs.getDefaultMemoryCellValue());
	}

	public void reset(String source) {
		this.source = source;
		reset();
	}

	public void execute() {
		executionThread.pushOperation(this::executeImpl);
	}

	private void executeImpl() throws IOException {
		isRunning.set(true);

		while (programCounter < source.length()) {
			stepImpl();
		}

		output.println();
		isRunning.set(false);
	}

	public void debug() {
		executionThread.pushOperation(this::debugImpl);
	}

	private void debugImpl() {
		isRunning.set(true);
	}

	public void stop() {
		executionThread.pushOperation(this::stopImpl);
	}

	private void stopImpl() {
		isRunning.set(false);
	}

	public void step() {
		executionThread.pushOperation(this::stepImpl);
	}

	private void stepImpl() throws IOException {
		if (!isRunning.get() || programCounter >= source.length())
			return;

		char currentInstruction = source.charAt(programCounter);

		switch (currentInstruction) {
			case '>':
				dataPointer++;
				break;
			case '<':
				dataPointer--;
				break;
			case '+':
				switch (preferences.getMemoryCellOverflowBehaviour()) {
					case WrapAround -> memory[dataPointer]++;
					case Cap ->
							memory[dataPointer] = (char) Math.max((memory[dataPointer] + 1) % 256, memory[dataPointer]);
					default -> throw new RuntimeException("Unknown MemoryCellOverflowBehaviour");
				}
				break;
			case '-':
				switch (preferences.getMemoryCellOverflowBehaviour()) {
					case WrapAround -> memory[dataPointer]--;
					case Cap ->
							memory[dataPointer] = (char) Math.min((memory[dataPointer] - 1) % 256, memory[dataPointer]);
					default -> throw new RuntimeException("Unknown MemoryCellOverflowBehaviour");
				}
				break;
			case '.':
				output.print(memory[dataPointer]);
				break;
			case ',':
				memory[dataPointer] = (char) input.read();
				break;
			case '[':
				if (memory[dataPointer] == 0) {
					programCounter = searchLoopEnd();
					return;
				}
				break;
			case ']':
				programCounter = searchLoopStart();
				return;
		}

		programCounter++;
	}

	private int searchLoopStart() {
		int innerLoopCount = 0;
		int pc = programCounter - 1;

		while (pc >= 0) {
			switch (source.charAt(pc)) {
				case '[':
					if (innerLoopCount > 0)
						innerLoopCount--;
					else
						return pc;
					break;
				case ']':
					innerLoopCount++;
			}
			pc--;
		}

		return -1;
	}

	private int searchLoopEnd() {
		int innerLoopCount = 0;
		int pc = programCounter + 1;

		while (pc < source.length()) {
			switch (source.charAt(pc)) {
				case '[':
					innerLoopCount++;
					break;
				case ']':
					if (innerLoopCount > 0)
						innerLoopCount--;
					else
						return pc + 1;
			}
			pc++;
		}

		return -1;
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	public int getProgramCounter() {
		return programCounter;
	}

	public int getDataPointer() {
		return dataPointer;
	}

	public char[] getMemory() {
		return memory;
	}

}
