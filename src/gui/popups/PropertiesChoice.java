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
import javax.swing.JPanel;
import javax.swing.JTextField;

import csm.statetree.CSMComponent;

public class PropertiesChoice extends PropertiesPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6047361258911293931L;
	
	public PropertiesChoice(WorkPanel wp, CSMComponent component, Point p) {
		super(wp, p, component);
		setSize(new Dimension(300,150));
		errorState = PropertiesPanel.NOERROR;
		validate();
	}
	
	public PropertiesChoice(WorkPanel wp, CSMComponent component, Point p, int error) {
		super(wp, p, component);
		setSize(new Dimension(300,150));
		errorState = error;
		validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok"))
			this.ok();
		if(cmd.equals("cancel"))
			cancel();
	}

	void ok() {
		if(name.getText().equals(component.getName()))
			this.cancel();
		else 
			if(wp.isValidName(name.getText())) {
				component.setName(name.getText());
				wp.changeElementList();
				this.dispose();
			} else {
				new PropertiesChoice(wp, component, this.getLocation(), PropertiesPanel.NAMEERROR);
				this.dispose();
			}
	}
		
	void setWindow() {
		JPanel window = new JPanel();
		window.setLayout(new GridBagLayout());
		JLabel head = new JLabel("Choice State:");
		JLabel namel = new JLabel("Name:");
		JLabel error = new JLabel("The choosen Name already exists!");
		name = new JTextField(component.getName());
		name.setColumns(15);
		
		window.add(head,       new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		if(errorState == PropertiesPanel.NAMEERROR)
			window.add(error,  new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(namel,      new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(name,       new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
				cont.add(window, BorderLayout.CENTER);		
	}
}
