package floorSubsystem;

import java.io.IOException;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

import dataSystems.InputInformation;
import dataSystems.TextFileReader;
import schedulerSubsystem.Scheduler;
import dataSystems.Configuration;
import dataSystems.ElevatorDirection;
import dataSystems.InputInformation;

/**
 * Represents a single Floor in the floor subsystem
 */
public class Floor extends Thread {
	private ArrayList<InputInformation> events;
	private FloorButton upButton;
	private FloorButton downButton;
	private FloorLamp upLamp;
	private FloorLamp downLamp;

	/**
	 * Creates a new floor for the floor subsystem
	 * 
	 * @param floorNum the Scheduler the floor subsystem will be dealing with
	 */
	public Floor(int floorNum, ArrayList<InputInformation> events) {
		this.events = events;
		this.upLamp = new FloorLamp(floorNum, ElevatorDirection.Up);
		this.downLamp = new FloorLamp(floorNum, ElevatorDirection.Down);
		this.upButton = new FloorButton(this.upLamp);
		this.downButton = new FloorButton(this.downLamp);
	}

	/**
	 * Used to check the direction of the pressed button
	 *
	 */
	public FloorButton getFloorButton(ElevatorDirection d) {
		if (d == ElevatorDirection.Down) {
			return downButton;
		} else if (d == ElevatorDirection.Up) {
			return upButton;
		}
		return null;
	}

	public synchronized void run() {

		Collections.sort(this.events, (a, b) -> a.getTime().compareTo(b.getTime()));

		// Wait for the first event to occur

		if (this.events.size() >= 1) {

			long seconds = Duration.between(Configuration.START_TIME, this.events.get(0).getTime()).getSeconds();

			try {
				Thread.sleep((long) (seconds * 1000 * Configuration.SIMULATION_SPEED));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		for (int i = 0; i < this.events.size(); i++) {
			// checking if the event is an error (serious or trivial) if so use pressError()
			// instead of press()
			if (this.events.get(i).isTrivialError() || this.events.get(i).isSeriousError()) {
				if (this.events.get(i).getElevatorDirection() == ElevatorDirection.Up) {
					try {
						this.upButton.pressError(this.events.get(i));
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}

				} else if (this.events.get(i).getElevatorDirection() == ElevatorDirection.Down) {
					try {
						this.downButton.pressError(this.events.get(i));
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			} else {

				// Press the button for the current event
				// Note that the FloorButton class is responsible for sending the request to the
				// Scheduler subsystem
				// The button will also turn off the lamps
				if (this.events.get(i).getElevatorDirection() == ElevatorDirection.Up) {
					try {
						this.upButton.press(this.events.get(i));
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				} else if (this.events.get(i).getElevatorDirection() == ElevatorDirection.Down) {
					try {
						this.downButton.press(this.events.get(i));
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			}
			// Wait until the next event should occur
			if (i < this.events.size() - 1) {
				long seconds = Duration.between(this.events.get(i).getTime(), this.events.get(i + 1).getTime())
						.getSeconds();
				try {
					Thread.sleep((long) (seconds * 1000 * Configuration.SIMULATION_SPEED));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws SocketException {
		TextFileReader input = new TextFileReader(Configuration.USER_ERROR_INPUT);
		// Find all of the floors
		ArrayList<Floor> floors = new ArrayList<Floor>();
		for (int i : input.getFloors()) {
			floors.add(new Floor(i, input.getEventsByFloor(i)));
		}
		// initiating the floor thread
		for (Floor floor : floors) {
			floor.start();
		}
	}
}
