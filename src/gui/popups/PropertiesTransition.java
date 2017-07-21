package gui.popups;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;

import gui.WorkPanel;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrUndefinedElement;
import csm.statetree.CSMComponent;
import csm.statetree.Transition;

public class PropertiesTransition extends PropertiesPanel{
	
	private static final long serialVersionUID = -6047361258911293931L;
	JTextField action, event, guard;
	JList events;
	String message;
	
	public PropertiesTransition(WorkPanel wp, CSMComponent component, Point p) {
		super(wp, p, component);
		setSize(new Dimension(300,400));
		errorState = PropertiesPanel.NOERROR;
		validate();
	}
	
	public PropertiesTransition(WorkPanel wp, CSMComponent component, Point p, int error, String message) {
		super(wp, p, component);
		this.message = message;
		setSize(new Dimension(300,400));
		errorState = error;
		validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok"))
			ok();
		if(cmd.equals("cancel"))
			cancel();
	}

	void ok() {
		if(name.getText().equals(component.getName())) ;
		else 
			if(wp.isValidName(name.getText())) 
				component.setName(name.getText());
			else {
				new PropertiesTransition(wp, component, this.getLocation(), PropertiesPanel.NAMEERROR, "Name already exist! Change Name!");
				this.dispose();
			}
		try {
			((Transition)component).setAction(action.getText());
			((Transition)component).setEvent(event.getText());
			((Transition)component).setGuard(guard.getText());
		} catch (ErrUndefinedElement e) {
			new PropertiesTransition(wp, component, this.getLocation(), PropertiesPanel.NAMEERROR, e.getMessage());
			this.dispose();
		} catch (ErrSyntaxError e) {
			new PropertiesTransition(wp, component, this.getLocation(), PropertiesPanel.NAMEERROR, e.getMessage());
			this.dispose();
		}
		wp.changeElementList();
		this.dispose();
	}
		
	void setWindow() {
		JPanel window = new JPanel();
		window.setLayout(new GridBagLayout());
		
		name = new JTextField(component.getName());
		action = new JTextField(((Transition)component).getAction().prettyprint());
		guard = new JTextField(((Transition)component).getGuard().prettyprint());
		event = new JTextField(((Transition)component).getEventName());
		
		events = new JList(wp.getCSM().events.getKeys().toArray());
		JScrollPane scroll = new JScrollPane(events);
		scroll.setPreferredSize(new Dimension(100,150));
		scroll.setMinimumSize(new Dimension(100,150));
		
		name.setColumns(15);
		action.setColumns(15);
		guard.setColumns(15);
		event.setColumns(15);
		
		window.add(new JLabel("Transition:"),  new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("from " + ((Transition)component).getSource().getName() + " to " + ((Transition)component).getTarget().getName()),  new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		if(errorState != PropertiesPanel.NOERROR)
			window.add(new JLabel(message),  new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Name:"), new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(name,  new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Action:"), new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(action,  new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Guard:"), new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(guard,  new GridBagConstraints(1,5,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Choose Event:"), new GridBagConstraints(0,6,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(scroll, new GridBagConstraints(0,7,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(event, new GridBagConstraints(0,8,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		
		cont.add(window, BorderLayout.CENTER);		
	}
}
