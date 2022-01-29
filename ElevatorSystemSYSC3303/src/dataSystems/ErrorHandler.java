package dataSystems;


import java.time.LocalTime;

/**
 * SYSC 3303 PROJECT.
 * ErrorEvent takes care of the faulty events in the system using the interface InputEvent
 * @author Omar Agamy 101088548
 *
 */
public class ErrorHandler implements InputInformation{
	private LocalTime currentTime;
	private boolean trivialError;
	private boolean seriousError;
	private int floorSource;
	private ElevatorDirection elevatorDirection;
	private int floorDestination;
	private boolean isElevatorTaken;

	/*
	 *The ErrorEvent constructor takes the text input file with the format of hh:mm:ss.mm, serious/trivial, car button
	 * */
	public ErrorHandler(String input) {
		String[] inputVals = input.split(",");
		this.currentTime = LocalTime.parse(inputVals[0]);
		this.floorSource = Integer.valueOf(inputVals[1]);
		this.elevatorDirection = inputVals[2].equalsIgnoreCase("up") ? ElevatorDirection.Up : ElevatorDirection.Down;
		this.floorDestination = Integer.valueOf(inputVals[3]);
		this.trivialError = inputVals[4].equalsIgnoreCase("Trivial");
		this.seriousError = inputVals[4].equalsIgnoreCase("Serious");
		this.isElevatorTaken = false;
	}

	/*
	 * Gets the exact time of when the fault event happened
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
	 * When the fault is serious returns true otherwise it returns fault;a serious fault could be 
	 * for example when an elevator gets stuck between floors or the door opens between floors
	 * */
	@Override
	public boolean isSeriousError() {
		return this.seriousError;
	}

	/*
	 * When the fault is trivial returns true otherwise it returns fault; a trivial fault could be that the 
	 * lights would not change or the numbers on the lights are different or doors not opening 
	 * */
	@Override
	public boolean isTrivialError() {
		return this.trivialError;
	}

	/*
	 * Returns true if the there is an error being handled by an elevator
	 * */
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
	
	

	/*
	 * ToSring printing the exact fault and the time of the error occurences
	 * */
	@Override
	public String toString() {
		return "The time is " + currentTime + " The floor is " + floorSource + " The floor button selection is to go " + (this.elevatorDirection == ElevatorDirection.Up ? "Up" : "Down") + " The destination is to go to floor " + floorDestination + " The is a" + (this.isTrivialError() == true  ? " TRIVIAL" : " SERIOUS") + " error";
	}

	
}
