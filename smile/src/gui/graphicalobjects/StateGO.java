/**
 * 
 */
package gui.graphicalobjects;

import java.awt.Color;
import java.awt.Graphics;

import csm.statetree.CSMComponent;

/**
 * StateGO ist die Oberklasse für die Objekte, die die States darstellen
 * 
 * @author omi
 *
 */
public abstract class StateGO {
	
	protected CSMComponent component;
	protected Color color;
	
	public StateGO() {
		
	}
	
	public abstract void paint(Graphics g);

	public String getName() {
		return component.getName();
	}
	
	public void setName(String name) {
		component.setName(name);
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
}
