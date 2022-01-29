package tests;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextArea;

import org.junit.jupiter.api.Test;
import elevatorSubsystem.ElevatorLamp;

class TestElevatorLamp {
	
	@Test
	void testLamp() throws InterruptedException {
		//Create a Elevator Lamp For Floor #1
		ElevatorLamp lamp = new ElevatorLamp(1,1);
		
		JTextArea area = new JTextArea();
		
		//turn on the lamp
		lamp.turnOn(area);
		
		//Check to see that the lamp is now on
		assertEquals(true, lamp.isOn());
		
		//turn off the lamp
		lamp.turnOff(area);
		
		//Check to see that the lamp is now off
		assertEquals(false, lamp.isOn());

		//Also check to see that the lamps's floor number is 1
		assertEquals(1, lamp.getFloorNum());
	}
}
