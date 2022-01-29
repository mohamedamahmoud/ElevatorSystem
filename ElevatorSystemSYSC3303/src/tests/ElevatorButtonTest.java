package tests;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextArea;

import org.junit.jupiter.api.Test;

import dataSystems.UserHandler;
import elevatorSubsystem.ElevatorButton;
import elevatorSubsystem.ElevatorLamp;

class TestElevatorButton {
	
	@Test
	void testButtonPressed() throws InterruptedException {
		//Create a Elevator Lamp For Floor #1
		ElevatorLamp lamp = new ElevatorLamp(1,1);
		
		//Create a elevator button
		ElevatorButton button = new ElevatorButton(lamp);
		
		JTextArea area = new JTextArea();
		
		//press the button
		button.press(area);
		
		//Check to see that the button's lamp is now lit
		assertEquals(true, button.getElevatorLamp().isOn());

		//Also check to see that the button's floor number is 1
		assertEquals(1, button.getElevatorLamp().getFloorNum());
	}
}
