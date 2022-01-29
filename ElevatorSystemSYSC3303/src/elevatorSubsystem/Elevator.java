/**
 * 
 */
package elevatorSubsystem;

import java.net.InetAddress;
import java.net.SocketException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

import dataSystems.Configuration;
import dataSystems.ElevatorDirection;
import dataSystems.InputInformation;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * this class will represent the elevator which gets and receive the information
 * and data from schedule
 * 
 * @author Taher Shabaan 101073767
 */
public class Elevator extends Thread {
	private static int ELEVATOR_NUM = 1;
	private int idOfCar;
	private ElevatorMotor elevatorMotor;
	private int actualCurrentFloor;
	private ElevatorDirection currentState;
	private ElevatorDoor elevatorDoor;
	private DatagramSocket datagramSocket;
	private List<ElevatorButton> listOfButton;
	private List<InputInformation> workToBeDone;
	JTextArea console;
	JTextArea floorPosition;
	JTextArea measurementArea;
	private static int completedEvents = 0;
	private static long startTimeNano;
	private long endTimeNano;
	private long timeElapsedNano;
	private static long startTimeInstant;
	private long endTimeInstant;
	private long timeElapsedInstant;

	/**
	 * the constructor for the Elevator subsystem
	 */
	public Elevator(JTextArea console, JTextArea floorPosition, JTextArea measurementArea) {
		this.idOfCar = ELEVATOR_NUM++;
		this.actualCurrentFloor = 1;
		this.currentState = ElevatorDirection.Stop;
		this.elevatorMotor = new ElevatorMotor();
		this.elevatorDoor = new ElevatorDoor();
		this.workToBeDone = new ArrayList<InputInformation>();

		// adding a elevator button and lamb for each of the floors
		this.listOfButton = new ArrayList<ElevatorButton>();
		for (int i = 1; i <= Configuration.FLOORS_IN_BUILDING; i++)
			this.listOfButton.add(new ElevatorButton(new ElevatorLamp(i, this.idOfCar)));
		this.console = console;
		this.floorPosition = floorPosition;
		this.measurementArea = measurementArea;
		console.append("Elevator number " + idOfCar + " was created\n");
		console.setCaretPosition(console.getText().length());
		floorPosition.setText("");
		floorPosition.append(" " + this.actualCurrentFloor);

	}

	public ElevatorDirection getCurrentState() {
		return this.currentState;
	}

	/**
	 * Receive all the events of an adjacent floor with the same direction as the
	 * direction of the elevator
	 * 
	 * @param direction The direction the elevator is going
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void getEventsOnTheWay(ElevatorDirection direction)
			throws InterruptedException, IOException, ClassNotFoundException {
		DatagramPacket sendThePacket, receiveThePacket;
		datagramSocket = new DatagramSocket();
		String massage;
		if (direction == ElevatorDirection.Up)
			massage = "messageType=stopandtakeevents&direction=up" + "&floorNum=" + this.actualCurrentFloor;
		else
			massage = "messageType=stopandtakeevents&direction=down" + "&floorNum=" + this.actualCurrentFloor;
		sendThePacket = new DatagramPacket(massage.getBytes(), massage.length(),
				InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
				Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
		// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
		// scheduler");
		datagramSocket.send(sendThePacket);
		byte[] schedulerData = new byte[1024];
		receiveThePacket = new DatagramPacket(schedulerData, schedulerData.length);
		// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
		// scheduler");
		datagramSocket.receive(receiveThePacket);
		ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(schedulerData));
		List<InputInformation> eventParsed = (List<InputInformation>) iStream.readObject();
		iStream.close();

		List<InputInformation> moreEvents = eventParsed;

		workToBeDone.addAll(moreEvents);
		datagramSocket.close();
	}

	/**
	 * sendWorkToScheduler checks the event and checks if any extra events are to be
	 * done in the Elevator with serious error and sends those events to the
	 * scheduler again
	 */
	public void sendWorkToScheduler(List<InputInformation> workToBeDone) {
		DatagramSocket socket;
		DatagramPacket sendPacket;

		for (InputInformation event : workToBeDone) {
			if (!event.isSeriousError()) {
				ByteArrayOutputStream bStream = new ByteArrayOutputStream();
				try {
					ObjectOutput objectOutput = new ObjectOutputStream(bStream);
					objectOutput.writeObject(event);
					objectOutput.close();
					byte[] serializedMessage = bStream.toByteArray();
					sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length,
							InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
							Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
					socket = new DatagramSocket();
					socket.send(sendPacket);
				} catch (IOException e) {

				}
			}
		}

	}

