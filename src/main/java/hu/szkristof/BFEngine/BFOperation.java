package hu.szkristof.BFEngine;

import java.io.IOException;

/** Represents an operation executed by the Brainfuck control thread. */
@FunctionalInterface
public interface BFOperation {

	/**
	 * Invokes the operation.
	 *
	 * @throws IOException if the operation encounters input or output I/O
	 */
	void invoke() throws IOException;

}
