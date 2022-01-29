package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataSystems.ElevatorDirection;
import dataSystems.MessageParser;
import dataSystems.MessageType;

/**
 * Tests for message parsing class
 */
class TestMessageParser {

	/**
	 * Tests requestWork message type
	 */
	@Test
	void testRequestWork() {
		MessageParser msg = new MessageParser("messageType=requestWork&floorNum=2");
		assertEquals(MessageType.REQUEST_WORK, msg.messageType);
		assertEquals(2, msg.floorNum);
		assertNull(msg.direction);
	}

	/**
	 * Tests checkForMoreEvents message type
	 */
	@Test
	void testCheckForMoreEvents() {
		MessageParser msg = new MessageParser("messageType=checkForMoreEvents&floorNum=2&direction=up");
		assertEquals(MessageType.CHECK_FOR_MORE_EVENTS, msg.messageType);
		assertEquals(2, msg.floorNum);
		assertEquals(ElevatorDirection.Up, msg.direction);
	}
	
	/**
	 * Tests stopAndTakeEvents message type
	 */
	@Test
	void testStopAndTakeEvents() {
		MessageParser msg = new MessageParser("messageType=stopAndTakeEvents&floorNum=2&direction=down");
		assertEquals(MessageType.STOP_AND_TAKE_EVENTS, msg.messageType);
		assertEquals(2, msg.floorNum);
		assertEquals(ElevatorDirection.Down, msg.direction);
	}
	
	/**
	 * Tests getHighestFloorTargetDestination message type
	 */
	@Test
	void testGetHighestFloorTargetDestination() {
		MessageParser msg = new MessageParser("messageType=getHighestFloorTargetDestination&direction=stationary");
		assertEquals(MessageType.GET_HIGHEST_FLOOR_TARGET_DESTINATION, msg.messageType);
		assertEquals(-1, msg.floorNum);
		assertEquals(ElevatorDirection.Stop, msg.direction);
	}
	
	/**
	 * Tests getLowestFloorTargetDestination message type
	 */
	@Test
	void testGetLowestFloorTargetDestination() {
		MessageParser msg = new MessageParser("messageType=getLowestFloorTargetDestination&direction=stationary");
		assertEquals(MessageType.GET_LOWEST_FLOOR_TARGET_DESTINATION, msg.messageType);
		assertEquals(-1, msg.floorNum);
		assertEquals(ElevatorDirection.Stop, msg.direction);
	}
	
	/**
	 * Tests removeFirstEvent message type
	 */
	@Test
	void testRemoveFirstEvent() {
		MessageParser msg = new MessageParser("messageType=removeFirstEvent");
		assertEquals(MessageType.REMOVE_FIRST_EVENT, msg.messageType);
		assertEquals(-1, msg.floorNum);
		assertNull(msg.direction);
	}
	
}
