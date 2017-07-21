package gui.popups;

import gui.WorkPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import csm.Dictionary;
import csm.Event;
import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrTreeNotChanged;

public class EventsPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	WorkPanel wp;
	Dictionary<Event> events;
	Container cont;
	JTextField name, event;
	JList eventList;
	
	public EventsPanel(WorkPanel wp) {
		super("Events");
		// setUp Frame
		cont = getContentPane();
		cont.setLayout(new BorderLayout());
		cont.add(new OKPanel(this), BorderLayout.SOUTH);
		JPanel window = new JPanel();
		cont.add(window);
		window.setLayout(new GridBagLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(400, 400);
		
		this.wp = wp;
		
		// setUp JList
		events = this.wp.getCSM().events;
		
		eventList = new JList(setList(events));
		JScrollPane scroller = new JScrollPane(eventList);
		scroller.setPreferredSize(new Dimension(200,100));
		scroller.setMinimumSize(new Dimension(200,100));
		eventList.setBackground(Color.WHITE);
		
		name = new JTextField();
		event = new JTextField();
		event.setColumns(15);
		
		JButton addEvent = new JButton("Add");	
		addEvent.setActionCommand("add");
		addEvent.addActionListener(this);
		JButton deleteEvent = new JButton("Delete");	
		deleteEvent.setActionCommand("delete");
		deleteEvent.addActionListener(this);	
		
		
		
		window.add(new JLabel("Events"),   new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(scroller, new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(deleteEvent, new GridBagConstraints(2,1,1,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Event:"),   new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(event,       new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(addEvent,    new GridBagConstraints(2,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		validate();
	}
	
	private Vector<String> setList(Dictionary<Event> events) {
		Vector<String> listEntries = new Vector<String>();
		Set<String> keys = events.getKeys();
		for(String key: keys) 
				listEntries.add(key);
		return listEntries;
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok"))
			wp.isChanged = true;
			dispose();
		if(cmd.equals("add")) {
			if(event.getText().equals("")) ;
			else {
				try {
					new Event(events, event.getText());
				} catch (ErrAlreadyDefinedElement e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				} catch (ErrSyntaxError e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				}
				event.setText("");
				eventList.setListData(setList(events));
			}
		}
		if(cmd.equals("delete"))
			try {
				if(eventList.getSelectedIndex() != -1) {
					events.remove((String)eventList.getSelectedValue());
					eventList.setListData(setList(events));
				}
			} catch (ErrTreeNotChanged e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);

			}
	}
}
