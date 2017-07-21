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
import java.util.LinkedList;

import javax.swing.JLabel;

import csm.exceptions.ErrTreeNotChanged;
import csm.statetree.CSMComponent;
import csm.statetree.SubRegion;
import csm.statetree.OutermostRegion;

/**
 * CompositeStateGO erweitert StateGO und repräsentiert die grafischen Objekte 
 * für Choice States.
 * @author sek
 *
 */

public class CompositeStateGO extends StateGO {

	private int width, height;
	private double dx = 0.0, dy = 0.0;
	private CompositeStateGO parentGO;
	private LinkedList<SubRegion> subregions = new LinkedList<SubRegion>();
	private LinkedList<GraphicalObject> children = new LinkedList<GraphicalObject>();
	
	private Grid grid;
	private int gridrows, gridcols;
	private int gridsize;
	private int layer = 0;
	private boolean moved = false, widthresized = false, heightresized = false;
	private boolean changed = false;
	private Point currentPosition;
	private int currentwidth, currentheight;
	
	private Point oldRelativeCenter;
	
	private int default_width, default_height;
	
	private boolean resize_left, resize_right, resize_top, resize_bottom;
	
	private static int count = -1;
	
	private static final int LEFT = 11;
	private static final int RIGHT = 12;
	private static final int UP = 13;
	private static final int DOWN = 14;
	
	
	
	public CompositeStateGO(CSMComponent component, CompositeStateGO parentGO, String name, Controller pc) {
		super(pc);
		type = GraphicalObject.COMPOSITESTATE;
		this.component = component;
		this.component.setName(name);
		
		this.parentGO = parentGO;
		if(this.parentGO != null)
			this.layer = this.parentGO.getLayer() - 1;
		
		/* default: CompositeStateGO belegt genau ein Grid-Feld,
		 * in diesem Zustand enthaelt er nur ein Grid der Groesse 1x1.
		 */
		this.gridsize = pc.getWPGridSize();
		this.gridrows = 1;
		this.gridcols = 1;
		this.grid = new Grid(gridrows,gridcols,gridsize);
		
		currentPosition = new Point (component.getPosition().x, component.getPosition().y);
		currentwidth = width;
		currentheight = height;
		
		
		this.setCenter(this.component.getAbsolutePosition());
		this.default_width = pc.ops.compositeSize;
		this.default_height = pc.ops.compositeSize;
		this.setWidth(pc.ops.compositeSize);
		this.setHeight(pc.ops.compositeSize);
		
		count++;
		setLabel();
	}
	
	public CompositeStateGO(CSMComponent component, CompositeStateGO parentGO, Controller pc) {
		super(pc);
		type = GraphicalObject.COMPOSITESTATE;
		this.component = component;
		this.parentGO = parentGO;
		if(this.parentGO != null)
			this.layer = this.parentGO.getLayer() - 1;
		
		/* default: CompositeStateGO belegt genau ein Grid-Feld,
		 * in diesem Zustand enthaelt er nur ein Grid der Groesse 1x1.
		 */
		this.gridsize = pc.getWPGridSize();
		this.gridrows = 1;
		this.gridcols = 1;
		this.grid = new Grid(gridrows,gridcols,gridsize);
		
		currentPosition = new Point (component.getPosition().x, component.getPosition().y);
		
		this.setCenter(this.component.getAbsolutePosition());
		this.default_width = pc.ops.compositeSize;
		this.default_height = pc.ops.compositeSize;
		this.setWidth(pc.ops.compositeSize);
		this.setHeight(pc.ops.compositeSize);
		
		count++;
		generateName();
		setLabel();
	}

