package co.com.psl.googlevehicletracking.classes;

import co.com.psl.googlevehicletracking.enums.CardinalPoint;

/**
 * Represent a location in cartesian coordinates
 * 
 * @author Alejandro
 *
 */
public class SpatialVehicleLocation implements Cloneable {

	private int xCoordinate;
	private int yCoordinate;
	private CardinalPoint cardinalPoint;

	/**
	 * Creates a new location with the defaul starting location
	 * @param cardinalPoint
	 */
	public SpatialVehicleLocation() {
		super();
		this.xCoordinate = 0;
		this.yCoordinate = 0;
		this.cardinalPoint = CardinalPoint.NORTH;
	}

	/**
	 * Creates a new location
	 * 
	 * @param xCoordinate
	 *            X position
	 * @param yCoordinate
	 *            Y position
	 * @param cardinalPoint
	 *            cardinal point which is facing the vehicle
	 */
	public SpatialVehicleLocation(int xCoordinate, int yCoordinate, CardinalPoint cardinalPoint) {
		super();
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.cardinalPoint = cardinalPoint;
	}

	/**
	 * Clone the current instance
	 * 
	 * @throws CloneNotSupportedException
	 */
	public SpatialVehicleLocation clone() throws CloneNotSupportedException {
		return (SpatialVehicleLocation) super.clone();
	}

	/**
	 * get the x coordinate
	 * 
	 * @return int
	 */
	public int getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * Get the y coordinate
	 * 
	 * @return int
	 */
	public int getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * Get the cardinal point the vehicle is facing
	 * 
	 * @return CardinalPoint
	 * @see CardinalPoint
	 */
	public CardinalPoint getCardinalPoint() {
		return cardinalPoint;
	}

	/**
	 * Set the x coordinate
	 * 
	 * @param xCoordinate
	 *            x coordinate
	 */
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * Set the y coordinate
	 * 
	 * @param yCoordinate
	 *            y coordinate
	 */
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * Set the cardinal point the vehicle is facing
	 * 
	 * @param cardinalPoint
	 *            cardinal point the vehicle is facing
	 */
	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}

}