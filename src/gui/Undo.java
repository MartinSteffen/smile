/**
 * 
 */
package gui;

import java.util.LinkedList;
import gui.graphicalobjects.GraphicalObject;

/**
 * @author Oliver
 *
 */
public class Undo {

	private LinkedList<Integer> undoActions;
	private LinkedList<GraphicalObject> undoObjects;
	private LinkedList<Integer> redoActions;
	private LinkedList<GraphicalObject> redoObjects;

	private WorkPanel wp;
	
	public static final int ADD = 1;
	public static final int DELETE = 2;
	
	public Undo(WorkPanel wp) {
		undoActions = new LinkedList<Integer>();
		undoObjects = new LinkedList<GraphicalObject>();
		redoActions = new LinkedList<Integer>();
		redoObjects = new LinkedList<GraphicalObject>();
		this.wp = wp;
	}
	
	public void push(int message, GraphicalObject object) {
		undoActions.addFirst(message);
		undoObjects.addFirst(object);
	}
	
	public void undo(int count) {
		
	}
}

