# SYSC 3303 Winter 2021 Project - Elevator System Simulator
---

Lab L2, Group 4
* Omar Agamy 101088548
* Mohamed Mahmoud 101040767
* Taher Shabaan 101073767
* Hassan Hassan 101095571
* Ahmed Abdelrazik 101103048

### Included directories/files

* `L3G4_milestone_3` - Eclipse project directory
	* `src/elevatorSubsystem/Elevator.java` - the elevator that gets the information
	* `src/elevatorSubsystem/ElevatorButton.java` - the button that calls the elevator 
	* `src/elevatorSubsystem/ElevatorDoor.java` - the elevator door and its closing and opening functions
	* `src/elevatorSubsystem/ElevatorLamp.java` - elevator lamps that check which floor
	* `src/elevatorSubsystem/ElevatorMotor.java` - the elevator motor that moves the elevator
	* `src/floorSubsystem/FloorButton.java` - the floor button in the floor system
	* `src/floorSubsystem/FloorLamp.java` - the floor lamp in the floor subsystem
	* `src/floorSubsystem/Floor.java` - a floor in the floor subsystem
	* `src/schedulerSubsystem/Scheduler.java` - the scheduler that is used by the system
	* `src/schedulerSubsystem/ElevatorCommunicator.java`-accepts all the requests from the elevator
	* `src/schedulerSubsystem/ElevatorSubThread.java` -excutes all the requests fom the elevator
	* `src/schedulerSubsystem/FloorCommunicator.java` -accept all the requests from the floor
	* `src/schedulerSubsystem/FloorSubThread.java` -excutes all the requests from the floor
	* `src/simulation/Simulation.java` - this class uses two threads to simulate the system
	* `src/tests/ElevatorButtonTest.java` - unit tests for ElevatorButton
	* `src/tests/ElevatorDoorTest.java` - unit tests for ElevatorDoor
	* `src/tests/ElevatorLampTest.java` - unit tests for ElevatorLamp
	* `src/tests/ElevatorMotorTest.java` - unit tests for ElevatorMotor
	* `src/tests/FloorButtonTest.java` - unit tests for FloorButton
	* `src/tests/FloorLampTest.java` - unit tests for FloorLamp
	* `src/tests/FileDataTest.java` - unit tests for FaultEvent, PassengerEvent, InputEvent, and InputReader
	* `src/tests/MessageParserTest.java` - unit tests for MessageParser
	* `src/tests/SchedulerElevatorCommunicationTest.java` -  unit tests that the ElevatorCommunicator and ElevatorSubThread appropriate the UDP requests
	* `src/tests/SchedulerFloorCommunicationTest.java` - tests that the FloorCommunicator and FloorSubThread classes appropriate the UDP requests
	* `src/tests/DataPathTest.java` - unit tests for checking if the program can read the input file and pass the data
	* `src/dataSystems/Configuration.java` - the configuration for the values used in the system
	* `src/dataSystems/ElevatorDirection.java` - the direction of where the elevator is
	* `src/dataSystems/ErrorHandler.java` - how the system handles errors
	* `src/dataSystems/InputInformation.java` - interface used by the error and user handler
	* `src/dataSystems/TextFileReader.java` - reads the file in in the readme
	* `src/dataSystems/UserHandler.java` - the user in the system
	* `src/input.txt` - file inputing the data about the user and the errors
	* `src/user-input.txt` - file inputing only users info
	
* `Diagrams`
	*  Class Diagram - The uml diagram used in iteration 3
	*  Sequence Diagram - The sequence diagram used in iteration 1
	*  Scheduler State Machine Diagram - state machine for the scheduler system for iteration 3
	*  Floor State Machine Diagram- state machine for the floor system for iteration 3
	*  Elevator State Machine Diagram -state machine for the elevator system for iteration 3


* `README.txt`

### Setup Instructions

* Unzip the submitted file and import it into a eclipse (import as an exisiting file).

### Simulation Instructions - Multiple Computers

* This iteration allows the user to run the elevator system's components on different computers. 
* The instructions bellow must be met on the three computers(all computers must be on the same network.)
*First regarding the computer that will be running the Schedular, check the computer's IP address and change the "SCHEDULER_IP_ADDRESS" in src/dataSystems/Configurations.java on all computers with that address.

IMPORTANT BEFORE RUNNING: any changed values in src/dataSystems/Configurations.java must be changed in all the computers

* In order to run the scheduler subsystem run "Scheduler.java" as a Java Application

* In order to run the floor subsystem run "Floor.java" as a Java Application
	
* In order to run the elevator subsytem run "Elevator.java" as a Java Application


### JUnit Instructions

* It is expected that your version of Eclipse supports JUnit.
* JUnit has already been added to the buildpath for the project.
* To find and run the tests:
	* From the Package Explorer menu in Eclipse, expand the `src` directory
	* There should be a `tests` package. Right click it, select "Run As", then select "JUnit Test".
	* This should run all test classes in the tests package.

### Breakdown of Responsibilities

* Iteration 3: (All members worked closely to make the whole system as well but the specifications below are the specialities of each member)
	* Mohamed: Floor Subsystem 
	* Taher: Elevator Subsystem 
	* Ahmed: Unit Tests
	* Hassan: Simulation and Scheduler subsystem
	* Omar: DataSystems
