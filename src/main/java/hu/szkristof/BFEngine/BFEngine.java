package hu.szkristof.BFEngine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class BFEngine {

	private static final int MEMORY_SIZE = 3000;

	private String source;

	private final byte[] memory;
	private int dataPointer;
	private int programCounter;

	public BFEngine() {
		this.memory = new byte[MEMORY_SIZE];
		reset();
	}

	public void reset() {
		this.programCounter = 0;
		this.dataPointer = 0;
		Arrays.fill(this.memory, (byte) 0);
	}

	public void reset(String source) {
		this.source = source;
		reset();
	}

	public void run() throws IOException {
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
					memory[dataPointer]++;
					break;
				case '-':
					memory[dataPointer]--;
					break;
				case '.':
					System.out.print((char) memory[dataPointer]);
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
						int loopEnd = searchLoopEnd();
						programCounter = loopEnd;
						continue;
					}
					break;
				case ']':
					int loopStart = searchLoopStart();
					programCounter = loopStart;
					continue;
			}

			programCounter++;
		}
		System.out.println();
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
