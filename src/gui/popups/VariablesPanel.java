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
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import csm.Dictionary;
import csm.Variable;
import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;
import csm.exceptions.ErrValueOutOfBounds;

public class VariablesPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	WorkPanel wp;
	Dictionary<Variable> variables;
	Container cont;
	JTextField name, init, min, max;
	JList variList;
	
	public VariablesPanel(WorkPanel wp) {
		super("Variables");
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
		variables = this.wp.getCSM().variables;
		variList = new JList(setList(variables));
		JScrollPane scroller = new JScrollPane(variList);
		scroller.setPreferredSize(new Dimension(200,100));
		scroller.setMinimumSize(new Dimension(200,100));
		variList.setBackground(Color.WHITE);
		variList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				listSelectionAction();}});
		
		name = new JTextField();
		init = new JTextField();
		min = new JTextField();
		max = new JTextField();
		name.setColumns(15);
		init.setColumns(5);
		min.setColumns(5);
		max.setColumns(5);
		
		JButton addEvent = new JButton("Add");	
		addEvent.setActionCommand("add");
		addEvent.addActionListener(this);
		JButton deleteEvent = new JButton("Delete");	
		deleteEvent.setActionCommand("delete");
		deleteEvent.addActionListener(this);	
		JButton editEvent = new JButton("Edit");	
		editEvent.setActionCommand("edit");
		editEvent.addActionListener(this);
		
		
		window.add(new JLabel("Variables"),   new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(scroller, new GridBagConstraints(0,1,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(deleteEvent, new GridBagConstraints(4,1,1,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Name:"),   new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(name,       new GridBagConstraints(1,2,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Min. Value:"),   new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(min,   new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Max Value:"),   new GridBagConstraints(2,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(max,   new GridBagConstraints(3,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(editEvent,    new GridBagConstraints(4,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(new JLabel("Inital Value:"),   new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		window.add(init,   new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
		window.add(addEvent,    new GridBagConstraints(4,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));

		validate();
	}
	
	private Vector<String> setList(Dictionary<Variable> variables) {
		Vector<String> listEntries = new Vector<String>();
		Collection<Variable> collection = variables.getContents();
		for(Variable variable: collection) 
				listEntries.add(variable.getName() + "; Init: " + variable.getInitialValue() + "; Min: " + variable.getMinValue() + "; Max: " + variable.getMaxValue());
		return listEntries;
	}
	
	private void listSelectionAction() {
		int elem = variList.getSelectedIndex();
		if (elem != -1) {
		Collection<Variable> collection = variables.getContents();
		Variable v = (Variable)collection.toArray()[elem];
		name.setText(v.getName());
		min.setText("" + v.getMinValue());
		max.setText("" + v.getMaxValue());
		init.setText("" + v.getInitialValue());
		}
	}
		
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok"))
			wp.isChanged = true;
			dispose();
		if(cmd.equals("delete")) {
			if(name.getText().equals("")) ;
			else {
				try {
					variables.remove(name.getText());
				} catch (ErrTreeNotChanged e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				}
				name.setText("");
				min.setText("");
				max.setText("");
				init.setText("");
				variList.setListData(setList(variables));
			}
		}
		if(cmd.equals("add")) {
			if(name.getText().equals("")) ;
			else {
				try {
					int mi, ma, in;
					if(min.getText().equals(""))
						mi = 0;
					else 
						mi = Integer.parseInt(min.getText());
					if(max.getText().equals(""))
						ma = mi;
					else
						ma = Integer.parseInt(max.getText());
					if(init.getText().equals(""))
						in = mi;
					else 
						in = Integer.parseInt(init.getText());
					new Variable(variables, name.getText(), in, mi, ma);
				} catch (ErrAlreadyDefinedElement e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				} catch (ErrSyntaxError e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				} catch (ErrValueOutOfBounds e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
				}
				name.setText("");
				min.setText("");
				max.setText("");
				init.setText("");
				variList.setListData(setList(variables));
			}
		}
		if(cmd.equals("edit")) {
			if(name.getText().equals("")) ;
			else {
				try {
					if(!min.getText().equals("") && !max.getText().equals("") && !init.getText().equals(""))
						variables.get(name.getText()).setValues(Integer.parseInt(init.getText()), Integer.parseInt(min.getText()), Integer.parseInt(max.getText()));
			} catch (ErrUndefinedElement e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
			} catch (ErrValueOutOfBounds e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
			}
			name.setText("");
			min.setText("");
			max.setText("");
			init.setText("");
			variList.setListData(setList(variables));
			}
		}
	}
}
