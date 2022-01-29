package dataSystems;

import java.util.HashMap;

public class MessageParser {
	public MessageType messageType = null; 
	public int floorNum = -1;
	public ElevatorDirection direction = null;
	
	/*
	 * Creates a message parser
	 * */
	public MessageParser(String message) {
		message = message.toLowerCase().trim();
		String[] params = message.split("&");
		
		HashMap<String, String> vals = new HashMap<>();
		for (String param : params) {
			String[] paramValPair = param.split("=");
			vals.put(paramValPair[0], paramValPair[1]);
		}
		if (vals.containsKey("messagetype")) {
			switch (vals.get("messagetype")) {
				case "requestwork":
					this.messageType = MessageType.REQUEST_WORK;
					break;
				case "checkformoreevents":
					this.messageType = MessageType.CHECK_FOR_MORE_EVENTS;
					break;
				case "stopandtakeevents":
					this.messageType = MessageType.STOP_AND_TAKE_EVENTS;
					break;
				case "gethighestfloortargetdestination":
					this.messageType = MessageType.GET_HIGHEST_FLOOR_TARGET_DESTINATION;
					break;
				case "getlowestfloortargetdestination":
					this.messageType = MessageType.GET_LOWEST_FLOOR_TARGET_DESTINATION;
					break;
				case "removefirstevent":
					this.messageType = MessageType.REMOVE_FIRST_EVENT;
					break;
				case "removeevent":
					this.messageType = MessageType.REMOVE_EVENT;
					break;
				case "getqueuedevents":
					this.messageType = MessageType.QUEUED_EVENT;
					break;
			}
		}
		if (vals.containsKey("floornum")) {
			this.floorNum = Integer.parseInt(vals.get("floornum"));
		}
		if (vals.containsKey("direction")) {
			switch (vals.get("direction")) {
				case "up":
					this.direction = ElevatorDirection.Up;
					break;
				case "down":
					this.direction = ElevatorDirection.Down;
					break;
				case "stationary":
					this.direction = ElevatorDirection.Stop;
					break;
			}
		}
	}
}
