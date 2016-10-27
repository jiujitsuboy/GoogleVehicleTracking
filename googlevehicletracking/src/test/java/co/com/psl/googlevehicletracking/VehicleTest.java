package co.com.psl.googlevehicletracking;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.com.psl.googlevehicletracking.classes.Passenger;
import co.com.psl.googlevehicletracking.classes.Route;
import co.com.psl.googlevehicletracking.classes.Vehicle;
import co.com.psl.googlevehicletracking.enums.VehicleMovement;
import co.com.psl.googlevehicletracking.exception.GoogleVehicleTrackingException;
import co.com.psl.googlevehicletracking.interfaces.IVehicle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class VehicleTest {

	@Autowired
	public ApplicationContext applicationContext;

	/**
	 * Exception variable to catch specific test exceptions
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void vehicleThreeTrips() throws GoogleVehicleTrackingException {

		final int NUMBER_OF_ROUTES = 3;
		final String ROUTE1_LOG = "(2,3) NORTH Direction, Distance: 0.5 Kilometers";
		final String ROUTE2_LOG = "(4,1) EAST Direction, Distance: 0.4 Kilometers";
		final String ROUTE3_LOG = "(7,3) EAST Direction, Distance: 0.5 Kilometers";

		Route[] routes = new Route[] {
				new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(79949875, "Alejandra Mendez"),
						new VehicleMovement[] { VehicleMovement.RIGHT,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(79949875, "Wanda Maria"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD }) };

		IVehicle vehicle = (IVehicle) applicationContext.getBean("IVehicle",
				new Object[] { routes });
		vehicle.doScheduledRoutes();
		Route[] routesTraveled = vehicle.getRoutes();

		assertEquals(NUMBER_OF_ROUTES, routesTraveled.length);
		assertEquals(ROUTE1_LOG, routesTraveled[0].toString());
		assertEquals(ROUTE2_LOG, routesTraveled[1].toString());
		assertEquals(ROUTE3_LOG, routesTraveled[2].toString());

	}

	@Test
	public final void vehicleMoreThanThreeTrips() {

		Route[] routes = new Route[] {
				new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(52705168, "Alejandra Mendez"),
						new VehicleMovement[] { VehicleMovement.RIGHT,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(899765234, "Wanda Maria"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(1234567654, "Brandy Liliana"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD })

		};

		thrown.expect(org.springframework.beans.factory.BeanCreationException.class);
		thrown.expectMessage("The number of routes to perform must be less or equal to "
				+ Vehicle.MAX_NUMBER_OF_ROUTES);

		applicationContext.getBean("IVehicle", new Object[] { routes });

	}

	@Test
	public final void vehicleMovementOutsideBlockCoverageArea()
			throws GoogleVehicleTrackingException {

		Route[] routes = new Route[] {
				new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(52705168, "Alejandra Mendez"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD }) };

		thrown.expect(GoogleVehicleTrackingException.class);
		thrown.expectMessage("Route outside the coverage area");

		IVehicle vehicle = (IVehicle) applicationContext.getBean("IVehicle", new Object[] { routes });
		vehicle.doScheduledRoutes();

	}

	@Test
	public final void vehicleTravelTwoKilometers()
			throws GoogleVehicleTrackingException {

		final double TWO_KILOMETERS_TRAVEL_DISTANCE = 2d;

		Route[] routes = new Route[] { new Route(new Passenger(79949875,
				"Jose Alejandro Nino Mora"), new VehicleMovement[] {
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.RIGHT, VehicleMovement.RIGHT,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD,
				VehicleMovement.FORWARD, VehicleMovement.FORWARD }) };
		
		IVehicle vehicle = (IVehicle) applicationContext.getBean("IVehicle", new Object[] { routes });
		vehicle.doScheduledRoutes();

		assertEquals(TWO_KILOMETERS_TRAVEL_DISTANCE,
				vehicle.getRoutes()[0].getDistanceInKm(), 0.000001);
	}

	@Test
	public final void onlyOneInstanceOfVehicle() {

		Route[] routes = new Route[] {
				new Route(new Passenger(79949875, "Jose Alejandro Nino Mora"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.RIGHT,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD, VehicleMovement.LEFT,
								VehicleMovement.FORWARD }),
				new Route(new Passenger(52705168, "Alejandra Mendez"),
						new VehicleMovement[] { VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD,
								VehicleMovement.FORWARD }) };

		
		IVehicle vehicle1 = (IVehicle) applicationContext.getBean("IVehicle", new Object[] { routes });		
		IVehicle vehicle2 = (IVehicle) applicationContext.getBean("IVehicle", new Object[] { routes });

		assertEquals(vehicle1, vehicle2);
	}

}
