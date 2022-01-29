package elevatorSubsystem;

import java.awt.Color;

import javax.swing.JTextArea;

import dataSystems.Configuration;


/**
 * This calss will take care of the moving elevator which represents the ElevatorMotor
 * @author Taher Shabaan 101073767
 */
public class ElevatorMotor {
	/**
	 * Used bu elevator when there is a need to move up 1 floor
	 * @throws InterruptedException
	 */
	public void ascendFloor(int numOfFloor, int idOfCar,JTextArea console) throws InterruptedException {
		console.setBackground(Color.decode("#82ff7d")); //Light purple
		Thread.sleep((long) Configuration.SPEED_FROM_FLOOR_TO_ANOTHER);
		System.out.println("ELEVATOR #" + idOfCar + " MOTOR: Elevator has ascended from floor " + numOfFloor + " to floor " + (numOfFloor+1) + "!");
		console.append("ELEVATOR #" + idOfCar + " MOTOR: Elevator has ascended from floor " + numOfFloor + " to floor " + (numOfFloor+1) + "!\n");
		console.setBackground(Color.decode("#ffffff")); //Light purple

	}
	
	/**
	 * Used by elevator when there is a need to move down 1 floor
	 * @throws InterruptedException
	 */
	public void descendFloor(int numOfFloor, int idOfCar,JTextArea console) throws InterruptedException {
		console.setBackground(Color.decode("#fff37d")); //Light purple
		Thread.sleep((long) Configuration.SPEED_FROM_FLOOR_TO_ANOTHER);
		System.out.println("ELEVATOR #" + idOfCar + " MOTOR: Elevator has descended from floor " + numOfFloor + " to floor " + (numOfFloor-1) + "!");
		console.append("ELEVATOR #" + idOfCar + " MOTOR: Elevator has descended from floor " + numOfFloor + " to floor " + (numOfFloor-1) + "!\n");
		console.setBackground(Color.decode("#ffffff")); //Light purple
	}
}