	/**
	 * Gets all the events of an adjacent floor with the same direction as the
	 * direction of the elevator and checks if the event is either a serious or a
	 * trivial error and does the corresponding open and close door implementation
	 * 
	 * @throws InterruptedException
	 */
	public void checkCompletedEvents() throws InterruptedException {
		List<InputInformation> processTheEvents = new ArrayList<InputInformation>();
		processTheEvents.addAll(workToBeDone);
		for (InputInformation event : processTheEvents) {
			if (event.getFloorDestination() == this.actualCurrentFloor) {

				// Open and close the door of the elevator
				if (event.isTrivialError()) { // trivial error
					this.elevatorDoor.openAndCloseDoorTrivialError(this.idOfCar, console);
				} else if (event.isSeriousError()) {// serious error
					completedEvents++; // incrementing the static variable to match how many events have been done
					this.elevatorDoor.openAndCloseDoorSeriousError(this.idOfCar, console);
					if (this.workToBeDone.size() <= 1) {
						System.out.println("Elevator #" + this.idOfCar + " No extra work to be sent to the scheduler");
						this.console
								.append("Elevator #" + this.idOfCar + " No extra work to be sent to the scheduler\n");
						console.setCaretPosition(console.getText().length());
						floorPosition.setText("");
						floorPosition.append(" " + this.actualCurrentFloor);
					} else {
						sendWorkToScheduler(this.workToBeDone);
					}
					Thread.currentThread().stop(); // killing the thread
				} else {
					this.elevatorDoor.openAndCloseDoor(this.idOfCar, console);
				}

				// turn off the elevator lamp
				this.listOfButton.get(this.actualCurrentFloor - 1).getElevatorLamp().turnOff(console);

				// delete and remove the event
				workToBeDone.remove(event);

				System.out.println("ELEVATOR #" + this.idOfCar + ": The elevator has completed the following event: "
						+ event.toString());
				this.console.append("ELEVATOR #" + this.idOfCar + ": The elevator has completed the following event: "
						+ event.toString() + "\n");
				completedEvents++; // incrementing the static variable to match how many events have been done
				//this.measurementArea.append(Integer.toString(completedEvents)+"\n");
				if (completedEvents == Configuration.numberOfEvents) {
					endTimeNano = System.nanoTime();
					endTimeInstant = Instant.now().toEpochMilli();
					timeElapsedNano = (endTimeNano - startTimeNano);
					timeElapsedInstant = (endTimeInstant - startTimeInstant);
					this.measurementArea.append(
							"The time taken to complete all the input events is " + timeElapsedNano + " nanoseconds. (using nanoTime)\n");
					this.measurementArea.append("The time taken to complete all the input events is " + timeElapsedInstant
							+ " milliseconds. (using Instant.now)");
				}

				console.setCaretPosition(console.getText().length());
				floorPosition.setText("");
				floorPosition.append(" " + this.actualCurrentFloor);
			}
		}

	}

