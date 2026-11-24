package hu.szkristof.BFEngine;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.Preferences;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Interprets and executes Brainfuck source code.
 * It maintains the memory segment, data pointer, instruction pointer,
 * input and output streams, and configured preferences.
 */
public class BFEngine {

	private final List<Character> validInstructions = Arrays.asList('+', '-', '>', '<', '.', ',', '[', ']');

	private String source;
	private final PrintStream output;
	private final InputStream input;

	private char[] memory;
	private int dataPointer;
	private int programCounter;

	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final ExecutionThread executionThread;

	private final Preferences preferences;

	/** Creates an engine connected to {@link System#out} and {@link System#in}. */
	public BFEngine() {
		this(System.out, System.in);
	}

	/**
	 * Creates an engine with the supplied output and input streams.
	 *
	 * @param output stream receiving Brainfuck output
	 * @param input  stream supplying character input
	 */
	public BFEngine(PrintStream output, InputStream input) {
		this.preferences = BFEditor.getInstance().getPreferences();

		this.memory = new char[preferences.getMemoryCellCount()];
		this.output = output;
		this.input = input;

		this.executionThread = new ExecutionThread();
		this.executionThread.start();

		reset();
	}

	/**
	 * Gets the stream used for program output.
	 *
	 * @return the output stream
	 */
	public PrintStream getOutput() {
		return output;
	}

	/**
	 * Resets the pointers and memory cells according to the current preferences.
	 */
	public void reset() {
		Preferences prefs = BFEditor.getInstance().getPreferences();

		this.programCounter = 0;
		this.dataPointer = 0;

		if (memory.length != prefs.getMemoryCellCount())
			this.memory = new char[prefs.getMemoryCellCount()];
		Arrays.fill(this.memory, (char) prefs.getDefaultMemoryCellValue());
	}

	/**
	 * Sets the source code and resets the execution state.
	 *
	 * @param source Brainfuck source code to execute
	 */
	public void reset(String source) {
		this.source = source;
		reset();
	}

	/** Queues execution of the complete source code. */
	public void execute() {
		executionThread.pushOperation(this::executeImpl);
	}

	private void executeImpl() throws IOException {
		isRunning.set(true);

		while (programCounter < source.length()) {
			processCurrentInstruction();
		}

		output.println();
		isRunning.set(false);
	}

	/** Starts debug mode for instruction-by-instruction execution. */
	public void debug() {
		executionThread.pushOperation(this::debugImpl);
	}

	private void debugImpl() {
		isRunning.set(true);
	}

	/** Stops the running or debugged program. */
	public void stop() {
		executionThread.pushOperation(this::stopImpl);
	}

	private void stopImpl() {
		isRunning.set(false);
	}

	/** Executes the next valid Brainfuck instruction. */
	public void nextInstruction() {
		executionThread.pushOperation(this::nextInstructionImpl);
	}

	private void nextInstructionImpl() throws IOException {
		while (programCounter < source.length() && !validInstructions.contains(source.charAt(programCounter))) {
			programCounter++;
		}

		processCurrentInstruction();

		if (programCounter >= source.length())
			stopImpl();
	}

	private void processCurrentInstruction() throws IOException {
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
					case WRAP_AROUND -> memory[dataPointer]++;
					case CAP ->
						memory[dataPointer] = (char) Math.max((memory[dataPointer] + 1) % 256, memory[dataPointer]);
					default -> throw new IllegalStateException("Unknown MemoryCellOverflowBehaviour");
				}
				break;
			case '-':
				switch (preferences.getMemoryCellOverflowBehaviour()) {
					case WRAP_AROUND -> memory[dataPointer]--;
					case CAP ->
						memory[dataPointer] = (char) Math.min((memory[dataPointer] - 1) % 256, memory[dataPointer]);
					default -> throw new IllegalStateException("Unknown MemoryCellOverflowBehaviour");
				}
				break;
			case '.':
				output.print(memory[dataPointer]);
				break;
			case ',':
				int inputValue = input.read();
				if (inputValue != -1) {
					memory[dataPointer] = (char) inputValue;
				}
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
			default:
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
					break;
				default:
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
					break;
				default:
			}
			pc++;
		}

		return -1;
	}

	/**
	 * Reports whether the engine is currently running.
	 *
	 * @return {@code true} while execution is active
	 */
	public boolean isRunning() {
		return isRunning.get();
	}

	/**
	 * Gets the current instruction pointer.
	 *
	 * @return position of the instruction pointer in the source
	 */
	public int getProgramCounter() {
		return programCounter;
	}

	/**
	 * Gets the current data pointer.
	 *
	 * @return index of the active memory cell
	 */
	public int getDataPointer() {
		return dataPointer;
	}

	/**
	 * Gets the engine's memory segment.
	 *
	 * @return the memory cells
	 */
	public char[] getMemory() {
		return memory;
	}

}
