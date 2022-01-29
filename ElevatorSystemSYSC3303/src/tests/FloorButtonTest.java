package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.jupiter.api.Test;

import floorSubsystem.FloorButton;
import floorSubsystem.FloorLamp;
import schedulerSubsystem.ElevatorCommunicator;
import schedulerSubsystem.FloorCommunicator;
import schedulerSubsystem.Scheduler;
import dataSystems.Configuration;
import dataSystems.ElevatorDirection;
import dataSystems.TextFileReader;

class TestFloorButton {

	/**
	 * Tests creating an up floor button
	 */
	@Test
	void testCreateFloorButtonUp() {
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Up));
		assertEquals(ElevatorDirection.Up, button.getButtonDirection());
	}
	
	/**
	 * Tests creating a down button
	 */
	@Test
	void testCreateFloorButtonDown() {
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Down));
		assertEquals(ElevatorDirection.Down, button.getButtonDirection());
	}
	
	@Test
	void testPressFloorButton() throws InterruptedException, IOException {
		DatagramSocket socket = new DatagramSocket();
		DatagramPacket sendPacket;
		Scheduler scheduler = new Scheduler();
		FloorCommunicator floorCommunicator = new FloorCommunicator(scheduler);
		ElevatorCommunicator elevatorCommunicator = new ElevatorCommunicator(scheduler);
		TextFileReader input = new TextFileReader("src/input.txt");
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Up));
		
		floorCommunicator.start();
		elevatorCommunicator.start();
		
		/*
		* This needs to run in an anonymous thread for testing because the button.press()
		* will send a datagramPacket to the FloorCommunicator which will spawn a FloorSubThread
		* which will run the SCHEDULER.ACCPETEVENT() method which WAITS until the event has 
		* been processed by the elevator. This anonymous thread is needed because said wait
		* will block the simulation of the elevator processing the event in this test. 
		*/
		Thread t= new Thread(){ 	
			public void run() {
				try {
					//Simulate a button being pressed (not running a floor thread because 
					// we only want to do this once for testing
					byte[] returnVal = button.press(input.getReadEvents().get(0));
					
					Thread.sleep(1000);
					//Assert that the confirmation message is received by the floor
					assertEquals("Event processed", new String(returnVal).trim());
					System.out.println(new String(returnVal));
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};

		//Starting anonymous thread
		t.start();
		Thread.sleep(3000);
		
		// Remove that event from the scheduler, this is what the elevator would be doing
		String msg = "messageType=removeFirstEvent";
		sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS), Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
		socket.send(sendPacket);
		
		socket.close();
		
		// Stop the threads manually
		// These threads are designed to run forever like a real elevator system,
		// which makes stopping manually necessary
		floorCommunicator.shutDown();
		elevatorCommunicator.shutDown();
	}
	
	@Test
	void testPressErrorFloorButton() throws InterruptedException, IOException {
		DatagramSocket socket = new DatagramSocket();
		DatagramPacket sendPacket;
		Scheduler scheduler = new Scheduler();
		FloorCommunicator floorCommunicator = new FloorCommunicator(scheduler);
		ElevatorCommunicator elevatorCommunicator = new ElevatorCommunicator(scheduler);
		TextFileReader input = new TextFileReader("src/input.txt");
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Up));
		
		floorCommunicator.start();
		elevatorCommunicator.start();
		
		/*
		* This needs to run in an anonymous thread for testing because the button.press()
		* will send a datagramPacket to the FloorCommunicator which will spawn a FloorSubThread
		* which will run the SCHEDULER.ACCPETEVENT() method which WAITS until the event has 
		* been processed by the elevator. This anonymous thread is needed because said wait
		* will block the simulation of the elevator processing the event in this test. 
		*/
		Thread t= new Thread(){ 	
			public void run() {
				try {
					//Simulate a button being pressed (not running a floor thread because 
					// we only want to do this once for testing
					byte[] returnVal = button.pressError(input.getReadEvents().get(0));
					
					Thread.sleep(1000);
					//Assert that the confirmation message is received by the floor
					assertEquals("Event processed", new String(returnVal).trim());
					System.out.println(new String(returnVal));
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};

		//Starting anonymous thread
		t.start();
		Thread.sleep(3000);
		
		// Remove that event from the scheduler, this is what the elevator would be doing
		String msg = "messageType=removeFirstEvent";
		sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS), Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
		socket.send(sendPacket);
		
		socket.close();
		
		// Stop the threads manually
		// These threads are designed to run forever like a real elevator system,
		// which makes stopping manually necessary
		floorCommunicator.shutDown();
		elevatorCommunicator.shutDown();
	}

}
