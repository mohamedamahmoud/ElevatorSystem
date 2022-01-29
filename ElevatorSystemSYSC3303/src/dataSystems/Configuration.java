package dataSystems;

import java.time.LocalTime;
 
/**
 * All the values that are configurable and going to be used in the elevator simulation
 */
public class Configuration {
	
	public static double SIMULATION_SPEED = 0.02;//speed of the simulation
	public static final LocalTime START_TIME = LocalTime.of(14, 0, 0, 0);//the start time of the elevator system
	public static final String USER_INPUT = "src/user-input.txt";//reading an input file with only users
	public static final String USER_ERROR_INPUT = "src/input.txt";//reading an input file with users and erros
	public static final int FLOORS_IN_BUILDING =22;//the number of floors in the building
	public static final int ELEVATORS_IN_BUILDING= 4; //the number of elevators in the building
	public static final double SPEED_FROM_FLOOR_TO_ANOTHER = 3119 * SIMULATION_SPEED; // the speed from going from floor to floor in milliseconds
	public static final double SPEED_OF_DOOR = 12903 * SIMULATION_SPEED; //the speed of the door opening and closing in milliseconds
	public static final int SCHEDULER_FLOOR_COMMUNICATOR_PORT = 3000;
	public static final int SCHEDULER_ELEVATOR_COMMUNICATOR_PORT = 3001;
	public static final String SCHEDULER_IP_ADDRESS = "127.0.0.1";
	public static TextFileReader fileReader= new TextFileReader(USER_ERROR_INPUT);
	public static final int numberOfEvents=fileReader.getReadEventsSize();
}
