package gui.popups;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import gui.WorkPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrUndefinedElement;
import csm.statetree.CSMComponent;
import csm.statetree.CompositeState;

public class PropertiesComposite extends PropertiesPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6047361258911293931L;
	JTextField action, event;
	String message;
	JList events, allEvents;
	LinkedList<String> eventNames;
	
	public PropertiesComposite(WorkPanel wp, CSMComponent component, Point p) {
		super(wp, p, component);
		setSize(new Dimension(300,300));
		errorState = PropertiesPanel.NOERROR;
		validate();
	}
	
	public PropertiesComposite(WorkPanel wp, CSMComponent component, Point p, int error, String message) {
		super(wp, p, component);
		this.message = message;
		setSize(new Dimension(300,300));
		errorState = error;
		validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok"))
			this.ok();
		if(cmd.equals("cancel"))
			this.cancel();
		if(cmd.equals("add"))
			this.add();
		if(cmd.equals("delete"))
			this.delete();
	}
	
	public void add() {
		if(allEvents.getSelectedIndex() != -1) {
			eventNames.add((String)allEvents.getSelectedValue());
			events.setListData(eventNames.toArray());
		}
	}
	
	public void delete() {
		if(events.getSelectedIndex() != -1) {
			eventNames.remove(events.getSelectedIndex());
			events.setListData(eventNames.toArray());
		}
	}
	
	void ok() {
		if(name.getText().equals(component.getName())) ;
		else {	
			if(wp.isValidName(name.getText())) 
				component.setName(name.getText());	
			else {
				new PropertiesComposite(wp, component, this.getLocation(), PropertiesPanel.NAMEERROR, "Name already choosen! Change Name!");
				this.dispose();				
			}
		}
		try {
			((CompositeState)component).setDoAction(action.getText());
			((CompositeState)component).setDeferredEventNames(eventNames);
		} catch (ErrUndefinedElement e) {
			new PropertiesComposite(wp, component, this.getLocation(), PropertiesPanel.ACTIONERROR, e.getMessage());
			dispose();
		} catch (ErrSyntaxError e) {
			new PropertiesComposite(wp, component, this.getLocation(), PropertiesPanel.ACTIONERROR, e.getMessage());
			dispose();
		}
		wp.changeElementList();
		this.dispose();
	}
	
	void setWindow() {
		JPanel window = new JPanel();
		window.setLayout(new GridBagLayout());
		// setzen der EventListen
		
		allEvents = new JList(wp.getCSM().events.getKeys().toArray());
		
		if(!((CompositeState)component).getDeferredEventNames().isEmpty()) 
			eventNames = ((CompositeState)component).getDeferredEventNames();
		else
			eventNames = new LinkedList<String>();
		events = new JList(eventNames.toArray());
		JScrollPane scroll1 = new JScrollPane(events);
		JScrollPane scroll2 = new JScrollPane(allEvents);
		scroll1.setPreferredSize(new Dimension(100, 100));
		scroll1.setMinimumSize(new Dimension(100, 100));
		scroll2.setPreferredSize(new Dimension(100, 100));
		scroll2.setMinimumSize(new Dimension(100, 100));
		
		// Setzen der Buttons
		JButton addEvent = new JButton("<<");
		addEvent.addActionListener(this);
		addEvent.setActionCommand("add");
		
		JButton delete = new JButton(">>");
		delete.addActionListener(this);
		delete.setActionCommand("delete");

		// setzen der TextFields
		name = new JTextField(((CompositeState)component).getName());
		action = new JTextField((((CompositeState)component).getDoAction().prettyprint()));

		name.setColumns(15);
		action.setColumns(15);

		window.add(new JLabel("Composite State:"),   new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		if(errorState != PropertiesPanel.NOERROR)
			window.add(new JLabel(message),          new GridBagConstraints(0,1,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Name:"),              new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(name,                             new GridBagConstraints(1,2,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("do Action:"),         new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(action,                           new GridBagConstraints(1,3,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Deferred Events:"),   new GridBagConstraints(0,4,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Events:"),            new GridBagConstraints(3,4,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(scroll1,                          new GridBagConstraints(0,5,2,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(addEvent,                         new GridBagConstraints(2,5,1,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(delete,                           new GridBagConstraints(2,6,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(scroll2,                          new GridBagConstraints(3,5,2,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));

		cont.add(window, BorderLayout.CENTER);		
	}
}
