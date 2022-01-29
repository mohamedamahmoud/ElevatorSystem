package elevatorSubsystem;

import javax.swing.JTextArea;

/**
 * Represents the ElevatorButton used by a user to request a floor in the elevator
 * @author Taher Shabaan 101073767
 */
public class ElevatorButton {
	private ElevatorLamp elevatorLamp;
	private int numOfFloor;
	
	/**
	 * Creates a elevator button
	 * @param elevatorLamp The lamp associated to the button
	 */
	public ElevatorButton(ElevatorLamp elevatorLamp) {
		this.elevatorLamp = elevatorLamp;
		this.numOfFloor = elevatorLamp.getFloorNum();
	}
	
	/**
	 * Gets the elevator lamp
	 * @return A ElevatorLamp
	 */
	public ElevatorLamp getElevatorLamp() {
		return this.elevatorLamp;
	}
	
	/**
	 * Used when a passenger presses the button, will turn on the elevator lamp
	 */
	public void press(JTextArea console) {
		System.out.println("Elevator's floor number " + numOfFloor + " has been pressed.");
		console.append("Elevator's floor number " + numOfFloor + " has been pressed.\n");
		console.setCaretPosition(console.getText().length());
		this.elevatorLamp.turnOn(console);
	}
}
