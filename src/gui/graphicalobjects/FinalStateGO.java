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
 * FinalStateGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für Final States.
 * @author sek
 *
 */

public class FinalStateGO extends StateGO {

	private int rad;
	private CompositeStateGO parentGO;
	private int gridsize;
	private boolean moved = false;
	private Point currentPosition;
	
	private static int count = -1;

	
	public FinalStateGO(CSMComponent component, CompositeStateGO parentGO, String name, Controller pc) {
		super(pc);
		type = GraphicalObject.FINALSTATE;
		this.rad = pc.ops.finalSize;
		this.component = component;
		this.component.setName(name);
		this.parentGO = parentGO;
		
		this.gridsize = pc.getWPGridSize();
		
		this.setCenter(component.getAbsolutePosition());
		
		currentPosition = new Point (component.getPosition().x, component.getPosition().y);
		
		count++;
		setLabel();
	}

	public FinalStateGO(CSMComponent component, CompositeStateGO parentGO, Controller pc) {
		super(pc);
		type = GraphicalObject.FINALSTATE;
		this.rad = pc.ops.finalSize;
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
		return component.getAbsolutePosition();
	}
	
	public void moveCenter(Point move) {
		if(!moved) {
			currentPosition = new Point (component.getPosition().x, component.getPosition().y);
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
			g2d.setStroke(new BasicStroke(stroke));
			g2d.setPaint(this.color);
		}
		g2d.fillOval(this.getCenter().x-rad, this.getCenter().y-rad, 2*rad, 2*rad);
		Color current = (Color) g2d.getPaint();
		g2d.setPaint(Color.WHITE);
		g2d.fillOval(this.getCenter().x-rad+1, this.getCenter().y-rad+1, 2*(rad-1), 2*(rad-1));
		g2d.setPaint(current);
		g2d.fillOval(this.getCenter().x-rad+2, this.getCenter().y-rad+2, 2*(rad-2), 2*(rad-2));
		paintLabel();
	}
	
	protected void generateName() {
		component.setName("final"+count);
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
	
	public Point getSidePosition(Point point) {
		
		int dx = point.x-(this.getCenter().x);
		int dy = point.y-(this.getCenter().y);
		
		/* Aufteilung des FinalState in vier Sektoren
		 * (einfach mal als Kreis vorstellen...)
		 * _______
		 * |\ 1 /|
		 * | \ / |
		 * |4 X 2|
		 * | / \ |
		 * |/ 3 \|
		 * -------
		 */	
		/*
		 * Berechnung der Positionen mit Kreisgleichung x² + y² = r².
		 * In Sektor 1 und 3 bleibt x-Position fest, in Sektor 2 und 4
		 * bleibt die y-Position fest. Der jeweils andere Wert wird
		 * dann berechnet.
		 */
		// obererer Sektor (1)
		if(-dx <= -dy && dx <= -dy) 
			return new Point(dx, (int) - Math.round(Math.sqrt((double) (rad*rad - dx*dx))));
		// rechter Sektor (2)
		else if(-dy <= dx && dy <= dx) 
			return new Point((int) + Math.round(Math.sqrt((double) (rad*rad - dy*dy))), dy);
		// unterer Sektor (3)
		else if(-dx <= dy && dx <= dy) 
			return new Point(dx, (int) + Math.round(Math.sqrt((double) (rad*rad - dx*dx))));
		// linker Sektor (4)
		else 
			return new Point((int) - Math.round(Math.sqrt((double) (rad*rad - dy*dy))), dy);
	}
	
	public void fitToGrid() {
		if(moved) {
			Point target = component.getAbsolutePosition();			
			Point current = (Point) currentPosition.clone();
		
			Grid grid;
			if(component.getParent() instanceof OutermostRegion) {
				grid = pc.getWorkPanel().getGrid();
			}
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
		label.setVisible(pc.ops.fiL);
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

