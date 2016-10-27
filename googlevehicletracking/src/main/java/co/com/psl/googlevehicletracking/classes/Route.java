package co.com.psl.googlevehicletracking.classes;

import co.com.psl.googlevehicletracking.enums.VehicleMovement;

/**
 * Represent a route traveled by the vehicle
 * @author Alejandro
 *
 */
public class Route {
	
	private Passenger passenger; 
	private VehicleMovement [] vehicleMovement; 
    private double distanceInKm;
    private SpatialVehicleLocation spatialVehicleLocation;    
    
    /**
     * Default constructor
     */
    public Route(){}
    /**
     * Create a new route to be traveled by the vehicle
     * @param passenger  passenger info
     * @param vehicleMovement instruction that conform the route
     */
	public Route(Passenger passenger, VehicleMovement[] vehicleMovement) {
		super();
		this.passenger = passenger;
		this.vehicleMovement = vehicleMovement;
	}

	/**
	 * Get the route's passenger
	 * @return Passenger
	 */
	public Passenger getPassenger() {
		return passenger;
	}

	/**
	 * Get the instruction that conform the route
	 * @return  VehicleMovement[]
	 */
	public VehicleMovement[] getVehicleMovement() {
		return vehicleMovement;
	}

	/**
	 * Get the distance in Km of the route traveled by the vehicle
	 * @return double
	 */
	public double getDistanceInKm() {
		return distanceInKm;
	}
	
	
	/**
	 * 	Set the distance in Km of the route traveled by the vehicle
	 * @param distanceInKm distance in Km of the route traveled by the vehicle
	 */
	public void setDistanceInKm(double distanceInKm) {
		this.distanceInKm = distanceInKm;
	}

	/**
	 * Get the final location of the vehicle after traveling the route
	 * @return SpatialVehicleLocation
	 */
	public SpatialVehicleLocation getSpatialVehicleLocation() {
		return spatialVehicleLocation;
	}
	
	
	/**
	 * Set the final location of the vehicle after traveling the route
	 * @param spatialVehicleLocation location of the vehicle
	 */
	public void setSpatialVehicleLocation(SpatialVehicleLocation spatialVehicleLocation) {
		this.spatialVehicleLocation = spatialVehicleLocation;
	}

	@Override
	public String toString() {
		
		StringBuilder routeStringRepresentation = new  StringBuilder();
		routeStringRepresentation.append("(")
		.append(spatialVehicleLocation.getxCoordinate())
		.append(",")
		.append(spatialVehicleLocation.getyCoordinate())
		.append(") ")
		.append(spatialVehicleLocation.getCardinalPoint())
		.append(" Direction, Distance: ")
		.append(distanceInKm)
		.append(" Kilometers");
		
		return routeStringRepresentation.toString();
	}
	
	
    
}
