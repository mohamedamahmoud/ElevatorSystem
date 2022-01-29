package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import elevatorSubsystem.ElevatorMotor;
import dataSystems.Configuration;

class TestElevatorMotor {
	
	@Test
	void testAscendingFloor() throws InterruptedException {
		//Create a Elevator Motor
		ElevatorMotor motor = new ElevatorMotor();

		//get the time before ascending a floor
		long timeBeforeMethod = new Date().getTime();
		//ascend a single floor
		//motor.ascendFloor(1,1);
		//get the time after ascending the floor
		long timeAfterMethod = new Date().getTime();

		//calculate how long it took to ascend the floor
		long timeTakenToAscend = (timeAfterMethod-timeBeforeMethod);
		
		//check to make sure that the floor took at least the amount of time as specified in the configuration file
		
		assertTrue(timeTakenToAscend<=((long)Configuration.SPEED_FROM_FLOOR_TO_ANOTHER));
	}
	
	@Test
	void testDescendingFloor() throws InterruptedException {
		//Create a Elevator Motor
		ElevatorMotor motor = new ElevatorMotor();

		//get the time before descending a floor
		long timeBeforeMethod = new Date().getTime();
		//descend a single floor (5 is the number of the floor)
		//motor.descendFloor(5,1);
		//get the time after descending the floor
		long timeAfterMethod = new Date().getTime();

		//calculate how long it took to descend the floor
		long timeTakenToDescend = (timeAfterMethod-timeBeforeMethod);
		
		//check to make sure that the floor took at least the amount of time as specified in the configuration file
		
		assertTrue(timeTakenToDescend<=((long)Configuration.SPEED_FROM_FLOOR_TO_ANOTHER));
	}
}
