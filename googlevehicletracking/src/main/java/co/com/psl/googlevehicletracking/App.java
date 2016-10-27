package co.com.psl.googlevehicletracking;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import co.com.psl.googlevehicletracking.classes.ReadWriteFile;
import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.classes.Vehicle;
import co.com.psl.googlevehicletracking.interfaces.IReadWriteFile;
import co.com.psl.googlevehicletracking.interfaces.IVehicle;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		System.out
				.println("Specify the routes the vehicle must perfom in the in.txt file following the next conventions:");
		System.out.println("1 - Each route must be a single line");
		System.out.println("2 - Each route must contain the next format");
		System.out
				.println("	* 'PassengerID','Passenger Name','Passenger Email {optional}, Vehicle Movements {F = FORWAR, L = LEFT, R = RIGHT}'");
		System.out.println("3 - Max number of routes must be 3");
		System.out
				.println("The result of the vehicle travel will be recorded in the out.txt file");
		System.out.println("");
		System.out.println("");

		try (ClassPathXmlApplicationContext cac = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml")) {
			IReadWriteFile readWriteFile = (IReadWriteFile) cac
					.getBean("IReadWriteFile");
			Route[] routesToTravel = readWriteFile.readInputFile();
			IVehicle vehicle = (IVehicle) cac.getBean("IVehicle",
					new Object[] { routesToTravel });
			vehicle.doScheduledRoutes();
			Route[] routesToTraveled = vehicle.getRoutes();

			readWriteFile.writeOutputFile(routesToTraveled);
		} catch (Exception e) {

			System.out.print(e.getMessage());
		}
		// try {
		// IReadWriteFile readWriteFile = new ReadWriteFile();
		// Route[] routesToTravel = readWriteFile.readInputFile();
		//
		// IVehicle vehicle = Vehicle.getVehicle(routesToTravel);
		// vehicle.doScheduledRoutes();
		// Route[] routesToTraveled = vehicle.getRoutes();
		//
		// readWriteFile.writeOutputFile(routesToTraveled);
		//
		// } catch (Exception e) {
		//
		// System.out.print(e.getMessage());
		// }
	}
}
