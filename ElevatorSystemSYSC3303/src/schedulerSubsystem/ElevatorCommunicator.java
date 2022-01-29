package schedulerSubsystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import dataSystems.Configuration;
import dataSystems.InputInformation;
import dataSystems.MessageParser;

/**
 * This thread continuously accepts requests from elevators For each new
 * request, a sub thread is spawned that conducts the appropriate operation
 * within the Scheduler
 */
public class ElevatorCommunicator extends Thread {
	private DatagramSocket receiveSocket;
	private DatagramPacket receivePacket;
	private Scheduler scheduler;
	private boolean running;

	/**
	 * Creates a thread that deals with elevator requests to the scheduler
	 * 
	 * @param scheduler The scheduler being used in the system
	 * @throws SocketException
	 */
	public ElevatorCommunicator(Scheduler scheduler) throws SocketException {
		this.receiveSocket = new DatagramSocket(Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
		this.scheduler = scheduler;
		this.running = true;
	}

	/**
	 * Continuously accepts new requests on a DatagramSocket
	 */
	public void run() {
		while (running) {
			byte[] elevatorData = new byte[1024];
			this.receivePacket = new DatagramPacket(elevatorData, elevatorData.length);
			System.out.println("From Scheduler: Waiting to accept requests from elevators on port "
					+ Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);

			try {
				this.receiveSocket.receive(receivePacket);
				String request = new String(elevatorData);
				MessageParser parsed = new MessageParser(request);
				//checks if the message contains the extra INCOMPLETE events from the elevator with the serious error
				if (parsed.messageType == null && parsed.floorNum == -1 && parsed.direction == null) {
					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(elevatorData));
					InputInformation parsedEvent = (InputInformation) iStream.readObject();
					iStream.close();
					this.scheduler.acceptEvent(parsedEvent);//if true send the event to scheduler
					System.out.println("From Scheduler: Received the remaining events from elevator with SERIOUS ERROR");
				}
				System.out.println("From Scheduler: successfully parsed request from elevator with address "
						+ receivePacket.getAddress() + ", port " + receivePacket.getPort());
				ElevatorSubThread subThread = new ElevatorSubThread(parsed, receivePacket.getAddress(),
						receivePacket.getPort(), this.scheduler);
				subThread.start();
			} catch (ClassNotFoundException | InterruptedException | IOException e) {
				System.out.println("From Scheduler: Failed to parse request from elevator, " + e.getMessage());

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Closes the socket and stops running the thread
	 */
	public void shutDown() {
		this.running = false;
		this.receiveSocket.close();
	}
}
