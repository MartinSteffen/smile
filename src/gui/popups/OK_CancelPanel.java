package gui.popups;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OK_CancelPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ActionListener al;
	
	public OK_CancelPanel(ActionListener al) {
		this.al = al;
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		add(makeButton("OK", "ok"));
		add(makeButton("Cancel", "cancel"));
		
	}
	
	private JButton makeButton(String name, String command) {
		JButton button = new JButton(name);
		button.setText(name);
		button.setActionCommand(command);
		button.addActionListener(al);
		return button;
	}
}
