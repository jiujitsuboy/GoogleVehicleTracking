package co.com.psl.googlevehicletracking.classes;

/**
 * Represent a passanger of a trip
 * @author Alejandro
 *
 */
public class Passenger {
	
	private String name;
	private long ID;
	private String email;
	public static final int NAME_MAX_LENGHT = 200;
	public static final int ID_MIN_LENGHT = 10;
	public static final int ID_MAX_LENGHT = 50;
	
	
	/**
	 * Construct a new passenger
	 * @param ID identification of the passenger
	 * @param name name of the passenger	 
	 */
	public Passenger(long ID,String name) {
		super();
		this.name = name;
		this.ID = ID;
	}

	/**
	 * Construct a new passenger
	 * @param ID identification of the passenger
	 * @param name name of the passenger
	 * @param email electronic mail of the passenger
	 */
	public Passenger(long ID,String name, String email) {
		super();
		this.name = name;
		this.ID = ID;
		this.email = email;
	}

	/**
	 * Get the passenger name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the passenger ID
	 * @return long
	 */
	public long getID() {
		return ID;
	}

	/**
	 * Get the passenger email
	 * @return String
	 */
	public String getEmail() {
		return email;
		
	}
	
	
	
}
