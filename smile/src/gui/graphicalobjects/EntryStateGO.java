/**
 * 
 */
package gui.graphicalobjects;

import java.awt.Graphics;
import java.awt.Point;

import csm.statetree.CSMComponent;

/**
 * EntryStateGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für Entry States.
 * 
 * @author omi
 *
 */
public class EntryStateGO extends StateGO {
	
	private Point center;
	private int rad;
	
	public EntryStateGO() {
		super();
	}
	
	public EntryStateGO(CSMComponent component, String name, Point center, int rad) {
		super();
		this.rad = rad;
		this.center = center;
		this.component = component;
		this.component.setName(name);
	}

	public void setCenter(Point p) {
		this.center = p;
	}
	
	public Point getCenter() {
		return this.center;
	}
	
	public void setRadius(int p) {
		this.rad = p;
	}
	
	public int getRadius() {
		return this.rad;
	}
	
	public void paint(Graphics g) {
		g.setColor(this.color);
		g.drawOval(center.x-rad, center.y-rad, 2*rad, 2*rad);
	}
}
