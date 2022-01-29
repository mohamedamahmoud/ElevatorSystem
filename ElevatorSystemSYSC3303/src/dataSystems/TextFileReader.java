package dataSystems;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * SYSC 3303 PROJECT.
 * This class reads the input.txt file and adds the inputs to an arrayList
 * @author Omar Agamy 101088548
 *
 */
public class TextFileReader {
	
	private static ArrayList<InputInformation> readEvents;
	
	/*
	 * Returns an ArrayList of input events written in the input.txt file 
	 * by going through the file and checking if the input is a user or an error
	 * */
	public TextFileReader(String filePath) {
		readEvents = new ArrayList<InputInformation>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
			String readLine;
			readLine = fileReader.readLine();
			while (readLine != null) {
				if (readLine.split("\\|")[0].equalsIgnoreCase("User")) {
					UserHandler event = new UserHandler(readLine.split("\\|")[1]);
					readEvents.add(event);
				} else {
					ErrorHandler event = new ErrorHandler(readLine.split("\\|")[1]);
					readEvents.add(event);
				}
				readLine = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Return all events read from the txt file
	 * */
	public ArrayList<InputInformation> getReadEvents() {
		return this.readEvents;
	}
	
	public int getReadEventsSize() {
		return this.readEvents.size();
	}
	
	/*
	 *Get all the events of a certain floor 
	 * */
	public ArrayList<InputInformation> getEventsByFloor(int floor) {
		ArrayList<InputInformation> floorEvents = new ArrayList<InputInformation>();
		for (InputInformation event : this.readEvents) {
			if (event.getFloorSource() == floor) {
				floorEvents.add(event);
			}
		}
		return floorEvents;
	}
	

	/*
	 * Gets all the floors that were used in the input
	 * */
	public Set<Integer> getFloors() {
		Set<Integer> floors = new HashSet<Integer>();
		for (InputInformation event : this.readEvents) {
			floors.add(event.getFloorSource());
		}
		return floors;
	}
	

}
