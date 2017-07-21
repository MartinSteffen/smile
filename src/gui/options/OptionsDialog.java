package gui.options;

// Grafischer Dialog zur Bearbeitung der Optionen.

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class OptionsDialog implements ItemListener {

	JFrame optionFrame;
	JTextField undo, composite, entry, exit, finalS, choice;
	JCheckBox chB, coB, enB, exB, fiB, tB;
	boolean chL, coL, enL, exL, fiL, tL;
	Options ops;
	
	// Konstruktor. Erzeugen des Frames
	public OptionsDialog(Options options) {
		ops = options;
		
		// Frame und Container setzen
		optionFrame = new JFrame("Options");
		Container cont = optionFrame.getContentPane();
		GridBagLayout g = new GridBagLayout();
		cont.setLayout(new BorderLayout());

		// statePanel für Optionen der States einrichten
		JPanel statePanel = new JPanel();
		statePanel.setLayout(g);
		// Label
		JLabel head1 = new JLabel("How big?");
		JLabel head2 = new JLabel("Wanna see some names?");
		JLabel label1 = new JLabel("Composite-State    Size:");
		JLabel label2 = new JLabel("Entry-State        Size:");
		JLabel label3 = new JLabel("Exit-State         Size:");
		JLabel label4 = new JLabel("Final-State        Size:");
		JLabel label5 = new JLabel("Choice-State       Size:");
		JLabel label6 = new JLabel("Transition");
		// TextFields
		composite = new JTextField();
		composite.setText("" + ops.compositeSize);
		composite.setColumns(5);
		entry = new JTextField();
		entry.setText("" + ops.entrySize);
		entry.setColumns(5);
		exit = new JTextField();
		exit.setText("" + ops.exitSize);
		exit.setColumns(5);
		finalS = new JTextField();
		finalS.setText("" + ops.finalSize);
		finalS.setColumns(5);
		choice = new JTextField();
		choice.setText("" + ops.choiceSize);
		choice.setColumns(5);
		// CheckBoxes einrichten
		chL = ops.chL;
		coL = ops.coL;
		enL = ops.enL;
		exL = ops.exL;
		fiL = ops.fiL;
		tL = ops.tL;
		chB = new JCheckBox("Label", chL);
		coB = new JCheckBox("Label", coL);
		enB = new JCheckBox("Label", enL);
		exB = new JCheckBox("Label", exL);
		fiB = new JCheckBox("Label", fiL);
		tB = new JCheckBox("Label", tL);
		chB.addItemListener(this);
		coB.addItemListener(this);
		enB.addItemListener(this);
		exB.addItemListener(this);
		fiB.addItemListener(this);
		tB.addItemListener(this);
		
		// Objekte in den Panel einfügen
		addObjct(statePanel, head1, g,     0, 0, 1, 0, 0);
		addObjct(statePanel, head2, g,     2, 0, 1, 0, 0);
		addObjct(statePanel, label5, g,    0, 1, 1, 0, 0);
		addObjct(statePanel, choice, g,    1, 1, 1, 0, 0);
		addObjct(statePanel, label1, g,    0, 2, 1, 0, 0);
		addObjct(statePanel, composite, g, 1, 2, 1, 0, 0);
		addObjct(statePanel, label2, g,    0, 3, 1, 0, 0);
		addObjct(statePanel, entry, g,     1, 3, 1, 0, 0);
		addObjct(statePanel, label3, g,    0, 4, 1, 0, 0);
		addObjct(statePanel, exit, g,      1, 4, 1, 0, 0);
		addObjct(statePanel, label4, g,    0, 5, 1, 0, 0);
		addObjct(statePanel, finalS, g,    1, 5, 1, 0, 0);
		addObjct(statePanel, label6, g,    0, 6, 1, 0, 0);
		addObjct(statePanel, chB, g,       2, 1, 1, 0, 0);
		addObjct(statePanel, coB, g,       2, 2, 1, 0, 0);
		addObjct(statePanel, enB, g,       2, 3, 1, 0, 0);
		addObjct(statePanel, exB, g,       2, 4, 1, 0, 0);
		addObjct(statePanel, fiB, g,       2, 5, 1, 0, 0);
		addObjct(statePanel, tB, g,        2, 6, 1, 0, 0);
		
		// Panel für Undo Optionen
		JPanel undoPanel = new JPanel();
		undoPanel.setLayout(g);
		JLabel label7 = new JLabel("Depth of the Undo-Stack:");
		label7.setToolTipText("Set Depth. Default is -1 for unlimited.");
		undo = new JTextField();
		undo.setText("" + ops.undoDepth);
		undo.setColumns(5);
		undo.setToolTipText("Set Depth. Default is -1 for unlimited.");
		addObjct(undoPanel, label7, g, 0, 0, 1, 0, 0);
		addObjct(undoPanel, undo, g, 1, 0, 1, 0, 0);
		
		// Einrichten der Buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton standard = new JButton("Default");
		standard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefault();
			}
		});
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
				optionFrame.dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionFrame.dispose();
			}
		});
		
		buttonPanel.add(standard);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		// Erstellen des Frame
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("States", statePanel);
		tp.addTab("Undo", undoPanel);
		
		cont.add(tp, BorderLayout.CENTER);
		cont.add(buttonPanel, BorderLayout.SOUTH);
		optionFrame.setSize(new Dimension(400, 400));
		optionFrame.setVisible(true);
		optionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	// Methode zum Einfügen und Ausrichten von JComponenten in einem Container
	private void addObjct(Container co, JComponent jc, GridBagLayout gbl,
			int gridx, int gridy, int gridwidth, double weightx, double weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = 1;
		c.weightx = weightx;
		c.weighty = weighty;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 0, 0);
		gbl.setConstraints(jc, c);
		co.add(jc);
	}

	// Default Aktion
	private void setDefault() {
		ops.setDefault();
		composite.setText("" + ops.compositeSize);
		entry.setText("" + ops.entrySize);
		exit.setText("" + ops.exitSize);
		finalS.setText("" + ops.finalSize);
		choice.setText("" + ops.choiceSize);
		undo.setText("" + ops.undoDepth);
		chB.setText(""+ops.chL);
		coB.setText(""+ops.coL);
		enB.setText(""+ops.enL);
		exB.setText(""+ops.exL);
		fiB.setText(""+ops.fiL);
		tB.setText(""+ops.tL);
	}
	
	// OK Aktion
	private void okAction() {
		ops.undoDepth = Integer.parseInt(undo.getText());
		ops.compositeSize = Integer.parseInt(composite.getText());
		ops.entrySize = Integer.parseInt(entry.getText());
		ops.exitSize = Integer.parseInt(exit.getText());
		ops.choiceSize = Integer.parseInt(choice.getText());
		ops.finalSize = Integer.parseInt(finalS.getText());
		ops.chL = chL;
		ops.coL = coL;		
		ops.enL = enL;
		ops.exL = exL;
		ops.fiL = fiL;
		ops.tL = tL;
		ops.save();
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		boolean state;
		if (e.getStateChange() == ItemEvent.DESELECTED)
			state = false;
		else
			state = true;

		if (source == chB)
			chL = state;
		else if (source == coB)
			coL = state;
		else if (source == enB)
			enL = state;
		else if (source == exB)
			exL = state;
		else if (source == fiB)
			fiL = state;
		else if (source == tB)
			tL = state;
	}
}