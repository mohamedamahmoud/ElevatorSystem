package floorSubsystem;

import dataSystems.ElevatorDirection;

/**
 * The lamps that indicate which button was pressed
 */
public class FloorLamp {
	private int floorNum;
	private ElevatorDirection lampDirection;
	private boolean lit;
	
	/**
	 * Creates a new FloorLamp
	 */
	public FloorLamp(int floorNum, ElevatorDirection direction) {
		this.floorNum = floorNum;
		this.lampDirection = direction;
		this.lit = false;
	}
	
	/**
	 * Turns the light on
	 */
	public void turnOn() {
		this.lit = true;
		System.out.println("FLOOR " + floorNum + ": " + (this.lampDirection == ElevatorDirection.Up ? "up " : "down ") + "lamp has been turned on!");
	}
	
	/**
	 * Turns the light off
	 */
	public void turnOff() {
		this.lit = false;
		System.out.println("FLOOR " + floorNum + ": " + (this.lampDirection == ElevatorDirection.Up ? "up " : "down ") + "lamp has been turned off.");
	}
	
	/**
	 * Checks if the light is on
	 * @return True of the light is on, false otherwise
	 */
	public boolean isOn() {
		return this.lit;
	}
	
	/**
	 * Gets the direction indicated by the lamp
	 * @return A direction
	 */
	public ElevatorDirection getLampDirection() {
		return this.lampDirection;
	}
	
	/**
	 * Returns the floor number associated to this lamp
	 * @return A floor number
	 */
	public int getFloorNum() {
		return this.floorNum;
	}
}
