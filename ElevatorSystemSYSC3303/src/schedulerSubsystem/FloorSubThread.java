package schedulerSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import dataSystems.InputInformation;

/**
 * This thread is created and used for each new event
 */
public class FloorSubThread extends Thread {
	private InputInformation event;
	private InetAddress sourceAddress;
	private int sourcePort;
	private Scheduler scheduler;
	
	/**
	 * Creates a new FloorSubThread for the new event
	 * @param event The added event
	 * @param sourceAddress The address making the new event request
	 * @param sourcePort The port making the new event request
	 * @param scheduler The scheduler in which the new event is added to
	 */
	public FloorSubThread(InputInformation event, InetAddress sourceAddress, int sourcePort, Scheduler scheduler) {
		this.event = event;
		this.sourceAddress = sourceAddress;
		this.sourcePort = sourcePort;
		this.scheduler = scheduler;
	}
	
	/**
	 * This method calls the scheduler's acceptEvent() method and sends a message to the source floor once the event has been added
	 */
	public void run() {
		try {
			this.scheduler.acceptEvent(event); // Waits until the event has been accepted and dealt with within the scheduler
			
			System.out.println("From Schedular: Event is finished");
			
			String msg = "Event processed";
			
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), this.sourceAddress, this.sourcePort);
			
			System.out.println("From Schedular: Acknowledgement packet to be sent to the floor");
			
			DatagramSocket socket = new DatagramSocket();
			
			socket.send(sendPacket);
			
			System.out.println("From Schedular: Sent acknowledgement packet to floor");
			
			socket.close();
		
		} catch (InterruptedException | IOException e) {
			
			System.out.println("From Schedular: Failure to accept event");
			e.printStackTrace();
		}
	}
}
