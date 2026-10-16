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
			}

			programCounter++;
		}
		System.out.println();
	}

}
