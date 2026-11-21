package hu.szkristof.BFEngine;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.Preferences;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;

public class BFEngine {

	private String source;
	private PrintStream output;
	private InputStream input;

	private char[] memory;
	private int dataPointer;
	private int programCounter;

	public BFEngine() {
		this(System.out, System.in);
	}

	public BFEngine(PrintStream output, InputStream input) {
		Preferences prefs = BFEditor.getInstance().getPreferences();

		this.memory = new char[prefs.getMemoryCellCount()];
		this.output = output;
		this.input = input;

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
		Thread interpreterThread = new Thread(() -> {
			try {
				executeImpl();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		interpreterThread.start();
	}

	private void executeImpl() throws IOException {
		Preferences prefs = BFEditor.getInstance().getPreferences();

		while (programCounter < source.length()) {
			char currentInstruction = source.charAt(programCounter);

			switch (currentInstruction) {
				case '>':
					dataPointer++;
					break;
				case '<':
					dataPointer--;
					break;
				case '+':
					switch (prefs.getMemoryCellOverflowBehaviour()) {
						case WrapAround -> memory[dataPointer]++;
						case Cap -> memory[dataPointer] = (char) Math.max((memory[dataPointer] + 1) % 256, memory[dataPointer]);
						default -> throw new RuntimeException("Unknown MemoryCellOverflowBehaviour");
					}
					break;
				case '-':
					switch (prefs.getMemoryCellOverflowBehaviour()) {
						case WrapAround -> memory[dataPointer]--;
						case Cap -> memory[dataPointer] = (char) Math.min((memory[dataPointer] - 1) % 256, memory[dataPointer]);
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
						continue;
					}
					break;
				case ']':
					programCounter = searchLoopStart();
					continue;
			}

			programCounter++;
		}

		output.println();
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

}
