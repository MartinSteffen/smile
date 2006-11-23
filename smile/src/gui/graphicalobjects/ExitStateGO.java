/**
 * 
 */
package gui.graphicalobjects;

import java.awt.Graphics;
import java.awt.Point;

import csm.statetree.CSMComponent;

/**
 * @author omi
 *
 */

public class ExitStateGO extends StateGO {
	
	private Point center;
	private int rad;
	
	public ExitStateGO() {
		super();
	}
	
	public ExitStateGO(CSMComponent component, String name, Point center, int rad) {
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
		int c = (int)Math.round((Math.sqrt(2*rad*rad))/2);
		g.drawLine(center.x + c, center.y + c, center.x - c, center.y - c);
		g.drawLine(center.x + c, center.y - c, center.x - c, center.y + c);
	}
}

