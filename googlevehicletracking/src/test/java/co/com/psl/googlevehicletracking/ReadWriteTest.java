package co.com.psl.googlevehicletracking;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import co.com.psl.googlevehicletracking.classes.Passenger;
import co.com.psl.googlevehicletracking.classes.ReadWriteFile;
import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.classes.SpatialVehicleLocation;
import co.com.psl.googlevehicletracking.enums.VehicleMovement;
import co.com.psl.googlevehicletracking.interfaces.IReadWriteFile;

public class ReadWriteTest {

	/**
	 * Exception variable to catch specific test exceptions
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void testReadThreeLines() throws FileNotFoundException, IOException {
		int NumberofLinesRead = 0;
		final int EXPECTED_NUMBER_OF_LINES_READ = 3;

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		try {
			IReadWriteFile readWriteFile = new ReadWriteFile();
			NumberofLinesRead = readWriteFile.readInputFile().length;
			assertEquals(EXPECTED_NUMBER_OF_LINES_READ, NumberofLinesRead);
		} finally {
			DeleteFile("in.txt");
		}

	}

	@Test
	public final void testReadEmptyFile() throws FileNotFoundException, IOException {

		final int EXPECTED_NUMBER_OF_LINES_READ = 1;

		CreateFile("in.txt", Arrays.asList(""));

		try {
			IReadWriteFile readWriteFile = new ReadWriteFile();
			Route[] routes = readWriteFile.readInputFile();
			assertEquals(EXPECTED_NUMBER_OF_LINES_READ, routes.length);
		} finally {
			DeleteFile("in.txt");
		}

	}

	@Test
	public final void testReadNonExistingFile() throws FileNotFoundException, IOException {

		DeleteFile("in.txt");
		thrown.expect(FileNotFoundException.class);
		thrown.expectMessage("File not found [in.txt]");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testReadBadFormatPassengerID() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4.3258.987, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(NumberFormatException.class);
		thrown.expectMessage("passenger ID [4.3258.987] must contain only numbers");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testReadTooShortPassengerID() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848, JUAN MACHADO,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger ID must be between " + Passenger.ID_MIN_LENGHT + " and "
				+ Passenger.ID_MAX_LENGHT + " digits");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testReadTooLongPassengerID() throws FileNotFoundException, IOException {

		CreateFile("in.txt",
				Arrays.asList(
						"478584812347858481234785848123478584812347858481234, JUAN MACHADO  ,juanMachado@psl.com.co,FFFFLFFR",
						"43258987, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger ID must be between " + Passenger.ID_MIN_LENGHT + " and "
				+ Passenger.ID_MAX_LENGHT + " digits");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testReadTooLongPassengerName() throws FileNotFoundException, IOException {

		CreateFile("in.txt",
				Arrays.asList(
						"4785848123, JUAN MACHADO RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR RRRRRRRRRR ,juanMachado@psl.com.co,FFFFLFFR",
						"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Passenger name lenght must be max " + Passenger.NAME_MAX_LENGHT + " characters");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testInvalidPassengerEmail() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO ,juanMachadoApsl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid email address [juanMachadoApsl.com.co]");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testRouteInvalidMovementToken() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "7895871234,PEDRO HEREDIA,pedro@google.com,FFFFFY"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid vehicle movement [Y]");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testRouteEmptyTokens() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co, ",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("White spaces are invalid tokens for a route");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testRouteOnlyTwoTokens() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848, JUAN MACHADO", "43258987, MARIA DELGADO, RRFLFR",
				"789587,PEDRO HEREDIA,pedro@google.com,FFLFDFR"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("The number of tokens for a route must be between 3 and 4");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testRouteFiveTokens() throws FileNotFoundException, IOException {

		CreateFile("in.txt", Arrays.asList("4785848123, JUAN MACHADO ,juanMachado@psl.com.co,FFFFLFFR",
				"4325898712, MARIA DELGADO, RRFLFR", "789587,PEDRO HEREDIA,pedro@google.com,FFLFFR,FFFFF"));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("The number of tokens for a route must be between 3 and 4");

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.readInputFile();

	}

	@Test
	public final void testWriteOutputFile() throws IOException {

		Route route1 = new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD });
		route1.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route2 = new Route(new Passenger(52705168, "Alejandra Mendez"),
				new VehicleMovement[] { VehicleMovement.RIGHT, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD });
		route2.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route3 = new Route(new Passenger(899765234, "Wanda Maria"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD });
		route3.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route route4 = new Route(new Passenger(1234567654, "Brandy Liliana"),
				new VehicleMovement[] { VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.LEFT,
						VehicleMovement.FORWARD, VehicleMovement.FORWARD, VehicleMovement.RIGHT,
						VehicleMovement.FORWARD });
		route4.setSpatialVehicleLocation(new SpatialVehicleLocation());

		Route[] routes = new Route[] { route1, route2, route3, route4 };

		IReadWriteFile readWriteFile = new ReadWriteFile();
		readWriteFile.writeOutputFile(routes);

	}

	/**
	 * Helper class which creates a file with the specify name and lines of
	 * content
	 * 
	 * @param fileName
	 * @param lstLine
	 */
	private static void CreateFile(String fileName, List<String> lstLine) {
		Path file = Paths.get(fileName);
		try {
			Files.write(file, lstLine, Charset.forName("UTF-8"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Helper class which delete the specify file
	 * 
	 * @param fileName
	 */
	private static void DeleteFile(String fileName) {
		Path file = Paths.get(fileName);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
