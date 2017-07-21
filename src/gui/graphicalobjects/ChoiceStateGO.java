/**
 * 
 */
package gui.graphicalobjects;

import gui.Controller;
import gui.Grid;

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
 * ChoiceStateGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für Choice States.
 * @author sek
 *
 */

public class ChoiceStateGO extends StateGO {
	
	private int rad;
	private CompositeStateGO parentGO;
	private static int count = -1;
	private int gridsize;
	private boolean moved = false;
	private Point currentPosition;
	
	
	public ChoiceStateGO(CSMComponent component, CompositeStateGO parentGO, String name, Controller pc) {
		super(pc);
		type = GraphicalObject.CHOICESTATE;
		this.rad = pc.ops.choiceSize;
		this.component = component;
		this.component.setName(name);
		this.parentGO = parentGO;
		
		this.gridsize = pc.getWPGridSize();
		
		this.setCenter(component.getAbsolutePosition());
		
		currentPosition = new Point (component.getPosition().x, component.getPosition().y);
		
		count++;
		setLabel();
	}
	
	public ChoiceStateGO(CSMComponent component, CompositeStateGO parentGO, Controller pc) {
		super(pc);
		type = GraphicalObject.CHOICESTATE;
		this.rad = pc.ops.choiceSize;
		this.component = component;
		this.parentGO = parentGO;
		
		this.gridsize = pc.getWPGridSize();
		
		this.setCenter(component.getAbsolutePosition());
		
		currentPosition = new Point (component.getPosition().x, component.getPosition().y);
		
		count++;
		generateName();
		setLabel();
	}

	public void setCenter(Point position) {
		int x = (position.x % gridsize) - gridsize / 2;
		int y = (position.y % gridsize) - gridsize / 2;
		
		component.getPosition().x -= x;
		component.getPosition().y -= y;
	}
	
	public Point getCenter() {
		// return component.getPosition();
		return component.getAbsolutePosition();
	}
	
	public void moveCenter(Point move) {
		if(!moved) {
			currentPosition = (Point) component.getPosition().clone();
			moved = true;
		}			
		component.getPosition().translate(move.x, move.y);
	}
	
	public void setRadius(int rad) {
		this.rad = rad;
	}
	
	public int getRadius() {
		return this.rad;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		if(mark) {
			g2d.setPaint(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(stroke+1));
		} else {
			g2d.setPaint(this.color);
			g2d.setStroke(new BasicStroke(stroke));
		}
    	int xPoints[] = { component.getAbsolutePosition().x, component.getAbsolutePosition().x+rad,
    			component.getAbsolutePosition().x, component.getAbsolutePosition().x-rad};
    	int yPoints[] = { component.getAbsolutePosition().y-rad, component.getAbsolutePosition().y,
    			component.getAbsolutePosition().y+rad, component.getAbsolutePosition().y};
		
    	Color current = (Color) g2d.getPaint();
		g2d.setPaint(Color.WHITE);
    	g2d.fillPolygon(xPoints, yPoints, 4);
    	g2d.setPaint(current);
    	g2d.drawPolygon(xPoints, yPoints, 4);
    	paintLabel();
	}
	
	protected void generateName() {
		this.component.setName("ChS"+count); 
	}
	
	public int sizeIfClicked(Point p) {		
		if ((Math.abs(p.x-component.getAbsolutePosition().x) <= (rad + 2))
				&& (Math.abs(p.y-component.getAbsolutePosition().y) <= (rad + 2)))
			return rad*rad*4;
		return 0; 
	}
	
	public void fitToGrid() {
		if(moved) {
			Point target = component.getAbsolutePosition();
			Point current = (Point) currentPosition.clone();
		
			Grid grid;
			if(component.getParent() instanceof OutermostRegion)
				grid = pc.getWorkPanel().getGrid();
			else {		// -> folgt parentGO != null
				if(target.x > component.getParent().getAbsolutePosition().x) {
					component.setPosition(currentPosition);
					moved = false;
					return;
				}
				grid = parentGO.getGrid();
				target.translate(-parentGO.getCenter().x, -parentGO.getCenter().y);
				target.translate(parentGO.getWidth()/2, parentGO.getHeight()/2);
				current.translate(-parentGO.getCenter().x, -parentGO.getCenter().y);
				current.translate(parentGO.getWidth()/2, parentGO.getHeight()/2);
			}
			
			if(!grid.isFree(target, target)) {
				component.setPosition(currentPosition);
				moved = false;
				return;
			}
				
			this.setCenter(component.getAbsolutePosition());

			grid.moveBlock(current, current, target, target);
			
			moved = false;
		}
	}
	
	public void delete() {
		Point position = getCenter();
		if(component.getParent() instanceof OutermostRegion) {
			pc.getWorkPanel().getGrid().releaseBlock(position, position);
		}
		// TODO wenn nicht in OutermostRegion liegt
/*		else {
			grid.releaseBlock(position, position);
		}*/
	}
	
	protected void paintLabel() {
		label.setText(component.getName());
		label.setToolTipText(component.getName());
		int x = (this.getCenter().x - rad);
		int y = (this.getCenter().y - rad);
		label.setBounds(x-rad, y-fontSize-2, rad*4, fontSize+2);
		label.setVisible(pc.ops.chL);
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

