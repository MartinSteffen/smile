package gui;

import javax.swing.ButtonGroup;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import java.net.URL;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author omi
 * @version 1.0
 */
public class SMILEToolBar extends JToolBar implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3205324795330160908L;
	private Controller pc;
	private JToggleButton compositeJB, choiceJB, entryJB, exitJB, finalJB, transitionJB, markJB, moveresizeJB;
	private ButtonGroup bg = new ButtonGroup();

	public SMILEToolBar(Controller pc) {
		//super("Tools");
		this.pc = pc;
		pc.setToolBar(this);

		// set Buttons
		compositeJB = makeButton("compst.gif", "Add Composite State", "Composite State");
		choiceJB = makeButton("choicest.gif", "Add Choice State", "Choice State");
		entryJB = makeButton("entryst.gif", "Add Entry State", "Entry State");
		exitJB = makeButton("exitst.gif", "Add Exit State", "Exit State");
		finalJB = makeButton("finalSt.gif", "Add Final State", "Final State");
		addSeparator();
		transitionJB = makeButton("trans.gif", "Add Transition", "Transition");
		addSeparator();
		markJB = makeButton("mark.gif", "Marking Tool", "Mark");
		addSeparator();
		moveresizeJB = makeButton("moveresize.gif", "Move/Resize", "Move/Resize");
	}

	protected JToggleButton makeButton(String imageName, String toolTipText, String altText) {
		// Look for the image.
		String imgLocation = "images/" + imageName;
		URL imageURL = SMILEToolBar.class.getResource(imgLocation);

		// Create and initialize the button.
		JToggleButton button = new JToggleButton();
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL));
		} else { // no image found
			button.setText(altText);
		}
		bg.add(button);
		button.setSelected(false);
		add(button);
		return button;
	}

	public void actionPerformed(ActionEvent e) {
		if (bg.isSelected(compositeJB.getModel())) {
			pc.setAction(Controller.COMPOSITE);
		} else if (bg.isSelected(choiceJB.getModel())) {
			pc.setAction(Controller.CHOICE);
		} else if (bg.isSelected(entryJB.getModel())) {
			pc.setAction(Controller.ENTRY);
		} else if (bg.isSelected(exitJB.getModel())) {
			pc.setAction(Controller.EXIT);
		} else if (bg.isSelected(finalJB.getModel())) {
			pc.setAction(Controller.FINAL);
		} else if (bg.isSelected(transitionJB.getModel())) {
			pc.setAction(Controller.TRANSITION);
		} else if (bg.isSelected(markJB.getModel())) {
			pc.setAction(Controller.MARK);
		} else if (bg.isSelected(moveresizeJB.getModel())) {
			pc.setAction(Controller.MOVERESIZE);
		}
	}
	
	public void unselectButtonGroup() {
			bg.setSelected(bg.getSelection(), false);
			pc.setAction(-1);
		}
}