	public void setCenter(Point position) {
		
		int x = (position.x / gridsize) * gridsize + gridsize / 2;
		int y = (position.y / gridsize) * gridsize + gridsize / 2;


		if((gridrows % 2) == 0)
			y -= gridsize / 2;
		if((gridcols % 2) == 0)
			x -= gridsize / 2;
		
		CSMComponent parent = component.getParent();		
		if (parent != null) {
			//pc.sendMessage(this.getName() + " parent = " + parent.getName() + " - " + parent.getAbsolutePosition(), false);
			x -= parent.getAbsolutePosition().x;
			y -= parent.getAbsolutePosition().y;
		}
		
		component.getPosition().x = x;
		component.getPosition().y = y;		
		
		// neuer Punkt ausserhalb des zulässigen Bereichs?
/*		if(parentGO != null) {
			//System.out.println("shift: " + component.getPosition());
			if(Math.abs(component.getPosition().x) > parentGO.getWidth()/2 ||
					Math.abs(component.getPosition().y) > parentGO.getHeight() / 2) {
				pc.sendMessage(" ABBRUCH DER AKTION - verschoben in nicht zulaessigen Bereich.", true);
				component.setPosition(currentPosition);
			}
		}*/
		
		if(currentPosition.x - x != 0 || currentPosition.y - y != 0) {
			if(parent != null && oldRelativeCenter != null) {
				if(parent instanceof SubRegion && parentGO != null) {
					if(component.getPosition().x > 0 || component.getPosition().x <
							parentGO.getSubregionsLeftBound(parent.getPosition()) - parent.getPosition().x ||
							Math.abs(component.getPosition().y) > parentGO.getHeight()/2) {
						component.setPosition(currentPosition);
						//pc.sendMessage(" ABBRUCH DER AKTION - verschoben in nicht zulaessigen Bereich.", true);
					}
				}
			}		
		}
		
		if(this.component.getPosition() != null)
			oldRelativeCenter = (Point) this.component.getPosition().clone();
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
	
	public void setRelativePosition(Point position) {
		component.setPosition(position);
	}
	
	public Point getRelativePosition() {
		return component.getPosition();
	}
	
	public void setWidth(int width) {
		currentwidth = width;
		this.width = width - (width % gridsize) + ((int) (default_width)) + 8 * layer;
	}
	
	public void setHeight(int height) {
		currentheight = height;
		this.height = height - (height % gridsize) + ((int) (default_height)) + 8 * layer;
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
		
		int x = this.getCenter().x;
		int y = this.getCenter().y;

/*		if(component.getParent() instanceof OutermostRegion) {
			Color current = (Color) g2d.getPaint();
			g2d.setPaint(Color.WHITE);
			//g2d.fillRoundRect(x-(width/2)+1,y-(height/2)+1, width, height, 8, 8);
			g2d.setPaint(current);
		}
*/
		g2d.drawRoundRect(x-(width/2),y-(height/2), width, height, 8, 8);
		if(isStartState())
			g2d.fillOval(getCenter().x+width/2-5, getCenter().y+height/2-5, 10, 10);

		// Umgestellt auf nur eine SubRegion
/*		if(!subregions.isEmpty()) {
			for(SubRegion subregion: subregions) {
				int sub_x = subregion.getAbsolutePosition().x;
				if(sub_x <= this.getCenter().x + this.getWidth()/2)
					g2d.drawLine(sub_x, y-(height/2), sub_x, y+(height/2));
			}
		}*/
/*		for(int i = 0; i < children.size(); i++) {
			children.get(i).paint(g);
		}*/ 
		
		
		paintLabel();
	}
	
	protected void generateName() {
		this.component.setName("CoS"+count); 
	}
	
	public int sizeIfClicked(Point p) {
		if ((Math.abs(p.x-component.getAbsolutePosition().x) <= (width/2 + 2))
				&& (Math.abs(p.y-component.getAbsolutePosition().y) <= (height/2 + 2)))
			return height*width;
		return 0; 
	}
	
	public void resizeLeft(int resize) {
		width += resize;
		dx -= ((double) resize)/2.0;
		this.moveCenter(new Point((int) dx, 0));
		
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO) {
				((CompositeStateGO) children.get(i)).moveCenter(new Point(-resize,0));
				((CompositeStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof ChoiceStateGO) {
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(-resize,0));
				((ChoiceStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof FinalStateGO) {
				((FinalStateGO) children.get(i)).moveCenter(new Point(-resize,0));
				((FinalStateGO) children.get(i)).fitToGrid();
			}
		}
		for(SubRegion subregion: subregions) {
			subregion.getPosition().x -= (int) dx;
		}
		
		resize_left = true;
		dx -= (double) (int) dx;
	}
	
	public void resizeRight(int resize) {
		width += resize;
		dx += ((double) resize)/2.0;
		this.moveCenter(new Point((int) dx, 0));
		
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO) {
				((CompositeStateGO) children.get(i)).moveCenter(new Point(resize,0));
				((CompositeStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof ChoiceStateGO) {
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(resize,0));
				((ChoiceStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof FinalStateGO) {
				((FinalStateGO) children.get(i)).moveCenter(new Point(resize,0));
				((FinalStateGO) children.get(i)).fitToGrid();
			}
		}
		for(SubRegion subregion: subregions) {
			subregion.getPosition().x -= (int) dx;
		}
		
		resize_right = true;
		dx -= (double) (int) dx;
	}
	
	public void resizeTop(int resize) {
		height += resize;
		dy -= ((double) resize)/2.0;
		this.moveCenter(new Point(0, (int) dy));
		resize_top = true;
		dy -= (double) (int) dy;
		
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO) {
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0,-resize));
				((CompositeStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof ChoiceStateGO) {
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(0,-resize));
				((ChoiceStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof FinalStateGO) {
				((FinalStateGO) children.get(i)).moveCenter(new Point(0,-resize));
				((FinalStateGO) children.get(i)).fitToGrid();
			}
		}
	}	
	
	public void resizeBottom(int resize) {
		height += resize;
		dy += ((double) resize)/2.0;
		this.moveCenter(new Point(0, (int) dy));
		resize_bottom = true;
		dy -= (double) (int) dy;
		
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO) {
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0,resize));
				((CompositeStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof ChoiceStateGO) {
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(0,resize));
				((ChoiceStateGO) children.get(i)).fitToGrid();
			}
			if(children.get(i) instanceof FinalStateGO) {
				((FinalStateGO) children.get(i)).moveCenter(new Point(0,resize));
				((FinalStateGO) children.get(i)).fitToGrid();
			}
		}
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
	
