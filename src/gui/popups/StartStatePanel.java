package gui.popups;

import gui.WorkPanel;
import gui.graphicalobjects.CompositeStateGO;
import gui.graphicalobjects.GraphicalObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import csm.exceptions.ErrTreeNotChanged;
import csm.statetree.CompositeState;

public class StartStatePanel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private WorkPanel wp;
	private Container cont;
	JList compositeList;
	LinkedList<String> composites;
	JTextField composite;
	
	public StartStatePanel(WorkPanel wp) {
		super("Start State");
		this.wp = wp;
		
		
//		 setUp Frame
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
		
//		 setUp JList
		composites = new LinkedList<String>();
		 for(GraphicalObject go : wp.getGOs()) {
			 if(go.getType() == GraphicalObject.COMPOSITESTATE && go.getComponent().getParent().equals(wp.getCSM().region))
				 composites.add(go.getName());
		 }
		 compositeList = new JList(composites.toArray());
		 JScrollPane scroller = new JScrollPane(compositeList);
		 scroller.setPreferredSize(new Dimension(200,100));
		 scroller.setMinimumSize(new Dimension(200,100));
		 compositeList.setBackground(Color.WHITE);
		 compositeList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					listSelectionAction();}});
		 

		 if(wp.getCSM().region.getStartState() != null)
			 composite = new JTextField(wp.getCSM().region.getStartState().getName());
		 else
			 composite = new JTextField();
		 composite.setColumns(15);
		 
		 window.add(new JLabel("Choose Start State:"),   new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		 window.add(scroller, new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		 window.add(composite, new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0));
		 
		 validate();
	}
	
	private void listSelectionAction() {
		composite.setText((String)compositeList.getSelectedValue());
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ok")) {
			try {
				for(GraphicalObject go : wp.getGOs())
					if(go.getName().equals(composite.getText())) {
						wp.getCSM().region.setStartState((CompositeState)go.getComponent());
						break;
					}
				wp.isChanged = true;
				wp.repaint();
				dispose();
			} catch (ErrTreeNotChanged e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
}
