package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import floorSubsystem.FloorLamp;
import dataSystems.ElevatorDirection;

class TestFloorLamp {
	
	@Test
	void testFloorLamp() throws InterruptedException {
		//Create a Floor Lamp for floor #1's up direction
		FloorLamp lamp = new FloorLamp(1, ElevatorDirection.Up);
		
		//Turn the lamp on
		lamp.turnOn();
		//Check to see that the lamp is on
		assertTrue(lamp.isOn());
		
		//Turn the lamp off
		lamp.turnOff();
		//Check to see that the lamp is off
		assertFalse(lamp.isOn());
		
		//Check to see that the lamps direction is up
		assertEquals(ElevatorDirection.Up, lamp.getLampDirection());
		
	}
}
