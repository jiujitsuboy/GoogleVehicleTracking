package co.com.psl.googlevehicletracking.exception;

public class GoogleVehicleTrackingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5691543889226404144L;

	public GoogleVehicleTrackingException(String message){
		super(message);
	}
	
	public GoogleVehicleTrackingException(String message, Throwable cause){
		super(message,cause);
	}
}
