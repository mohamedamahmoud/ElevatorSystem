package elevatorSubsystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

import javax.swing.JButton;

import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import schedulerSubsystem.Scheduler;

@SuppressWarnings("serial")
public class StartButton extends JButton{
	
	public StartButton() {
		super("Start!");
		super.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        		try {
					Scheduler.main(null);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		try {
					Floor.main(null);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		try {
					Elevator.main(null);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		setVisible(false);
            
			}
        });
	}

}
