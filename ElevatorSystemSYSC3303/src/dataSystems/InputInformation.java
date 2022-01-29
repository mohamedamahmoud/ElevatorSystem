package dataSystems;


import java.time.LocalTime;
import java.io.Serializable;

/**
 * SYSC 3303 PROJECT.
 * The interface is used by ErrorEvent and UserEvent
 *  * @author Omar Agamy 101088548
 *
 */
public interface InputInformation extends Serializable{
	LocalTime getTime(); //Required of the method to get the current time
	int getFloorSource();//Required of the method to get which floor was the button pressed
	int getFloorDestination();//Required of the method to get the destination of the elevator
	ElevatorDirection getElevatorDirection();//Required of the method to see if it going up
	boolean isSeriousError();//Required of the method to see if the fault is serious
	boolean isTrivialError();//Required of the method to see if the fault is trivial 
	boolean isElevatorTaken();
	void elevatorTakeEvent();
	String toString();
}
