/**
 * 
 */
package gui;

import gui.graphicalobjects.ChoiceStateGO;
import gui.graphicalobjects.CompositeStateGO;
import gui.graphicalobjects.GraphicalObject;

import java.awt.Point;
import java.awt.event.MouseEvent;

import java.awt.event.MouseMotionAdapter;

/**
 * @author omi
 * 
 */
public class MouseMotionListener extends MouseMotionAdapter {

	public void mouseMoved(MouseEvent e) {
		((WorkPanel)e.getSource()).mousePosition(e.getPoint());
	}
	
	public void mouseDragged(MouseEvent e) {
		((WorkPanel)e.getSource()).mouseDragged(e.getPoint());
	}
}