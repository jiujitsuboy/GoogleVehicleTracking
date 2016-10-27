package co.com.psl.googlevehicletracking.interfaces;

import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;

public interface IVehicle {

	void doScheduledRoutes() throws GoogleVehicleTrackingException;
	Route[] getRoutes();	
}
