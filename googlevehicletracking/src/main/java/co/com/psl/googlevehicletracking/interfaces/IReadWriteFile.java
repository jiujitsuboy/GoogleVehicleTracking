package co.com.psl.googlevehicletracking.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import co.com.psl.googlevehicletracking.classes.Route;

public interface IReadWriteFile {

	/**
	 * Read the implementation configured file and return a Route for each valid line
	 * @return Route[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	Route[] readInputFile() throws FileNotFoundException,IOException;

	/**
	 * Write to the implementation configured file each Route log
	 * @param routes
	 * @throws IOException
	 */
	void writeOutputFile(Route[] routes) throws IOException;
}
