package schedulerSubsystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import dataSystems.Configuration;
import dataSystems.InputInformation;

/**
 * This thread is created to continuously wait to accept new events from floors
 * For each new event, a sub thread is created that waits until
 * the event has been removed from the scheduler's queue
 */
public class FloorCommunicator extends Thread {
	private DatagramSocket receivingSocket;
	private DatagramPacket receivingPacket;
	private Scheduler scheduler;
	private boolean isrunning;

	/**
	 * Creates a thread that deals with floor requests to the scheduler
	 * @param scheduler The scheduler being used in the system
	 * @throws SocketException
	 */
	public FloorCommunicator(Scheduler scheduler) throws SocketException {
		this.receivingSocket = new DatagramSocket(Configuration.SCHEDULER_FLOOR_COMMUNICATOR_PORT);
		this.scheduler = scheduler;
		this.isrunning = true;
	}
	
	/**
	 * Continuously retrieves new events on a DatagramSocket
	 */
	public void run() {
		while (isrunning) {
			byte[] floorData = new byte[1024];
			this.receivingPacket = new DatagramPacket(floorData, floorData.length);		
			System.out.println("From Scheduler: Waiting to accept events from floors on port " + Configuration.SCHEDULER_FLOOR_COMMUNICATOR_PORT);
			
			try {
				this.receivingSocket.receive(receivingPacket);
				
				ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(floorData));
				InputInformation parsedEvent = (InputInformation)iStream.readObject();
				
				iStream.close();

				System.out.println("From Scheduler: Successfully parsed event from floor with address " + receivingPacket.getAddress() + ", port " + receivingPacket.getPort());
				
				FloorSubThread subThread = new FloorSubThread(parsedEvent, receivingPacket.getAddress(), receivingPacket.getPort(), this.scheduler);
				
				subThread.start();
				
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("From Scheduler: Failed to parse event from floor, " + e.getMessage());
			}
		}
	}
	
	/**
	 * Closes the socket and stops running the thread
	 */
	public void shutDown() {
		this.isrunning = false;
		this.receivingSocket.close();
	}
}