	public SubRegion getSubRegion(Point position) {
		if(subregions.isEmpty()) {
			return null;
		}
		
		return subregions.getFirst();
		
		// Umgestellt auf nur eine SubRegion
/*		for(SubRegion subregion: subregions) {
			if(position.x < subregion.getAbsolutePosition().x) {
				return subregion;
			}
		}
		return null;*/
	}
	
	public void addSubregion(Point position) {
		
		int x = ((position.x / gridsize) + 1) * gridsize; 
		x -= this.getCenter().x;
		int y = 0;						// y-Position der SubRegion mittig im CompositeState
		
		SubRegion subregion = new SubRegion(new Point(x,y));
		this.addSubregion(subregion);
	}
	
	
	public void addSubregion(SubRegion subregion) {
		
		// Umgestellt auf nur eine SubRegion
		if(!subregions.isEmpty())
			return;
		
		try {
			this.getComponent().dropHere(subregion);
		} catch (ErrTreeNotChanged e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pc.sendMessage(e.getMessage(), true);
		}
		if(subregions.isEmpty()) {
			this.increaseLayer();
			//moved = true;
/*			if(gridcols < 2)
				this.resizeRight((2-gridcols) * gridsize);
			if(gridrows < 2)
				this.resizeBottom((2-gridrows) * gridsize);*/
		}

		// Umgestellt auf nur eine SubRegion
/*		int index = 0;
		for(SubRegion sub: subregions) {
			if(sub.getPosition().x == subregion.getPosition().x)
				return;
			if(sub.getPosition().x > subregion.getPosition().x)
				break;
			index++;			
		}
		subregions.add(index, subregion);*/
		
		subregions.add(subregion);
		
		pc.sendMessage("subregion added", false);

		this.fitToGrid();
		//this.paint(pc.getWorkPanel().getGraphics());
	}
	
/*	public void dropHere(GraphicalObject childGO) {

		Point location;
		int d_left, d_right, d_top, d_bottom;
		
		
		if(childGO instanceof CompositeStateGO) {
			CompositeStateGO child = (CompositeStateGO) childGO;
			location = child.getCenter();
			// check left
			if ((d_left = (this.getCenter().x - width/2) - (location.x - child.getWidth()/2)) > -10)
				resizeLeft2(d_left);
			// check right
			if((d_right = (location.x + child.getWidth()/2) - (this.getCenter().x + width/2)) > -10)
				resizeRight2(d_right);
			// check top
			if ((d_top = (this.getCenter().y - height/2) - (location.y - child.getHeight()/2)) > -10)
				resizeTop2(d_top);
			// check bottom
			if ((d_bottom = (location.y + child.getHeight()/2) - (this.getCenter().y + height/2)) > -10)
				resizeBottom2(d_bottom);
		}
		else if(childGO instanceof ChoiceStateGO) {
			ChoiceStateGO child = (ChoiceStateGO) childGO;
			location = child.getCenter();
			int radius = child.getRadius();
			// check left
			if ((d_left = (this.getCenter().x - width/2) - (location.x - radius)) > -10)
				resizeLeft2(d_left);
			// check right
			if((d_right = (location.x + radius) - (this.getCenter().x + width/2)) > -10)
				resizeRight2(d_right);
			// check top
			if ((d_top = (this.getCenter().y - height/2) - (location.y - radius)) > -10)
				resizeTop2(d_top);
			// check bottom
			if ((d_bottom = (location.y + radius) - (this.getCenter().y + height/2)) > -10)
				resizeBottom2(d_bottom);
		}
		else if(childGO instanceof FinalStateGO) {
			FinalStateGO child = (FinalStateGO) childGO;
			location = child.getCenter();
			int radius = child.getRadius();
			// check left
			if ((d_left = (this.getCenter().x - width/2) - (location.x - radius)) > -10)
				resizeLeft2(d_left);
			// check right
			if((d_right = (location.x + radius) - (this.getCenter().x + width/2)) > -10)
				resizeRight2(d_right);
			// check top
			if ((d_top = (this.getCenter().y - height/2) - (location.y - radius)) > -10)
				resizeTop2(d_top);
			// check bottom
			if ((d_bottom = (location.y + radius) - (this.getCenter().y + height/2)) > -10)
				resizeBottom2(d_bottom);
		}
		
		children.add(childGO);
	}*/
	
