package elevatorSubsystem;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import dataSystems.Configuration;;

@SuppressWarnings("serial")
public class ElevatorGUI extends JFrame{
	
		private JTextArea ta[];
		private JTextArea floor[];
		
		
		public ElevatorGUI() {
			super("Elevators");
			System.out.println("Text Area Created!");
			ta = new JTextArea[(Configuration.ELEVATORS_IN_BUILDING+1)];
			
		    floor = new JTextArea[(Configuration.ELEVATORS_IN_BUILDING)];
		    
		    Box box = Box.createVerticalBox();
		    
		    ta[0] = new JTextArea(5,40);
		    ta[0].setEditable(false);
	    	
		    
		    JScrollPane pane =
			        new JScrollPane(ta[0], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    pane.setBorder(BorderFactory.createTitledBorder("Measurements"));
			    box.add(pane);
		    for(int i = 0; i < Configuration.ELEVATORS_IN_BUILDING; i++) {
		    	ta[i+1] = new JTextArea(5,40);
			    ta[i+1].setEditable(false);
			    
			    floor[i] = new JTextArea(5,20);
			    floor[i].setEditable(false);
			    
			    
			    JPanel panel = new JPanel();
			    JPanel panel2 = new JPanel();
			    panel2.add(floor[i]);
			    panel2.setBorder(BorderFactory.createTitledBorder("Elevator " + (i+1) + " Current Floor position"));
			       
		        panel.setBackground(Color.gray); 	    	
		        		        
			    JScrollPane pane1 =
			        new JScrollPane(ta[i+1], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    pane1.setBorder(BorderFactory.createTitledBorder("Elevator " + (i+1)));
			    
			    panel.add(pane1);
			    panel.add(panel2);
			    box.add(panel);
		    }
		    
		    getContentPane().add(box);


		    
		}
		
		public JTextArea getArea(int i) {
			return ta[i];
		}
		
		public JTextArea getFloor(int i) {
			return floor[i];
		}
		
		public JTextArea getMeasurement() {
			return ta[0];
		}

}
