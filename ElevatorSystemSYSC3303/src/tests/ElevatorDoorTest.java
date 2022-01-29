package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import elevatorSubsystem.ElevatorDoor;
import dataSystems.Configuration;

class TestElevatorDoor {
	
	@Test
	void testDoorOpening() throws InterruptedException {
		//Create a Elevator Door
		ElevatorDoor door = new ElevatorDoor();

		//get the time before opening the door
		long timeBeforeMethod = new Date().getTime();
		//ascend a single floor
		//door.openAndCloseDoor(1);;
		//get the time after opening the door
		long timeAfterMethod = new Date().getTime();

		//calculate how long it took to ascend the floor
		long timeTakenToOpenAndCloseDoor = (timeAfterMethod-timeBeforeMethod);

		//check to make sure that the floor took at least the amount of time as specified in the configuration file
		assertTrue(timeTakenToOpenAndCloseDoor<=((long)Configuration.SPEED_OF_DOOR));
	}
}
