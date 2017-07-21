/**
 * 
 */
package gui.graphicalobjects;

import gui.Controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import csm.statetree.CSMComponent;
//import csm.statetree.State;

/**
 * @author Oliver
 *
 */
public abstract class GraphicalObject {

	public static final int CHOICESTATE = 1;
	public static final int COMPOSITESTATE = 2;
	public static final int ENTRYSTATE = 3;
	public static final int EXITSTATE = 4;
	public static final int FINALSTATE = 5;
	public static final int OUTERMOSTREGION = 6;
	public static final int SUBREGION = 7;
	public static final int TRANSITION = 8;
	
	protected boolean mark;
	protected CSMComponent component;
	protected int type, fontSize;
	protected Color color, labelBg;
	protected Border labelBorder;
	protected int stroke;
	protected JLabel label;
	protected Controller pc;
	//protected State state;
	
	public GraphicalObject(Controller pc) {
		this.pc = pc;
		fontSize = 12;
		labelBg = new Color(196,234,255);
		labelBorder = new LineBorder(Color.BLACK, 1, true);
		color = Color.BLACK;
		stroke = 1;
		mark = false;
		
	}
	
	public abstract void paint(Graphics g);
	public abstract int sizeIfClicked(Point point);
	public abstract void fitToGrid();
	public abstract void delete();
	protected abstract void generateName();
	protected abstract void paintLabel();
	protected abstract void setLabel();
	
	
	
	// Was zeichnen wir für eine Komponente?
	// Entsprechend in die CSM einfügen!
	// Brauchen wir sowas auch für States?
	public CSMComponent getComponent() {
		return component;
	}

	public final int getType() {
		return type;
	}
	
	public final String getName() {
		return component.getName();
	}
	
	public final void setName(String name) {
		component.setName(name);
	}
	
	public final void setStroke(int stroke) {
		this.stroke = stroke;
	}
	
	public final void setColor(Color c) {
		this.color = c;
	}
	
	public final boolean isType(int type) {
		return this.type == type;
	}
	
	public final boolean isMarked() {
		return mark;
	}
	
	public final void setMark(boolean state) {
		mark = state;
	}
	
	public final void toggleMark() {
		mark = !mark;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
}
	