	public void run() {
		DatagramPacket sendPacket, receivePacket;
		int minFloorDestination = 0;
		int maxFloorDestination = 0;

		try {
			while (true) {
				if (ElevatorDirection.Stop == this.currentState) {
					datagramSocket = new DatagramSocket();
					String massage = "messageType=requestWork&floorNum=" + actualCurrentFloor;
					sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
							InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
							Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
					datagramSocket.send(sendPacket);
					byte[] schedulerData = new byte[1024];
					receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
					// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
					// scheduler");
					datagramSocket.receive(receivePacket);
					this.currentState = ElevatorDirection.valueOf(new String(receivePacket.getData()).trim());
					datagramSocket.close();
				}

				// if the state goes from Stop to Stop, then that means that the elevator is
				// currently
				// at the same floor as the the floor the request was made from
				if (this.currentState == ElevatorDirection.Stop) {
					// if thats the case remove the first element from the schedules queue and add
					// it to list of events to be completed
					datagramSocket = new DatagramSocket();
					String massage = "messageType=removeEvent&floorNum=" + actualCurrentFloor;
					sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
							InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
							Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
					// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
					// scheduler");
					datagramSocket.send(sendPacket);

					byte[] schedulerData = new byte[1024];
					receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
					// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
					// scheduler");
					datagramSocket.receive(receivePacket);
					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(schedulerData));
					InputInformation parsedEvent = (InputInformation) iStream.readObject();
					iStream.close();
					InputInformation eventToAdd = parsedEvent;

					datagramSocket.close();

					if (eventToAdd != null) {
						this.workToBeDone.add(eventToAdd);
						this.currentState = eventToAdd.getElevatorDirection();
						maxFloorDestination = eventToAdd.getFloorDestination();
						minFloorDestination = eventToAdd.getFloorDestination();

						// Open and close the door of the elevator
						this.elevatorDoor.openAndCloseDoor(this.idOfCar, console);
						System.out.println("ELEVATOR #" + this.idOfCar + ": The elevator now has the following events: "
								+ this.workToBeDone.toString());
						this.console
								.append("ELEVATOR #" + this.idOfCar + ": The elevator now has the following events: "
										+ this.workToBeDone.toString() + "\n");
						console.setCaretPosition(console.getText().length());
						floorPosition.setText("");
						floorPosition.append(" " + this.actualCurrentFloor);
						this.listOfButton.get(this.actualCurrentFloor - 1).press(console);

					}
				}

				// Travels upwards and checks on each level if there is any additional requests
				// that it could take on
				if (ElevatorDirection.Up == this.currentState) {
					datagramSocket = new DatagramSocket();
					String massage = "messageType=getHighestFloorTargetDestination&direction=up";
					sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
							InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
							Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
					// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
					// scheduler");
					datagramSocket.send(sendPacket);
					byte[] dataOfSchedular = new byte[1024];
					receivePacket = new DatagramPacket(dataOfSchedular, dataOfSchedular.length);
					// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
					// scheduler");
					datagramSocket.receive(receivePacket);
					int floorMax = Integer.valueOf(new String(receivePacket.getData()).trim());
					datagramSocket.close();
					if (floorMax != 0) {
						maxFloorDestination = floorMax;
					}
					while (this.actualCurrentFloor <= maxFloorDestination) {
						datagramSocket = new DatagramSocket();
						massage = "messageType=getHighestFloorTargetDestination&direction=up";
						sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
								InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
								Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
						// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
						// scheduler");
						datagramSocket.send(sendPacket);
						dataOfSchedular = new byte[1024];
						receivePacket = new DatagramPacket(dataOfSchedular, dataOfSchedular.length);
						// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
						// scheduler");
						datagramSocket.receive(receivePacket);
						int highest = Integer.valueOf(new String(receivePacket.getData()).trim());
						maxFloorDestination = (highest > maxFloorDestination ? highest : maxFloorDestination);
						checkCompletedEvents();

						if (this.actualCurrentFloor == maxFloorDestination)
							break;
						massage = "messageType=checkForMoreEvents&floorNum=" + actualCurrentFloor + "&direction=up";
						sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
								InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
								Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
						// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
						// scheduler");
						datagramSocket.send(sendPacket);
						dataOfSchedular = new byte[1024];
						receivePacket = new DatagramPacket(dataOfSchedular, dataOfSchedular.length);
						// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
						// scheduler");
						datagramSocket.receive(receivePacket);
						boolean moreEvents = Boolean.valueOf(new String(receivePacket.getData()).trim());
						if (moreEvents) {
							try {
								getEventsOnTheWay(currentState);
							} catch (ClassNotFoundException e) {
							} catch (IOException e) {
							}

							// ascends the floor, waits based on calculations from iteration 0
							this.elevatorMotor.ascendFloor(this.actualCurrentFloor, this.idOfCar, console);

							// Open and close the door of the elevator
							this.elevatorDoor.openAndCloseDoor(this.idOfCar, console);

							System.out.println("ELEVATOR #" + this.idOfCar
									+ ": The elevator now has the following events: " + this.workToBeDone.toString());
							this.console.append(
									"ELEVATOR #" + this.idOfCar + ": The elevator now has the following events: "
											+ this.workToBeDone.toString() + "\n");
							console.setCaretPosition(console.getText().length());
							floorPosition.setText("");
							floorPosition.append(" " + this.actualCurrentFloor);
							this.listOfButton.get(this.actualCurrentFloor - 1).press(console);

						} else {
							// ascends the floor, waits based on calculations from iteration 0
							try {
								this.elevatorMotor.ascendFloor(this.actualCurrentFloor, this.idOfCar, console);

							} catch (Exception e) {
							}

						}
						this.actualCurrentFloor++;
						datagramSocket.close();
					}
				}

				// Travels downwards and checks on each level if there is any additional
				// requests that it could take on
				else if (ElevatorDirection.Down == this.currentState) {
					// minFloorDestination =
					// this.scheduler.getLowestFloorTargetDestination(currentState) ==
					// Config.NUM_FLOORS_IN_BUILDING ? minFloorDestination :
					// this.scheduler.getLowestFloorTargetDestination(currentState);
					datagramSocket = new DatagramSocket();
					String massage = "messageType=getqueuedevents";
					sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
							InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
							Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
					// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
					// scheduler");
					datagramSocket.send(sendPacket);
					byte[] schedulerData = new byte[1024];
					receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
					// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
					// scheduler");
					datagramSocket.receive(receivePacket);
					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(schedulerData));
					LinkedList<InputInformation> parsedEvents = (LinkedList<InputInformation>) iStream.readObject();
					datagramSocket.close();
					if (!parsedEvents.isEmpty())
						minFloorDestination = parsedEvents.getFirst().getFloorSource();
					while (this.actualCurrentFloor >= minFloorDestination) {
						datagramSocket = new DatagramSocket();
						massage = "messageType=getLowestFloorTargetDestination&direction=down";
						sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
								InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
								Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
						// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
						// scheduler");
						datagramSocket.send(sendPacket);
						schedulerData = new byte[1024];
						receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
						// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
						// scheduler");
						datagramSocket.receive(receivePacket);
						int lowest = Integer.valueOf(new String(receivePacket.getData()).trim());
						minFloorDestination = (lowest < minFloorDestination ? lowest : minFloorDestination);

						checkCompletedEvents();

						if (this.actualCurrentFloor == minFloorDestination)
							break;

						massage = "messageType=checkForMoreEvents&floorNum=" + actualCurrentFloor + "&direction=down";
						sendPacket = new DatagramPacket(massage.getBytes(), massage.length(),
								InetAddress.getByName(Configuration.SCHEDULER_IP_ADDRESS),
								Configuration.SCHEDULER_ELEVATOR_COMMUNICATOR_PORT);
						// System.out.println("ELEVATOR #" + this.carID + ": sending request packet to
						// scheduler");
						datagramSocket.send(sendPacket);
						schedulerData = new byte[1024];
						receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
						// System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from
						// scheduler");
						datagramSocket.receive(receivePacket);
						boolean moreEvents = Boolean.valueOf(new String(receivePacket.getData()).trim());
						if (moreEvents) {
							try {
								getEventsOnTheWay(currentState);
							} catch (ClassNotFoundException e) {
							} catch (IOException e) {
							}

							// descends the floor, waits based on calculations from iteration 0
							this.elevatorMotor.descendFloor(this.actualCurrentFloor, this.idOfCar, console);

							// Open and close the door of the elevator
							this.elevatorDoor.openAndCloseDoor(this.idOfCar, console);

							System.out.println("ELEVATOR #" + this.idOfCar
									+ ": The elevator now has the following events: " + this.workToBeDone.toString());
							this.console.append(
									"ELEVATOR #" + this.idOfCar + ": The elevator now has the following events: "
											+ this.workToBeDone.toString() + "\n");
							console.setCaretPosition(console.getText().length());
							floorPosition.setText("");
							floorPosition.append(" " + this.actualCurrentFloor);
							this.listOfButton.get(this.actualCurrentFloor - 1).press(console);

						} else {
							// descends the floor, waits based on calculations from iteration 0
							this.elevatorMotor.descendFloor(this.actualCurrentFloor, this.idOfCar, console);
						}

						this.actualCurrentFloor--;
						datagramSocket.close();
					}
				}

				// now that all work is done, go back to being Stop
				this.currentState = ElevatorDirection.Stop;
			}
		} catch (InterruptedException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SocketException {
		// Find all of the floors
		ArrayList<Elevator> elevatorArrayList = new ArrayList<Elevator>();
		ElevatorGUI gui = new ElevatorGUI();
		gui = new ElevatorGUI();
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		gui.pack();
		gui.setVisible(true);
		for (int j = 0; j < Configuration.ELEVATORS_IN_BUILDING; j++) {
			elevatorArrayList.add(new Elevator(gui.getArea(j + 1), gui.getFloor(j), gui.getMeasurement()));
		}
		startTimeNano = System.nanoTime();
		startTimeInstant = Instant.now().toEpochMilli();
		// Start all the elevator threads
		for (Elevator elevator : elevatorArrayList) {
			elevator.start();
		}
	}
}
