package floorSubsystem;

import schedulerSubsystem.Scheduler;
import dataSystems.Configuration;
import dataSystems.ElevatorDirection;
import dataSystems.InputInformation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * Represents the FloorButton used by a user to request an elevator
 */
public class FloorButton {
	private FloorLamp lamp;
	private int floorNum;
	private ElevatorDirection direction;
	private DatagramSocket socket;
	private DatagramPacket sendPacket;
	
	public FloorLamp getFloorLamp() {
		return lamp;
	}
	
	/*
	 * Creates a button for the floor
	 * @param: lamp -> The lamp associated to the button
	 * 	       scheduler -> An instant of the Scheduler Class
	 */
	public FloorButton(FloorLamp lamp) {
		this.direction = lamp.getLampDirection();
		this.floorNum = lamp.getFloorNum();
		this.lamp = lamp;
	}

	
	/**
	 * Gets the direction of the button
	 * @return The direction of the button
	 */
	public ElevatorDirection getButtonDirection() {
		return this.direction;
	}
	
	/**
	 * Used when a passenger presses the button, will turn on the floor lamp and send a RPC call to the 
	 * floor communicator which will call the scheduler to handle the event. Will wait for a acknowledgement 
	 * packet 
	 * @param event: The event that will be passed to the scheduler
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	
	public byte[] press(InputInformation event) throws InterruptedException, IOException {
		System.out.println("FLOOR " + floorNum + ": " + (this.direction == ElevatorDirection.Up ? "up " : "down ") + "button has pressed.");
		
		// switch on floor lamp
		this.lamp.turnOn();;
		
		// Serialize event
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(bStream); 
		objectOutput.writeObject(event);
		objectOutput.close();
		byte[] serializedMessage = bStream.toByteArray();
		
		// Send event to the floor communicator so it can deal with the call to the scheduler
		System.out.println("FLOOR sending event to the floor communicator: \n\n" + event.toString());
		sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length, InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS), Configuration.SCHEDULER_FLOOR_COMMUNICATOR_PORT);
		socket = new DatagramSocket();
		socket.send(sendPacket);
		
		// Wait for acknowledgement packet
		System.out.println("FLOOR is waiting for acknoledgement packet from the scheduler");
		byte[] ACK = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(ACK, ACK.length);
		socket.receive(receivePacket); // first receive the "Event processed" message
		System.out.println("FLOOR #" + this.floorNum + " received acknoledgement packet from scheduler: " + new String(receivePacket.getData()));
		socket.close();
		//switch off the lamp
		this.lamp.turnOff();;
		
		return(receivePacket.getData());
	}

	/**
	 * Used when a passenger presses the button,WHEN THERE IS AN ERROR will turn on the floor lamp and send a RPC call to the 
	 * floor communicator which will call the scheduler to handle the event. Will wait for a acknowledgement 
	 * packet 
	 * @param event: The event that will be passed to the scheduler
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	
	public byte[] pressError(InputInformation event) throws InterruptedException, IOException {

		// Serialize event
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(bStream);
		objectOutput.writeObject(event);
		objectOutput.close();
		byte[] serializedMessage = bStream.toByteArray();

		// Send event to the floor communicator so it can deal with the call to the scheduler
		System.out.println("FLOOR sending event to the floor communicator: \n\n" + event.toString());
		sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length, InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS), Configuration.SCHEDULER_FLOOR_COMMUNICATOR_PORT);
		socket = new DatagramSocket();
		socket.send(sendPacket);

		// Wait for acknowledgement packet
		System.out.println("FLOOR is waiting for acknoledgement packet from the scheduler");
		byte[] ACK = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(ACK, ACK.length);
		socket.receive(receivePacket); // first receive the "Event processed" message
		System.out.println("FLOOR #" + this.floorNum + " received acknoledgement packet from scheduler: " + new String(receivePacket.getData()));
		socket.close();

		return(receivePacket.getData());
	}
}
