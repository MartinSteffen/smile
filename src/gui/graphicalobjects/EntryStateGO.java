/**
 * 
 */
package gui.graphicalobjects;

import gui.Controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JLabel;

import csm.statetree.CSMComponent;
import csm.statetree.OutermostRegion;

/**
 * EntryStateGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für Entry States.
 * 
 * @author omi
 *
 */
public class EntryStateGO extends StateGO {

	private int rad;
	private CompositeStateGO parentGO;
	private static int count = -1;

	
/*	public EntryStateGO() {
		super();
		type = GraphicalObject.ENTRYSTATE;
	}*/
	
	public EntryStateGO(CSMComponent component, CompositeStateGO parentGO, String name, Controller pc) {
		super(pc);
		type = GraphicalObject.ENTRYSTATE;
		this.rad = pc.ops.entrySize;
		this.component = component;
		this.component.setName(name);
		this.parentGO = parentGO;
		count++;
		setLabel();
	}
	
	public EntryStateGO(CSMComponent component, CompositeStateGO parentGO, Controller pc) {
		super(pc);
		type = GraphicalObject.ENTRYSTATE;
		this.rad = pc.ops.entrySize;
		this.component = component;
		this.parentGO = parentGO;
		count++;
		generateName();
		setLabel();
	}
	
	/**
	 * relative Position zur Parent-Komponente
	 * @param p
	 */
	public void setCenter(Point p) {
		component.setPosition(p);
	}
	
	/**
	 * 
	 * @return relative Position zur Parent-Komponente
	 */
	public Point getCenter() {
		//return component.getPosition();
		return component.getAbsolutePosition();
	}
	
	public void setRadius(int p) {
		this.rad = p;
	}
	
	public int getRadius() {
		return this.rad;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		
        this.setCenter(parentGO.getSidePosition(component.getAbsolutePosition()));
        
        int xCoord = component.getAbsolutePosition().x;
        int yCoord = component.getAbsolutePosition().y;
        
		g2d.setPaint(Color.WHITE);
		g2d.fillOval(xCoord - rad, yCoord - rad, 2*rad, 2*rad);
		
		if(mark) {
			g2d.setPaint(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(stroke+1));
		} else {
			g2d.setPaint(color);
			g2d.setStroke(new BasicStroke(stroke));
		}
		g2d.drawOval(xCoord - rad, yCoord - rad, 2*rad, 2*rad);
		paintLabel();
	}
	
	protected void generateName() {
		component.setName("EnS"+count);
	}
	
/*	public boolean isClicked(Point p) {
		if ( Math.sqrt(Math.pow(Math.abs(p.x-center.x),2) + Math.pow(Math.abs(p.y-center.y),2)) <= rad)
			return true;
		return false;
	}*/
	
	public int sizeIfClicked(Point p) {
		if ((Math.abs(p.x-component.getAbsolutePosition().x) <= (rad + 2))
				&& (Math.abs(p.y-component.getAbsolutePosition().y) <= (rad + 2)))
			return rad*rad*4;
		return 0; 
	}
	
	public void fitToGrid() {
		;
	}
	
	public void delete() {
		;
	}
	
	protected void paintLabel() {
		label.setText(component.getName());
		label.setToolTipText(component.getName());
		int x,y;
		Point ul, lr;
		ul = new Point(parentGO.getCenter().x-parentGO.getWidth()/2,parentGO.getCenter().y-parentGO.getHeight()/2);
		lr = new Point(parentGO.getCenter().x+parentGO.getWidth()/2,parentGO.getCenter().y+parentGO.getHeight()/2);
		if(component.getAbsolutePosition().y == ul.y) {
			// Label drüber
			x = component.getAbsolutePosition().x - rad*2;
			y = component.getAbsolutePosition().y - rad-fontSize-2;
		} else if (component.getAbsolutePosition().y == lr.y) {
			// Label drunter
			x = component.getAbsolutePosition().x - rad*2;
			y = component.getAbsolutePosition().y + rad;
		} else if (component.getAbsolutePosition().x == ul.x) {
			// Label links
			x = component.getAbsolutePosition().x - rad*5-1;
			y = component.getAbsolutePosition().y - fontSize/2-1;
		} else {
			// Label rechts
			x = component.getAbsolutePosition().x + rad+1;
			y = component.getAbsolutePosition().y - fontSize/2-1;
		}
		label.setBounds(x, y, rad*4, fontSize+2);
		label.setVisible(pc.ops.enL);
	}
	
	protected void setLabel() {
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		label = new JLabel(component.getName());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setToolTipText(component.getName());
		label.setFont(font);
		label.setBackground(this.labelBg);
		label.setBorder(this.labelBorder);
		label.setEnabled(true);
		pc.getWorkPanel().add(label);
	}
}
