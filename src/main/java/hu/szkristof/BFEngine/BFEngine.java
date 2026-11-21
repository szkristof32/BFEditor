package hu.szkristof.BFEngine;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEditor.Preferences;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;

public class BFEngine {

	private String source;
	private PrintStream output;

	private byte[] memory;
	private int dataPointer;
	private int programCounter;

	public BFEngine() {
		this(System.out);
	}

	public BFEngine(PrintStream output) {
		Preferences prefs = BFEditor.getInstance().getPreferences();

		this.memory = new byte[prefs.getMemoryCellCount()];
		this.output = output;
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
			this.memory = new byte[prefs.getMemoryCellCount()];
		Arrays.fill(this.memory, (byte) prefs.getDefaultMemoryCellValue());
	}

	public void reset(String source) {
		this.source = source;
		reset();
	}

	public void execute() throws IOException {
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
						case Cap -> memory[dataPointer] = (byte) Math.max(memory[dataPointer] + 1, memory[dataPointer]);
						default -> throw new RuntimeException("Unknown MemoryCellOverflowBehaviour");
					}
					break;
				case '-':
					switch (prefs.getMemoryCellOverflowBehaviour()) {
						case WrapAround -> memory[dataPointer]--;
						case Cap -> memory[dataPointer] = (byte) Math.min(memory[dataPointer] - 1, memory[dataPointer]);
						default -> throw new RuntimeException("Unknown MemoryCellOverflowBehaviour");
					}
					break;
				case '.':
					output.print((char) memory[dataPointer]);
					break;
				case ',':
					Reader reader = new InputStreamReader(System.in);
					char[] buffer = {0};
					int bytesRead = reader.read(buffer, 0, 1);
					if (bytesRead == 1)
						memory[dataPointer] = (byte) buffer[0];
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
