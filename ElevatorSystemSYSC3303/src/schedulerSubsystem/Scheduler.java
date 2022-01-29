package schedulerSubsystem;

import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dataSystems.Configuration;
import dataSystems.ElevatorDirection;
import dataSystems.InputInformation;


/**
 * The scheduler is used to communicate event data between floors and the elevator
 */
public class Scheduler {
	private LinkedList<InputInformation> eventQueue;
	
	/**
	 * Constructor to create a Scheduler
	 */
	public Scheduler() {
		this.eventQueue = new LinkedList<>();
	}
	
	public synchronized LinkedList<InputInformation> getQueuedEvents() {
		notifyAll();
		return this.eventQueue;
	}
	
	/**
	 * This method will be called by the floors to add input events to the queue
	 * Note that the floors will not wait to add events
	 * Floors should be able to add their event and wait until it has been processed
	 * @param event The event to add to the queue
	 * @throws InterruptedException 
	 */
	public synchronized void acceptEvent(InputInformation event) throws InterruptedException {
		this.eventQueue.add(event);
		notifyAll();
				
		// The Floor must wait until the event has been processed
		while (this.eventQueue.contains(event)) {
			wait();
		}
	}
	
	/**
	 * This method will be called to request the elevator to move if it is not moving already
	 * @param floorNum The floor the elevator is currently stationed at
	 * @return The ElevatorDirection in which the elevator must move towards
	 * @throws InterruptedException
	 */
	public synchronized ElevatorDirection requestWork(int floorNum) throws InterruptedException {
		while (eventQueue.size() == 0) {
			wait();
		}
		
		//Goes through all the events and returns the ElevatorDirection that the elevator must go to get to the first event
		//That is currently not being handled by the elevator. If no event is found, the ElevatorDirection will be Stop
		for (InputInformation event: eventQueue) {
			if (!event.isElevatorTaken()) {
				event.elevatorTakeEvent();
		        // If the elevator is Stop at the floor that the current event came from, don't move
				if (event.getFloorSource() == floorNum) {
					return ElevatorDirection.Stop;
				}
				//If the elevator is Stop at a floor below the first event, move up, otherwise move down
				return floorNum < event.getFloorSource() ? ElevatorDirection.Up : ElevatorDirection.Down;	
			}
		}
		return ElevatorDirection.Stop;
	}
	
	/**
	 * This method is called to checks if there is an event in the event queue 
	 * that is coming from the adjacent floor
	 * @param floorNum The floor number the elevator is currently at
	 * @param ElevatorDirection The ElevatorDirection in which the elevator is going towards
	 * @return True if there is an adjacent event, false otherwise
	 */
	public synchronized boolean checkForMoreEvents(int floorNum, ElevatorDirection ElevatorDirection) throws InterruptedException {
		return this.eventQueue.stream().anyMatch(event -> event.getElevatorDirection() == ElevatorDirection && event.getFloorSource() == floorNum + (ElevatorDirection == ElevatorDirection.Up ? 1 : -1));
	}
	
	/**
	 * This method is called by an elevator when it reaches a floor to take any appropriate queued events
	 * @param floorNum The floor number the elevator has reached
	 * @param ElevatorDirection The ElevatorDirection in which the elevator is going towards
	 * @return A list of events that has been taken by the elevator
	 * @throws InterruptedException
	 */
	public synchronized List<InputInformation> stopAndTakeEvents(int floorNum, ElevatorDirection ElevatorDirection) throws InterruptedException {
		List<InputInformation> takenEvents = this.eventQueue.stream().filter(event -> event.getElevatorDirection() == ElevatorDirection && event.getFloorSource() == floorNum + (ElevatorDirection == ElevatorDirection.Up ? 1 : -1)).collect(Collectors.toList());
		this.eventQueue.removeAll(takenEvents);
		
		//wake up any of the floor threads that may be sleeping since now some events have been taken
		notifyAll();
		return takenEvents;
	}
	
	/**
	 * This method is called by an elevator to know what is the highest destination in a specific ElevatorDirection
	 * @param ElevatorDirection The ElevatorDirection in which the elevator is going towards
	 * @return A integer representing the highest floor target destination
	 */
	public synchronized int getHighestFloorTargetDestination(ElevatorDirection ElevatorDirection) {
		int highestDestination = 0;
		for (InputInformation event: this.eventQueue) {
			if ((event.getFloorDestination() > highestDestination) && event.getElevatorDirection() == ElevatorDirection)
				highestDestination = event.getFloorDestination();
			else if ((event.getFloorDestination() > highestDestination) && event.getElevatorDirection() != ElevatorDirection) {
				highestDestination = event.getFloorSource();
			}
			
		}
		return highestDestination;
	}
	
	/**
	 * This is method called by an elevator to know what is the lowest destination in a specific ElevatorDirection
	 * @param ElevatorDirection The ElevatorDirection in which the elevator is going towards
	 * @return A integer representing the lowest floor target destination
	 */
	public synchronized int getLowestFloorTargetDestination(ElevatorDirection ElevatorDirection) {
		int lowestDestination = Configuration.FLOORS_IN_BUILDING;
		for (InputInformation event: this.eventQueue) {
			if ((event.getFloorDestination() < lowestDestination) && event.getElevatorDirection() == ElevatorDirection)
				lowestDestination = event.getFloorDestination();
		}
	
		return lowestDestination;
	}
	
	/**
	 * This is called by an elevator to remove the first element in the queue and return it to the elevator
	 * @return A input event which is the first element in the queue
	 */
    public synchronized InputInformation removeFirstEvent() {
        InputInformation event = this.eventQueue.pop();
        notifyAll();
        return event;
    }
	
	/**
	 * This is called by an elevator to remove a taken element in the queue that matches the passed floor number and returns it to the elevator
	 * @return A input event
	 */
	public synchronized InputInformation removeEvent(int floorNum) {
		InputInformation event = null;// this.eventQueue.pop();
		for (InputInformation e: eventQueue) {
			if (e.isElevatorTaken() && e.getFloorSource()==floorNum) {
				event = e;
				this.eventQueue.remove(e);
			}
		}
		notifyAll();
		return event;
	}
	
	public static void main(String[] args) throws SocketException {
		Scheduler scheduler = new Scheduler();
		FloorCommunicator floorCommunicator = new FloorCommunicator(scheduler);
		ElevatorCommunicator elevatorCommunicator = new ElevatorCommunicator(scheduler);
		floorCommunicator.start();
		elevatorCommunicator.start();
	}
}
