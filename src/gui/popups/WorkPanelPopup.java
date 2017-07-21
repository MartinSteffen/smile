/**
 * 
 */
package gui.popups;

import gui.WorkPanel;

import gui.graphicalobjects.GraphicalObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author Oliver
 *
 */
public class WorkPanelPopup extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 5729350798153767400L;

	private WorkPanel wp;
	private GraphicalObject go;
	
	public WorkPanelPopup(WorkPanel wp, GraphicalObject go) {
		super();
		this.go = go;
		this.wp = wp;
//		makeItem("Undo", "undo");
		makeItem("Delete", "del");
//		makeItem("Print variables", "print");
		this.addSeparator();
		if(go == null) {
			makeItem("Properties", "csmprop");
		}
		else {
		switch(go.getType()) {
		case(GraphicalObject.CHOICESTATE): {
			makeItem("Properties", "choiceprop");
			break;
		}
		case(GraphicalObject.COMPOSITESTATE): {
			makeItem("Add Subregion", "subregion");
			makeItem("Properties", "compositeprop");
			break;
		}
		case(GraphicalObject.ENTRYSTATE): {
			makeItem("Properties", "entryprop");
			break;
		}
		case(GraphicalObject.EXITSTATE): {
			makeItem("Properties", "exitprop");
			break;
		}
		case(GraphicalObject.FINALSTATE): {
			makeItem("Properties", "finalprop");
			break;
		}
		case(GraphicalObject.TRANSITION): {
			makeItem("Properties", "transitionprop");
			break;
		}
		}
		}
	}
	
	private JMenuItem makeItem(String name, String command) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(this);
		item.setActionCommand(command);
		add(item);
		return item;
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("undo"))
			;
		else if(cmd.equals("del")) {
			LinkedList<GraphicalObject> temp = new LinkedList<GraphicalObject>();
			temp.add(go);
			wp.delete(temp);
		}
		else if(cmd.equals("print"))
			wp.printVariables();
		else if(cmd.equals("subregion"))
			wp.addSubregion();
		else if(cmd.equals("choiceprop"))
			new PropertiesChoice(wp, go.getComponent(), this.getLocation());	
		else if(cmd.equals("entryprop"))
			new PropertiesEntry(wp, go.getComponent(), this.getLocation());
		else if(cmd.equals("exitprop"))
			new PropertiesExit(wp, go.getComponent(), this.getLocation());
		else if(cmd.equals("finalprop"))
			new PropertiesFinal(wp, go.getComponent(), this.getLocation());
		else if(cmd.equals("compositeprop"))
			new PropertiesComposite(wp, go.getComponent(), this.getLocation());
		else if(cmd.equals("transitionprop"))
			new PropertiesTransition(wp, go.getComponent(), this.getLocation());
		else if(cmd.equals("csmprop"))
			new PropertiesCSM(wp, null, this.getLocation());
	}
}
