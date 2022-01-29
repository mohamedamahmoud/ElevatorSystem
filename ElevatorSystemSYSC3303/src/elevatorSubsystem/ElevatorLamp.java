
package elevatorSubsystem;

import javax.swing.JTextArea;

/**
 * The lamps that indicate which floor will be visited by the elevator
 * @author Taher Shabaan 101073767
 */
public class ElevatorLamp {
	private int numOfFloor, idOfCar;
	private boolean onOrOff;
	
	/**
	 * Creates a new ElevatorLamb
	 */
	public ElevatorLamp(int floorNum, int idOfCar) {
		this.idOfCar = idOfCar;
		this.onOrOff = false;
		this.numOfFloor = floorNum;
	}
	
	/**
	 * Switch the light on
	 */
	public void turnOn(JTextArea console) {
		this.onOrOff = true;
		System.out.println("ELEVATOR #" + this.idOfCar + " LAMP: Elevator's floor number " + numOfFloor + ": lamp has been turned on!");
		console.append("ELEVATOR #" + this.idOfCar + " LAMP: Elevator's floor number " + numOfFloor + ": lamp has been turned on!\n");
	}
	
	/**
	 * Switch the light off
	 */
	public void turnOff(JTextArea console) {
		this.onOrOff = false;
		System.out.println("ELEVATOR #" + this.idOfCar + " LAMP: Elevator's floor number " + numOfFloor + ": lamp has been turned off.");
		console.append("ELEVATOR #" + this.idOfCar + " LAMP: Elevator's floor number " + numOfFloor + ": lamp has been turned off.\n");
	}
	
	/**
	 * Make sure if the light is on
	 * @return True of the light is on, false otherwise
	 */
	public boolean isOn() {
		return this.onOrOff;
	}
	
	/**
	 * Returns the floor number associated to this lamp
	 * @return A floor number
	 */
	public int getFloorNum() {
		return this.numOfFloor;
	}
}
