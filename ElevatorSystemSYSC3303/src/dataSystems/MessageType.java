package dataSystems;

/*
 * This enum is used to check what type of request will be sent over UDP from the elevator to reach the scheduler  
 * */
public enum MessageType {
	REQUEST_WORK, 
	CHECK_FOR_MORE_EVENTS, 
	STOP_AND_TAKE_EVENTS,
	GET_HIGHEST_FLOOR_TARGET_DESTINATION,
	GET_LOWEST_FLOOR_TARGET_DESTINATION,
	REMOVE_FIRST_EVENT,
	REMOVE_EVENT,
	QUEUED_EVENT
}
