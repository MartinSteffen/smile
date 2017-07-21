package gui.popups;

import gui.WorkPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import csm.statetree.CSMComponent;


public abstract class PropertiesPanel extends JFrame implements ActionListener{

	public static final int NOERROR = 0;
	public static final int NAMEERROR = 1;
	public static final int ACTIONERROR = 2;
	public static final int EVENTERROR = 3;
	protected int errorState;
	
	CSMComponent component;
	JTextField name;
	Container cont;
	WorkPanel wp;
	
	public PropertiesPanel (WorkPanel wp, Point p, CSMComponent component) {
		super("Properties");
		this.wp = wp;
		this.component = component;
		cont = getContentPane();
		cont.setLayout(new BorderLayout());
		setWindow();
		add(new OK_CancelPanel(this), BorderLayout.SOUTH);
		setLocation(p);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	abstract void setWindow();
	abstract void ok();
	
	protected void cancel() {
		dispose();
	}
}
