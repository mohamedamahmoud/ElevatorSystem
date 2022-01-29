package elevatorSubsystem;

import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import dataSystems.Configuration;

public class startSystem extends JFrame {
	
	private JTextArea colorLegend;
	private JTextArea measurement;
	private JButton startButton;

	public startSystem() {
		startButton= new StartButton();
		Box box = Box.createVerticalBox();
		box.add(new StartButton());
		this.setSize(400, 150);
		this.add(box);
		startButton.setAlignmentX(box.CENTER_ALIGNMENT);
		colorLegend= new JTextArea();
		colorLegend.append("Stop/Waiting State: White\n");
		colorLegend.append("Up State: Green\n");
		colorLegend.append("Down State: Yellow\n");
		colorLegend.append("Error State: light red (if SERIOUS goes to dark red)\n");
		box.add(colorLegend);
		
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			startSystem gui = new startSystem();
			gui.setVisible(true);
		});
	}

}