	public void dropHere2(GraphicalObject childGO) {	
		
		int x = 0;
		int y = 0;
		
		if(childGO instanceof CompositeStateGO) {
			x = ((CompositeStateGO) childGO).getCenter().x - this.getCenter().x;
			y = ((CompositeStateGO) childGO).getCenter().y - this.getCenter().y;
		}
		else if(childGO instanceof ChoiceStateGO) {
			x = ((ChoiceStateGO) childGO).getCenter().x - this.getCenter().x;
			y = ((ChoiceStateGO) childGO).getCenter().y - this.getCenter().y;
		}
		else if(childGO instanceof FinalStateGO) {
			x = ((FinalStateGO) childGO).getCenter().x - this.getCenter().x;
			y = ((FinalStateGO) childGO).getCenter().y - this.getCenter().y;
		}

		x += (grid.getCols() - 1) * (gridsize / 2);
		y += (grid.getRows() - 1) * (gridsize / 2);
		
		this.grid.insert(new Point(x,y));
		
		children.add(childGO);		
	}
	
	public void removeHere(GraphicalObject childGO) {
		this.grid.delete(((CompositeStateGO) childGO).getRelativePosition());
		
		children.remove(childGO);
	}	
	
	void resizeLeft2(int resize) {
		this.setWidth(this.getWidth() + resize + 20);
		this.getRelativePosition().x -= (resize/2 + 10);  
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(resize/2 + 10, 0));
		}
	}
	
	void resizeRight2(int resize) {
		this.setWidth(this.getWidth() + resize + 20);
		this.getRelativePosition().x += (resize/2 + 10);  
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(-(resize/2 + 10), 0));
		}
	}
	
	void resizeTop2(int resize) {
		this.setHeight(this.getHeight() + resize + 20);
		this.getRelativePosition().y -= (resize/2 + 10);
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0, resize/2 + 10));
		}
	}
	
	void resizeBottom2(int resize) {
		this.setHeight(this.getHeight() + resize + 20);
		this.getRelativePosition().y += (resize/2 + 10);  
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0, -(resize/2 + 10)));
		}
	}
	
	private void resizeGridLeft(int cols) {		
		if(cols < 0)
			grid.reduceSize(-cols, Grid.LEFT);
		else if(cols > 0)
			grid.extendSize(cols, Grid.LEFT);
		this.gridcols = grid.getCols();

		//System.out.println("current:: rows: " + gridrows + ", cols: " + gridcols);
		
/*		for(int i = 0; i < children.size(); i++) {
			int x = cols * gridsize / 2;
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(x,0));
			if(children.get(i) instanceof ChoiceStateGO)
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(x,0));
			if(children.get(i) instanceof FinalStateGO)
				((FinalStateGO) children.get(i)).moveCenter(new Point(x,0));
		}*/
	}
	
	private void resizeGridRight(int cols) {		
		if(cols < 0)
			grid.reduceSize(-cols, Grid.RIGHT);
		else if(cols > 0)
			grid.extendSize(cols, Grid.RIGHT);
		this.gridcols = grid.getCols();

		//System.out.println("current:: rows: " + gridrows + ", cols: " + gridcols);
		
/*		for(int i = 0; i < children.size(); i++) {
			int x = - cols * gridsize / 2;
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(x,0));
			if(children.get(i) instanceof ChoiceStateGO)
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(x,0));
			if(children.get(i) instanceof FinalStateGO)
				((FinalStateGO) children.get(i)).moveCenter(new Point(x,0));
		}*/
	}
	
	private void resizeGridTop(int rows) {		
		if(rows < 0)
			grid.reduceSize(-rows, Grid.TOP);
		else if(rows > 0)
			grid.extendSize(rows, Grid.TOP);
		this.gridrows = grid.getRows();

		//System.out.println("current:: rows: " + gridrows + ", cols: " + gridcols);
		
/*		for(int i = 0; i < children.size(); i++) {
			int y = rows * gridsize / 2;
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0,y));
			if(children.get(i) instanceof ChoiceStateGO)
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(0,y));
			if(children.get(i) instanceof FinalStateGO)
				((FinalStateGO) children.get(i)).moveCenter(new Point(0,y));
		}*/
	}
	
	private void resizeGridBottom(int rows) {		
		if(rows < 0)
			grid.reduceSize(-rows, Grid.BOTTOM);
		else if(rows > 0)
			grid.extendSize(rows, Grid.BOTTOM);
		this.gridrows = grid.getRows();

		//System.out.println("current:: rows: " + gridrows + ", cols: " + gridcols);
		
/*		for(int i = 0; i < children.size(); i++) {
			int y = - rows * gridsize / 2;
			if(children.get(i) instanceof CompositeStateGO)
				((CompositeStateGO) children.get(i)).moveCenter(new Point(0,y));
			if(children.get(i) instanceof ChoiceStateGO)
				((ChoiceStateGO) children.get(i)).moveCenter(new Point(0,y));
			if(children.get(i) instanceof FinalStateGO)
				((FinalStateGO) children.get(i)).moveCenter(new Point(0,y));
		}*/
	}
	
	private boolean testIfFree(Point target_upperleft, Point target_lowerright) {
		
		Point current_upperleft = (Point) currentPosition.clone();
		Point current_lowerright = (Point) currentPosition.clone();
		
		current_upperleft.translate(-getWidth()/2, -getHeight()/2);
		current_lowerright.translate(getWidth()/2, getHeight()/2);
		
		if(component.getParent() instanceof OutermostRegion) {
			pc.getWorkPanel().getGrid().releaseBlock(current_upperleft, current_lowerright);
			boolean isfree = pc.getWorkPanel().getGrid().isFree(target_upperleft, target_lowerright);
			pc.getWorkPanel().getGrid().isFree(target_upperleft, target_lowerright);
			pc.getWorkPanel().getGrid().allocateBlock(current_upperleft, current_lowerright);
			return isfree;
		}
		else {
			parentGO.getGrid().releaseBlock(current_upperleft, current_lowerright);
			boolean isfree = parentGO.getGrid().isFree(target_upperleft, target_lowerright);
			parentGO.getGrid().isFree(target_upperleft, target_lowerright);
			parentGO.getGrid().allocateBlock(current_upperleft, current_lowerright);
			return isfree;
		}
	}
	
	public void fitToGrid() {
		if(moved) {

			Point target_upperleft = (Point) component.getPosition().clone();
			Point target_lowerright = (Point) component.getPosition().clone();
			
			target_upperleft.translate(-getWidth()/2, -getHeight()/2);
			target_lowerright.translate(getWidth()/2, getHeight()/2);
			
			if(!testIfFree(target_upperleft, target_lowerright)) {
				pc.sendMessage("Zielposition besetzt.", true);
				component.setPosition(currentPosition);
				moved = false;
				return;
			}
			
			
			int cols_difference = 0;
			int rows_difference = 0;		
			
			// links
			if(resize_left) {
				gridcols = grid.getCols();
				cols_difference = (this.getWidth() / gridsize) + 1 - gridcols;				
				resizeGridLeft(cols_difference);
				Point position = new Point(oldRelativeCenter.x - cols_difference * gridsize / 2,
						oldRelativeCenter.y);
				this.setRelativePosition(position);
				resize_left = false;
				
				this.setCenter(this.getCenter());
				
				for(SubRegion subregion: subregions) {
					subregion.getPosition().x = ((subregion.getPosition().x / gridsize)) * gridsize;
					subregion.getPosition().x += (((gridcols + 1) % 2) + 1) * gridsize / 2;
				}
			}
			
			// rechts
			else if(resize_right) {
				gridcols = grid.getCols();
				cols_difference = (this.getWidth() / gridsize) + 1 - gridcols;				
				resizeGridRight(cols_difference);
				Point position = new Point(oldRelativeCenter.x + cols_difference * gridsize / 2,
						oldRelativeCenter.y);
				this.setRelativePosition(position);
				resize_right = false;
				
				this.setCenter(this.getCenter());
				
				for(SubRegion subregion: subregions) {
					subregion.getPosition().x = ((subregion.getPosition().x / gridsize)) * gridsize;
					subregion.getPosition().x -= (((gridcols) % 2)) * gridsize / 2;
				}
			}
			
			// oben
			if(resize_top) {
				gridrows = grid.getRows();
				rows_difference = (this.getHeight() / gridsize) + 1 - gridrows;
				resizeGridTop(rows_difference);
				Point position = new Point(oldRelativeCenter.x,
						oldRelativeCenter.y - rows_difference * gridsize / 2);
				this.setRelativePosition(position);
				resize_top = false;
				
				this.setCenter(this.getCenter());
			}
			
			// unten
			else if(resize_bottom) {
				gridrows = grid.getRows();
				rows_difference = (this.getHeight() / gridsize) + 1 - gridrows;
				resizeGridBottom(rows_difference);
				Point position = new Point(oldRelativeCenter.x,
						oldRelativeCenter.y + rows_difference * gridsize / 2);
				this.setRelativePosition(position);
				resize_bottom = false;
				
				this.setCenter(this.getCenter());
			}
			
			
			this.setWidth(width);
			this.setHeight(height);
			this.setCenter(this.getCenter());
			
			Point current_upperleft = (Point) currentPosition.clone();
			Point current_lowerright = (Point) currentPosition.clone();
			current_upperleft.translate(-getWidth()/2, -getHeight()/2);
			current_lowerright.translate(getWidth()/2, getHeight()/2);
			
			target_upperleft = (Point) component.getPosition().clone();
			target_lowerright = (Point) component.getPosition().clone();
			target_upperleft.translate(-getWidth()/2, -getHeight()/2);
			target_lowerright.translate(getWidth()/2, getHeight()/2);
			
			if(component.getParent() instanceof OutermostRegion)
				pc.getWorkPanel().getGrid().moveBlock(current_upperleft, current_lowerright,
						target_upperleft, target_lowerright);
			else
				parentGO.getGrid().moveBlock(current_upperleft, current_lowerright,
						target_upperleft, target_lowerright);
	
			moved = false;
		
		}

	}
