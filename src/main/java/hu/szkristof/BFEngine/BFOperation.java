package hu.szkristof.BFEngine;

import java.io.IOException;

@FunctionalInterface
public interface BFOperation {

	void invoke() throws IOException;

}
