package co.com.psl.googlevehicletracking.classes;

import co.com.psl.googlevehicletracking.enums.CardinalPoint;
import co.com.psl.googlevehicletracking.enums.VehicleMovement;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;
import co.com.psl.googlevehicletracking.interfaces.IVehicle;

/**
 * Represent the vehicle which travel the routes
 * 
 * @author Alejandro
 *
 */
public class Vehicle implements IVehicle {

	private static Vehicle vehicle;
	private Route[] routes;
	private SpatialVehicleLocation currentSpatialVehicleLocation;
	public static final double BLOCK_DISTANCE = 0.1;
    public static final double MAX_BLOCKS_IN_ALL_DIRECTIONS = 10;
    public static final int MAX_NUMBER_OF_ROUTES = 3;

	/**
	 * Create a new vehicle (singleton)
	 * 
	 * @param routes
	 */
	private Vehicle(Route[] routes) {		
		super();
		setRoutes(routes);		
	}

	/**
	 * Return a singleton Vehicle
	 * 
	 * @param routes
	 *            routes to be traveled by the vehicle
	 * @return Vehicle
	 */
	public static IVehicle getVehicle(Route[] routes) {
		if (vehicle == null) {
			vehicle = new Vehicle(routes);
		}
		else{
			vehicle.setRoutes(routes);			
		}
		vehicle.currentSpatialVehicleLocation = new SpatialVehicleLocation();

		return vehicle;
	}

	/**
	 * Get the routes to travel by the vehicle
	 * 
	 * @return Route[]
	 * @see Route
	 */
	@Override
	public Route[] getRoutes() {
		return routes;
	}
	
	
	/**
	 * Set the routes to travel by the vehicle
	 * @param routes routes to travel by the vehicle
	 */
	public void setRoutes(Route[] routes) {
		if (routes.length > Vehicle.MAX_NUMBER_OF_ROUTES) {
			throw new IllegalArgumentException("The number of routes to perform must be less or equal to " + Vehicle.MAX_NUMBER_OF_ROUTES);
		}
		this.routes = routes;
	}

	/**
	 * Get the current vehicle location
	 * 
	 * @return SpatialVehicleLocation
	 */
	public SpatialVehicleLocation getCurrentSpatialVehicleLocation() {
		return currentSpatialVehicleLocation;
	}

	/**
	 * Perform all the routes assigned to the vehicle
	 * 
	 * @throws GoogleVehicleTrackingException
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public void doScheduledRoutes() throws GoogleVehicleTrackingException {

		for (int route = 0; route < routes.length; route++) {
			try {
				performRoute(routes[route]);
			} catch (CloneNotSupportedException e) {				
				throw new GoogleVehicleTrackingException("Clonning Exception",e);
			}
		}
	}

	/**
	 * Perform a specific route assigned to the vehicle
	 * 
	 * @param route
	 *            route to perfom
	 * @throws GoogleVehicleTrackingException
	 * @throws CloneNotSupportedException 
	 */
	private void performRoute(Route route) throws GoogleVehicleTrackingException, CloneNotSupportedException {
		VehicleMovement[] vehicleMovement = route.getVehicleMovement();
		double traveledDistance = 0;

		for (int movement = 0; movement < vehicleMovement.length; movement++) {

			switch (vehicleMovement[movement]) {
			case FORWARD:
				moveVehicleForward();
				traveledDistance += Vehicle.BLOCK_DISTANCE;
				break;
			case LEFT:
				moveVehicleLeft();
				break;
			case RIGHT:
				moveVehicleRight();
				break;
			}
		}		

		route.setDistanceInKm(traveledDistance);
		route.setSpatialVehicleLocation(currentSpatialVehicleLocation.clone());
	}

	/**
	 * Evaluate if the next block of the route is within the MAX_BLOCKS_IN_ALL_DIRECTIONS 
	 * @param nextBlock next block of the route 
	 * @return boolean
	 */
	private boolean isVehicleMovementInsideRouteCoverageArea(int nextBlock) {
		return Math.abs(nextBlock) <= Vehicle.MAX_BLOCKS_IN_ALL_DIRECTIONS;
	}

	/**
	 * Move the vehicle forward
	 * 
	 * @throws GoogleVehicleTrackingException
	 */
	private void moveVehicleForward() throws GoogleVehicleTrackingException {

		boolean isMovementInsideCoverageArea = false;
		int newCoordinateCompomentValue = 0;

		switch (currentSpatialVehicleLocation.getCardinalPoint()) {
		case NORTH:
			newCoordinateCompomentValue = currentSpatialVehicleLocation.getyCoordinate() + 1;
			isMovementInsideCoverageArea = isVehicleMovementInsideRouteCoverageArea(newCoordinateCompomentValue);
			if (isMovementInsideCoverageArea) {
				currentSpatialVehicleLocation.setyCoordinate(newCoordinateCompomentValue);
			}

			break;
		case SOUTH:
			newCoordinateCompomentValue = currentSpatialVehicleLocation.getyCoordinate() - 1;
			isMovementInsideCoverageArea = isVehicleMovementInsideRouteCoverageArea(newCoordinateCompomentValue);
			if (isMovementInsideCoverageArea) {
				currentSpatialVehicleLocation.setyCoordinate(newCoordinateCompomentValue);
			}

			break;
		case EAST:
			newCoordinateCompomentValue = currentSpatialVehicleLocation.getxCoordinate() + 1;
			isMovementInsideCoverageArea = isVehicleMovementInsideRouteCoverageArea(newCoordinateCompomentValue);
			if (isMovementInsideCoverageArea) {
				currentSpatialVehicleLocation.setxCoordinate(newCoordinateCompomentValue);
			}

			break;
		case WEST:
			newCoordinateCompomentValue = currentSpatialVehicleLocation.getxCoordinate() - 1;
			isMovementInsideCoverageArea = isVehicleMovementInsideRouteCoverageArea(newCoordinateCompomentValue);
			if (isMovementInsideCoverageArea) {
				currentSpatialVehicleLocation.setxCoordinate(newCoordinateCompomentValue);
			}

			break;
		}

		if (!isMovementInsideCoverageArea) {
			throw new GoogleVehicleTrackingException("Route outside the coverage area");
		}
	}

	/**
	 * Turn left the vehicle
	 */
	private void moveVehicleLeft() {
		switch (currentSpatialVehicleLocation.getCardinalPoint()) {
		case NORTH:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.WEST);
			break;
		case SOUTH:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.EAST);
			break;
		case EAST:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.NORTH);
			break;
		case WEST:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.SOUTH);
			break;
		}
	}

	/**
	 * Turn right the vehicle
	 */
	private void moveVehicleRight() {
		switch (currentSpatialVehicleLocation.getCardinalPoint()) {
		case NORTH:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.EAST);
			break;
		case SOUTH:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.WEST);
			break;
		case EAST:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.SOUTH);
			break;
		case WEST:
			currentSpatialVehicleLocation.setCardinalPoint(CardinalPoint.NORTH);
			break;
		}
	}

}
