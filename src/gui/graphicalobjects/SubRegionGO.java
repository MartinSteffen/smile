/**
 * 
 */
package gui.graphicalobjects;

import gui.Controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;

import csm.exceptions.ErrTreeNotChanged;
import csm.statetree.CSMComponent;
import csm.statetree.SubRegion;

/**
 * SubRegionGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für SubRegions.
 * @author sek
 *
 */

public class SubRegionGO extends StateGO {

	private int width, height;
	//private boolean hasSubRegion = false;
	private LinkedList<SubRegion> subregions = new LinkedList<SubRegion>();
	private CompositeStateGO parent;
	//private LinkedList<GraphicalObject> children;
	private static int count = -1;
	
	
/*	public CompositeStateGO() {
		super();
		type = GraphicalObject.COMPOSITESTATE;
	}*/
	
	public SubRegionGO(CSMComponent component, CompositeStateGO parent, String name, int width, int height, Controller pc) {
		super(pc);
		type = GraphicalObject.COMPOSITESTATE;
		this.component = component;
		this.parent = parent;
		
		// Center und Dimensionen setzen/an Raster angepasst
		this.setCenter(this.component.getAbsolutePosition());
		this.setWidth(width);
		this.setHeight(height);
		
		this.count++;
	}
	
	public SubRegionGO(CSMComponent component, CompositeStateGO parent, int width, int height, Controller pc) {
		super(pc);
		type = GraphicalObject.COMPOSITESTATE;
		this.component = component;
		this.parent = parent;
		
		// Center und Dimensionen setzen/an Raster angepasst
		this.setCenter(this.component.getAbsolutePosition());
		this.setWidth(width);
		this.setHeight(height);
		
		this.count++;
		generateName();
	}

	public void setCenter(Point position) {
		//p.x = (int) Math.round(((double) (p.x))/20.0)*20;
		//p.y = (int) Math.round(((double) (p.y))/20.0)*20;

		if (parent != null) {
			position.x -= parent.getCenter().x;
			position.y -= parent.getCenter().y;
		}
		component.setPosition(position);
	}
	
	public Point getCenter() {
		return component.getAbsolutePosition();
	}
	
	public void setRelativePosition(Point position) {
		component.setPosition(position);
	}
	
	public Point getRelativePosition() {
		return component.getPosition();
	}
	
	public void moveCenter(Point move) {
		component.getPosition().translate(move.x, move.y);
	}
	
	public void setWidth(int width) {
		//this.width = (int) (Math.round(((double) width)/20.0))*20;
		this.width = width;
	}
	
	public void setHeight(int height) {
		//this.height = (int) (Math.round(((double) height)/20.0))*20;
		this.height = height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
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
		
		height = parent.getHeight();
		width = parent.getWidth();

		g2d.setPaint(new Color(230, 230, 230));
    	g2d.fillRoundRect(parent.getCenter().x-(width/2)+1,
    			parent.getCenter().y-(height/2)+1, width-1, height-1, 8, 8);

		
	}
	
	protected void generateName() {
		this.component.setName("subregion"+count); 
	}
	
	
/*	public boolean isClicked(Point p) {
		if ((Math.abs(p.x-center.x) <= width/2) && (Math.abs(p.y-center.y) <= height/2))
			return true;
		return false;
	}*/
	
/*	public Point getSidePosition(Point point) {
		
		int dx1 = point.x-(component.getPosition().x-(width/2));
		System.out.println(dx1);
		int dy1 = point.y-(component.getPosition().y-(height/2));
		System.out.println(dy1);
		int dx2 = width - dx1;
		System.out.println(dx2);
		int dy2 = height - dy1;
		System.out.println(dy2);
		
		if(dx1 < dx2 && dx1 < dy1 && dx1 < dy2) 
			return new Point(component.getPosition().x-(width/2), point.y);
		else if(dx2 < dx1 && dx2 < dy1 && dx2 < dy2) 
			return new Point(component.getPosition().x+(width/2), point.y);
		else if(dy1 < dx2 && dy1 < dx1 && dy1 < dy2) 
			return new Point(point.x, component.getPosition().y-(height/2));
		else 
			return new Point(point.x, component.getPosition().y+(height/2));
		}*/
		
	public int sizeIfClicked(Point p) {
		if ((Math.abs(p.x-component.getAbsolutePosition().x) <= (width/2 + 2))
				&& (Math.abs(p.y-component.getAbsolutePosition().y) <= (height/2 + 2)))
			return height*width-2;
		return 0; 
	}
	
	public void resizeRight(int resize) {
		this.setWidth(width + resize);
		component.getAbsolutePosition().x = component.getAbsolutePosition().x + (int)((double) resize)/2;
	}
	
	public void resizeLeft(int resize) {
		this.setWidth(width + resize);
		component.getAbsolutePosition().x = component.getAbsolutePosition().x - (int)((double) resize)/2;
	}
	
	public void resizeTop(int resize) {
		this.setHeight(height + resize);
		component.getAbsolutePosition().y = component.getAbsolutePosition().y - (int)((double) resize)/2;
	}	
	
	public void resizeBottom(int resize) {
		this.setHeight(height + resize);
		component.getAbsolutePosition().y = component.getAbsolutePosition().y + (int)((double) resize)/2;
	}
	
	
	// experimental - sek
	public Point getSidePosition(Point point) {
		
		int dx = point.x-(component.getAbsolutePosition().x);
		int dy = point.y-(component.getAbsolutePosition().y);
		
		/* Aufteilung des CompositeState in vier Sektoren
		 * _______
		 * |\ 1 /|
		 * | \ / |
		 * |4 X 2|
		 * | / \ |
		 * |/ 3 \|
		 * -------
		 */	
		// obererer Sektor (1)
		if(-dx <= -dy && dx <= -dy) 
			return new Point(dx, -(height/2));
		// rechter Sektor (2)
		else if(-dy <= dx && dy <= dx) 
			return new Point((width/2), dy);
		// unterer Sektor (3)
		else if(-dx <= dy && dx <= dy) 
			return new Point(dx, (height/2));
		// linker Sektor (4)
		else 
			return new Point(-(width/2), dy);
	}
	
/*	public void dropHere(GraphicalObject childGO) {
		parent.dropHere(childGO);
	}*/
	
	public void fitToGrid() {
		this.setCenter(component.getAbsolutePosition());
	}
	
	public void delete() {
		;
	}
	
	protected void setLabel() {
		
	}
	
	protected void paintLabel() {
		
	}
}

