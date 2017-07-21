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

public class PropertiesCSM extends PropertiesPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6047361258911293931L;
	
	JTextField outerName;
	
	public PropertiesCSM(WorkPanel wp, CSMComponent component, Point p) {
		super(wp, p, wp.getCSM().region);
		setSize(new Dimension(300,150));
		errorState = PropertiesPanel.NOERROR;
		validate();
	}
	
	public PropertiesCSM(WorkPanel wp, CSMComponent component, Point p, int error) {
		super(wp, p, wp.getCSM().region);
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
		if(!name.getText().equals(wp.getCSMName()))
			wp.setCSMName(name.getText());
		if(!outerName.getText().equals(component.getName()))
			component.setName(outerName.getText());
		wp.isChanged = true;
		wp.changeElementList();
		dispose();
	}
		
	void setWindow() {
		JPanel window = new JPanel();
		window.setLayout(new GridBagLayout());
		name = new JTextField(wp.getCSMName());
		outerName = new JTextField(component.getName());
		name.setColumns(15);
		outerName.setColumns(15);
		
		window.add(new JLabel("CoreStateMMachine: "),       new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(name,      new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("OutermostRegion: "),      new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(outerName,       new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		cont.add(window, BorderLayout.CENTER);		
	}
}
