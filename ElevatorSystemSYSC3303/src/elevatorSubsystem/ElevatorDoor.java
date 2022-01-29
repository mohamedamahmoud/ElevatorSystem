package elevatorSubsystem;

import java.awt.Color;

import javax.swing.JTextArea;

import dataSystems.Configuration;
import dataSystems.Configuration;

/**
 * This class would represent an elevator door and opens or closes when needed
 * 
 * @author Taher Shabaan 101073767
 */
public class ElevatorDoor {
	/**
	 * Will be called by an elevator when needed to open and close door
	 * 
	 * @throws InterruptedException
	 */
	public void openAndCloseDoor(int carID,JTextArea console) throws InterruptedException {
		double sleepTime = Configuration.SPEED_OF_DOOR;
		Thread.sleep((long) sleepTime);
		if (!this.checkSleepTime(sleepTime)) {
			System.out.println("ELEVATOR #" + carID + " DOOR: Door has opened and closed!");
			console.append("ELEVATOR #" + carID + " DOOR: Door has opened and closed!"+"\n");
			console.setCaretPosition(console.getText().length());

		}
	}
	/**
	 * Will be called by an elevator to implement a
	 * trivial error and then opens and close the door with a 
	 * delay to simulate a trivial error
	 * 
	 * @throws InterruptedException
	 */
	public void openAndCloseDoorTrivialError(int carID,JTextArea console) throws InterruptedException {
		console.setBackground(Color.decode("#ff7f7f")); //Light purple
		System.out.println("An ERROR occurred in ELEVATOR #" + carID + "!!");
		console.append("An ERROR occurred in ELEVATOR #" + carID + "!!\n");
		double sleepTime = 4 * (Configuration.SPEED_OF_DOOR);
		Thread.sleep((long) sleepTime);
		if(this.checkSleepTime(sleepTime)) {
		System.out.println("ELEVATOR #" + carID + " DOOR: Door did not open!");
		console.append("ELEVATOR #" + carID + " DOOR: Door did not open!\n");
		Thread.sleep((long) sleepTime);
		System.out.println("A TRIVIAL error occurred in ELEVATOR # "+carID+"\n" + "The door got stuck while opening but the door eventually opened and closed in ELEVATOR # "+carID );
		console.append("A TRIVIAL error occurred in ELEVATOR # "+carID+"\n" + "The door got stuck while opening but the door eventually opened and closed in ELEVATOR # "+carID+"\n");
		console.setCaretPosition(console.getText().length());
		console.setBackground(Color.decode("#ffffff")); //Light purple

		}
	}

	/**
	 * Will be called by an elevator to implement a
	 * serious error and then the elevator stops working
	 * as there would be "maintainence" on the elevator
	 * @throws InterruptedException
	 */
	public void openAndCloseDoorSeriousError(int carID,JTextArea console) throws InterruptedException {
		System.out.println("An ERROR occurred in ELEVATOR #" + carID + "!!");
		console.setBackground(Color.decode("#ff7f7f")); //Light purple
		console.append("An ERROR occurred in ELEVATOR #" + carID + "!!\n");
		double sleepTime = 2 * (Configuration.SPEED_OF_DOOR);
		Thread.sleep((long) sleepTime);
		System.out.println("ELEVATOR #" + carID + " DOOR: Door is not opening in ELEVATOR "+carID+" this is a SERIOUS ERROR");
		console.append("ELEVATOR #" + carID + " DOOR: Door is not opening in ELEVATOR "+carID+" this is a SERIOUS ERROR\n");
		Thread.sleep((long) sleepTime);
		System.out.println("Call Maintainence for ELEVATOR # " + carID);
		console.append("Call Maintainence for ELEVATOR # " + carID+"\n");
		System.out.println("ELEVATOR # " + carID +" has stopped working");
		console.setBackground(Color.decode("#d80000")); //Light purple
		console.append("ELEVATOR # " + carID +" has stopped working+\n");
		console.setCaretPosition(console.getText().length());
		

	}
	/**
	 * returns true if the doors open in the exact calculated time
	 * false otherwise
	 */
	private boolean checkSleepTime(double time) {
		if (time > Configuration.SPEED_OF_DOOR) {
			return true;
		}
		return false;
	}
}
