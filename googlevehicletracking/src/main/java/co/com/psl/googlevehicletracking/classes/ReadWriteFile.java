package co.com.psl.googlevehicletracking.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.com.psl.googlevehicletracking.enums.VehicleMovement;
import co.com.psl.googlevehicletracking.interfaces.IReadWriteFile;

public class ReadWriteFile implements IReadWriteFile {

	private static final String INPUT_FILE = "in.txt";
	private static final String OUTPUT_FILE = "out.txt";
	private static final int MIN_NUMBER_TOKENS = 3;
	private static final int MAX_NUMBER_TOKENS = 4;

	@Override
	public Route[] readInputFile() throws FileNotFoundException, IOException {

		List<Route> lstCommands = new ArrayList<>();
		File file = new File(INPUT_FILE);
		if (!file.exists()) {
			throw new FileNotFoundException("File not found [" + INPUT_FILE + "]");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				lstCommands.add(ParseRoute(line));
			}
		} catch (IOException ex) {
			throw ex;
		}

		return lstCommands.toArray(new Route[lstCommands.size()]);

	}

	@Override
	public void writeOutputFile(Route[] routes) throws IOException {

		// TODO Auto-generated method stub
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
			for (int route = 0; route < routes.length; route++) {
				bw.write(routes[route].toString());
				bw.newLine();
			}
			bw.flush();
		}
	}

	/**
	 * Parse a String representing a route to be travel by a vehicle
	 * 
	 * @param line
	 *            string representing the route
	 * @return Route
	 * @see Route
	 */
	private Route ParseRoute(String line) {

		Route route = null;

		if (line != null && line.length() > 0) {

			// split line into tokens
			String[] tokens = line.split(",");

			if (tokens.length < MIN_NUMBER_TOKENS || tokens.length > MAX_NUMBER_TOKENS) {

				throw new IllegalArgumentException("The number of tokens for the route [" + line + "] must be between "
						+ MIN_NUMBER_TOKENS + " and " + MAX_NUMBER_TOKENS);
			}

			if (!IsValidRouteTokens(tokens)) {
				throw new IllegalArgumentException("White spaces are invalid tokens for a route");
			}

			Passenger passenger = null;
			VehicleMovement[] vehicleMovements;

			Long passengerID = null;
			String passengerBeforeCastingID = null;
			String passengerName = null;
			String passengerEmail = null;
			String routeIntructions = null;

			passengerBeforeCastingID = tokens[0].trim();
			passengerName = tokens[1].trim();

			if (passengerBeforeCastingID.length() < Passenger.ID_MIN_LENGHT
					|| passengerBeforeCastingID.length() > Passenger.ID_MAX_LENGHT) {
				throw new IllegalArgumentException("Passenger ID must be between " + Passenger.ID_MIN_LENGHT + " and "
						+ Passenger.ID_MAX_LENGHT + " digits");
			}
			if (passengerName.length() > Passenger.NAME_MAX_LENGHT) {
				throw new IllegalArgumentException(
						"Passenger name lenght must be max " + Passenger.NAME_MAX_LENGHT + " characters");
			}

			try {
				passengerID = Long.parseLong(passengerBeforeCastingID);
			} catch (NumberFormatException ex) {
				throw new NumberFormatException(
						"passenger ID [" + passengerBeforeCastingID + "] must contain only numbers");
			}

			if (tokens.length == MIN_NUMBER_TOKENS) {

				routeIntructions = tokens[2].trim();
				passenger = new Passenger(passengerID, passengerName);

			}

			else {

				passengerEmail = tokens[2].trim();
				routeIntructions = tokens[3].trim();

				if (!passengerEmail.matches("^([\\w-\\.]+)@((?:[\\w]+\\.)+)([a-zA-Z]{2,4})$")) {
					throw new IllegalArgumentException("Invalid email address [" + passengerEmail + "]");
				}

				passenger = new Passenger(passengerID, passengerName, passengerEmail);

			}

			vehicleMovements = ParseMovement(routeIntructions);
			route = new Route(passenger, vehicleMovements);

		}

		return route;
	}

	/**
	 * Verify if the list of tokens representing a route configuration are valid
	 * 
	 * @param tokens
	 *            token list
	 * @return boolean
	 */
	private boolean IsValidRouteTokens(String[] tokens) {

		for (int token = 0; token < tokens.length; token++) {
			if (tokens[token].trim().length() == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Parse a string representing the movement of the vehicle
	 * 
	 * @param movements
	 *            string representing the movement of the vehicle
	 * @return VehicleMovement[]
	 */
	private VehicleMovement[] ParseMovement(String movements) {

		List<VehicleMovement> vehicleMovements = new ArrayList<>();

		if (movements != null && movements.length() > 0) {
			for (int character = 0; character < movements.length(); character++) {
				switch (movements.charAt(character)) {
				case 'F':
					vehicleMovements.add(VehicleMovement.FORWARD);
					break;
				case 'L':
					vehicleMovements.add(VehicleMovement.LEFT);
					break;
				case 'R':
					vehicleMovements.add(VehicleMovement.RIGHT);
					break;
				default:
					throw new IllegalArgumentException(
							"Invalid vehicle movement [" + movements.charAt(character) + "]");
				}
			}
		}

		return vehicleMovements.toArray(new VehicleMovement[vehicleMovements.size()]);
	}
}