/*	
	public void removeFromGrid() {
		
		Point current_upperleft = (Point) currentPosition.clone();
		Point current_lowerright = (Point) currentPosition.clone();
		
		current_upperleft.translate(-getWidth()/2, -getHeight()/2);
		current_lowerright.translate(getWidth()/2, getHeight()/2);
		
		if(component.getParent() instanceof OutermostRegion) {
			pc.getWorkPanel().getGrid().releaseBlock(current_upperleft, current_lowerright);
			boolean isfree = pc.getWorkPanel().getGrid().isFree(target_upperleft, target_lowerright);
			pc.getWorkPanel().getGrid().isFree(target_upperleft, target_lowerright);
			pc.getWorkPanel().getGrid().allocateBlock(current_upperleft, current_lowerright);
			return isfree;
		}
		else {
			parentGO.getGrid().releaseBlock(current_upperleft, current_lowerright);
			boolean isfree = pc.getWorkPanel().getGrid().isFree(target_upperleft, target_lowerright);
			parentGO.getGrid().isFree(target_upperleft, target_lowerright);
			parentGO.getGrid().allocateBlock(current_upperleft, current_lowerright);
			return isfree;
		}
	}*/
	
	public Point getFreePosition(Point position) {
		
		int x = position.x - this.getCenter().x;
		int y = position.y - this.getCenter().y;
		
		Point relativePosition = new Point(x,y);
		
		int xOffset = gridcols * gridsize / 2;//(int) ((((double) gridcols) / 2.0) * gridsize);
		int yOffset = gridrows * gridsize / 2;//(int) ((((double) gridrows) / 2.0) * gridsize);
		
		x = relativePosition.x + xOffset;
		y = relativePosition.y + yOffset;
		
		Point bounds = getSubregionBounds(position);
		int subregion_offset = this.getCenter().x - this.getSubRegion(position).getAbsolutePosition().x;
		
		//pc.sendMessage("x: " + x + ", y: " + y, false);
		//pc.sendMessage("bounds: " + bounds, false);
		
		Point freePosition = this.grid.getFreePosition(new Point(x,y), bounds);
		
		if(freePosition != null) {
			freePosition.x -= xOffset;
			freePosition.x += subregion_offset;
			freePosition.y -= yOffset;
		}

		return freePosition;
	}
	
	
	private Point getSubregionBounds(Point position) {
		if(subregions.isEmpty()) {
			return null;
		}
		
		SubRegion left = null, current = null;
		
		for(int i = 0; i < subregions.size(); i++) {
			if(position.x - subregions.get(i).getAbsolutePosition().x < 0) {
				current = subregions.get(i);
				if(i > 0)
					left = subregions.get(i-1);
				break;
			}
		}
		
		int left_bound = 0, right_bound = gridcols - 1;
		
		if(current != null)
			right_bound = ((current.getPosition().x + (gridcols * gridsize / 2)) / gridsize) - 1;
		if(left != null)
			left_bound = (left.getPosition().x + (gridcols * gridsize / 2)) / gridsize;
		
		return new Point(left_bound, right_bound);
	}
	
	private int getSubregionsLeftBound(Point position) {
		if(subregions.isEmpty()) {
			//pc.sendMessage(":::leftBound:::", true);
			return 0;
		}
		
		SubRegion left = null;
		
		for(int i = 0; i < subregions.size(); i++) {
			if(position.x - subregions.get(i).getPosition().x < 0) {
				if(i > 0)
					left = subregions.get(i-1);
				break;
			}
		}
		if(left != null) {
			//pc.sendMessage("leftBound: " + left.getPosition(), false);
			return left.getPosition().x;
		}
		
		//pc.sendMessage("leftBound: " + (-(gridcols * gridsize / 2)), false);
		return -(gridcols * gridsize / 2);
	}
	
	
	public int getLayer() {
		return layer;
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public LinkedList<SubRegion> getSubRegions() {
		return subregions;
	}
	
	public void increaseLayer() {
		layer++;
		this.fitToGrid();
		if(parentGO != null)
			parentGO.increaseLayer();
	}
	
	public void delete() {
		Point upperleft = getCenter();
		Point lowerright = getCenter();
		upperleft.translate(-getWidth()/2, -getHeight()/2);
		lowerright.translate(getWidth()/2, getHeight()/2);
		if(component.getParent() instanceof OutermostRegion) {
			pc.getWorkPanel().getGrid().releaseBlock(upperleft, lowerright);
		}
		// TODO wenn nicht in OutermostRegion liegt
/*		else {
			grid.releaseBlock(upperleft, lowerright);
		}*/
	}
	
	private boolean isStartState() {
		return (component.equals(pc.getWorkPanel().getCSM().region.getStartState()));
	}
	
	protected void paintLabel() {
		label.setText(component.getName());
		label.setToolTipText(component.getName());
		int x = (this.getCenter().x - width/2);
		int y = (this.getCenter().y - height/2);
		label.setBounds(x, y-fontSize-2, getWidth(), fontSize+2);
		label.setVisible(pc.ops.coL);
	}
	
	protected void setLabel() {
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		label = new JLabel(component.getName());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setToolTipText(component.getName());
		label.setFont(font);
		label.setBorder(this.labelBorder);
		label.setEnabled(true);
		pc.getWorkPanel().add(label);
	}
	
	
}


