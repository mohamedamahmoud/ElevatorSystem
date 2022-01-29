package dataSystems;


import java.time.LocalTime;

/**
 * SYSC 3303 PROJECT.
 * UserHandler takes care of the faulty events in the system using the interface InputInformation
 *@author Omar Agamy 101088548
 *
 */
public class UserHandler implements InputInformation {
	private LocalTime currentTime;
	private int floorSource;
	private ElevatorDirection elevatorDirection;
	private int floorDestination;	
	private boolean isElevatorTaken;

	
	/*
	 * Creating the user with the input time, floor. and if it going up or down
	 * */
	public UserHandler(String input) {
		String[] inputVals = input.split(",");
		this.currentTime = LocalTime.parse(inputVals[0]);
		this.floorSource = Integer.valueOf(inputVals[1]);
		this.elevatorDirection = inputVals[2].equalsIgnoreCase("up") ? ElevatorDirection.Up : ElevatorDirection.Down;
		this.floorDestination = Integer.valueOf(inputVals[3]);
	}

	/*
	 * Gets exact time
	 * */
	@Override
	public LocalTime getTime() {
		return this.currentTime;
	}

	/*
	 * Gets the floor for which the button was selected
	 * */
	@Override
	public int getFloorSource() {
		return this.floorSource;
	}

	/*
	 * Gets where the elevator is going
	 * */
	@Override
	public int getFloorDestination() {
		return this.floorDestination;
	}

	/*
	 *Gets if the elevator is going up or not 
	 * */
	@Override
	public ElevatorDirection getElevatorDirection() {
		return this.elevatorDirection;
	}

	/*
	 * It isn't a fault so this method does not associate with PanssengerInput
	 * */
	@Override
	public boolean isSeriousError() {
		return false;
	}

	/*
	 * It isn't a fault so this method does not associate with PanssengerInput
	 * */
	@Override
	public boolean isTrivialError() {
		return false;
	}
	
	
	@Override
	public boolean isElevatorTaken() {
		return this.isElevatorTaken;
	}

	/*
	 * gets the elevator to handle the event
	 * */
	@Override
	public void elevatorTakeEvent() {
		this.isElevatorTaken=true;
	}


	
	@Override
	public String toString() {
		return "The time is " + currentTime + " The floor is " + floorSource + " The floor button selection is to go " + (this.elevatorDirection == ElevatorDirection.Up ? "Up" : "Down") + " The destination is to go to floor " + floorDestination ;
	}


}
