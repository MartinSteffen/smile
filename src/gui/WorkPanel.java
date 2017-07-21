package gui;

import csm.exceptions.*;
import csm.statetree.*;
import csm.*;

import gui.graphicalobjects.*;
import gui.popups.WorkPanelPopup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

/**
 * <p>
 * Title: WorkPanel
 * </p>
 * 
 * <p>
 * Description: Diese Klasse realisiert unsere Arbeitsfläche.
 * </p>
 * @author hfab + sek + omi
 */
public class WorkPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4470037111589285207L;
	
	// Definition globaler Variablen
	private Point mousePosition, transitionStart, popupPosition;
	private LinkedList<GraphicalObject> graphicalObjects;
	private GraphicalObject currentGO = null, tempGO = null;
	private GraphicalObject transitionStartGO = null, transitionEndGO = null;
	private boolean isTransition = false;
	private boolean mousePressedMove = false, mousePressedResize = false;
	public boolean isChanged = false;
	private Controller pc;
	private Undo undo;
	private Cursor cursor = Cursor.getDefaultCursor();
	private File file;
	private CoreStateMachine csm;
	private OutermostRegion outermost;
	private String csmName;
	
	private int gridsize = 50;
	private int gridrows, gridcols;
	private Grid grid;

	
	/**
	 * Konstruktor unseres Workpanels, Initialisierung
	 * 
	 * @param pc
	 */
	public WorkPanel(Controller pc, String name) {
		this.pc = pc;
		file = null;
		csmName = name;
		csm = new CoreStateMachine();
		outermost = csm.region;
		outermost.setName("OutermostRegion");
		graphicalObjects = new LinkedList<GraphicalObject>();
		undo = new Undo(this);
		addMouseMotionListener(new MouseMotionListener());
		addMouseListener(new MouseActionListener());
		setBackground(Color.WHITE);
		setLayout(null);
		pc.getElementList().refreshTree(csm);
		
		// Anzahl der Zeilen und Spalten bestimmen
		gridrows = 5000 / gridsize;//this.getHeight()/gridsize;
		gridcols = 5000 / gridsize;//this.getWidth()/gridsize;
		
		// Groesse des WorkPanel entsprechend anpassen 
		this.setSize(gridcols*gridsize,gridrows*gridsize);
		
		// Speicherfeld fuer die einzelnen Zellen
		grid = new Grid(gridrows, gridcols, gridsize);
	}
	/**
	 * Diese Klasse sorgt für das Zeichnen der States im Workpanel.
	 * Je nachdem, welcher Button ausgewählt ist, wird der entsprechende
	 * State a) dem WorkPpanel als grafisches Objekt und b) der CSM
	 * hinzugefügt.
	 * 
	 * @param point Die Koordinate, die im WorkPanel geklickt wurde.
	 */
	public void leftClick(Point point) {
		switch (pc.getAction()) {
		case Controller.ENTRY: {
			isTransition = false;
			addInternalState(point, GraphicalObject.ENTRYSTATE);
			break;
		}
		case Controller.EXIT: {
			isTransition = false;
			addInternalState(point, GraphicalObject.EXITSTATE);
			break;
		}
		case Controller.CHOICE: {
			isTransition = false;
			addInternalState(point, GraphicalObject.CHOICESTATE);
			break;
		}
		case Controller.FINAL: {
			isTransition = false;
			addInternalState(point, GraphicalObject.FINALSTATE);
			break;
		}
		case Controller.COMPOSITE: {
			isTransition = false;
			addInternalState(point, GraphicalObject.COMPOSITESTATE);
			break;
		}
		case Controller.TRANSITION: {
			
				// "target"
				if (isTransition) {
					transitionEndGO = getSelectedObject(point);
					
					//Bestimme Komponenten und caste auf State.
					CSMComponent currentComponent = transitionEndGO.getComponent();
					State source = (State)transitionStartGO.getComponent();
					State target = (State)currentComponent;
					
				
					if(transitionEndGO != null) {
						try {
							// erstelle neue Transition, die automatisch eingefügt wird.
							Transition trans = new Transition(point, source, target);
							
							// zeichne nur, wenn das einfügen geklappt hat.
							TransitionGO t = new TransitionGO(trans, transitionStartGO, transitionEndGO, pc);
							graphicalObjects.add(t);		
							
						}catch (ErrMayNotConnect e) {
							e.printStackTrace();
							pc.sendMessage("Selected states are not valid source and target states for a transition.", true);

						}
						
						isTransition = false;
						transitionStartGO = null;
						transitionEndGO = null;
					}
				}
				// "source"
				else {
					transitionStartGO = getSelectedObject(point);
					if(transitionStartGO != null)
						isTransition = true;
					//transitionStart = new Point(point);
				}
				isChanged = true;	
				break;
		}
		case Controller.MARK: {
			isTransition = false;
			GraphicalObject go = getSelectedObject(point);
			releaseMark();
			if(go != null)
				go.setMark(true);			
		}
		default:
			break;
		}
		// Einfügen in den Baum
		pc.getElementList().refreshTree(csm);
		this.repaint();
			
	}
	
	private void releaseMark() {
		for(GraphicalObject go : graphicalObjects) {
			go.setMark(false);
		}
	}
	
	// einen State in die CSM einfügen
	public void addInternalState(Point point, int type) {
		try {
			GraphicalObject go = getSelectedObject(point);
			StateGO state;
			CSMComponent currentComponent;
			// falls OutermostRegion
			if (go == null) {
				currentComponent = outermost;
				switch(type) {
				case(GraphicalObject.COMPOSITESTATE) : {
					point = grid.getFreePosition(point);
					CompositeState composite = new CompositeState(point);
					try{
					currentComponent.dropHere(composite);
					pc.sendMessage("Composite state successfully added.", false);
					}catch(ErrTreeNotChanged e){
						pc.sendMessage(e.getMessage(), true);
					}
					grid.insert(point);
					state = new CompositeStateGO(composite, null, pc);
					undo.push(Undo.ADD, state);
					graphicalObjects.add(state);
					break;
				} case(GraphicalObject.FINALSTATE) : {
					point = grid.getFreePosition(point);
					FinalState finalstate = new FinalState(point);
					try{
					currentComponent.dropHere(finalstate);
					pc.sendMessage("Final state successfully added.", false);
					}catch(ErrTreeNotChanged e){
						pc.sendMessage(e.getMessage(), true);
					}
					grid.insert(point);
					state = new FinalStateGO(finalstate, null, pc);
					undo.push(Undo.ADD, state);
					graphicalObjects.add(state);
					break;
				} case(GraphicalObject.CHOICESTATE) : {
					point = grid.getFreePosition(point);
					ChoiceState choicestate = new ChoiceState(point);
					try{
					currentComponent.dropHere(choicestate);
					pc.sendMessage("Choice state successfully added.", false);
					}catch(ErrTreeNotChanged e){
						pc.sendMessage(e.getMessage(), true);
					}
					grid.insert(point);
					state = new ChoiceStateGO(choicestate, null, pc);
					undo.push(Undo.ADD, state);
					graphicalObjects.add(state);
					break;
				}
				default:
					break;
				}
			}
			// in einer Subregion weiterzeichnen 
			else {
				if(go.getType() == GraphicalObject.COMPOSITESTATE) {
					CompositeStateGO cs = (CompositeStateGO) go;
					switch(type) {
					case(GraphicalObject.ENTRYSTATE): {
						currentComponent = go.getComponent();
						point = cs.getSidePosition(point);
						EntryState entry = new EntryState(point);
						
						//in die CSM einfügen und Exc. abfangen
						try{
							currentComponent.dropHere(entry);
							pc.sendMessage("Entry state successfully added.", false);
						}catch(ErrTreeNotChanged e){
							pc.sendMessage(e.getMessage(), true);
						}
						state = new EntryStateGO(entry, cs, pc);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						break;
					}
					case(GraphicalObject.EXITSTATE): {
						currentComponent = go.getComponent();
						point = cs.getSidePosition(point);
						ExitState exit = new ExitState(point);
						
						// in die CSM einfügen und Exc. abfangen
						try{
							currentComponent.dropHere(exit);
							pc.sendMessage("Exit state successfully added.", false);
						}catch(ErrTreeNotChanged e){
							pc.sendMessage(e.getMessage(), true);
						}
						state = new ExitStateGO(exit, cs, pc);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						break;
					}
					case(GraphicalObject.COMPOSITESTATE): {
						CompositeStateGO parentGO = (CompositeStateGO) go;
						currentComponent = parentGO.getSubRegion(point);
						if(currentComponent == null) {
							pc.sendMessage("keine SubRegion gefunden", true);
							break;
						}		
						point = parentGO.getFreePosition(point);
						pc.sendMessage("Composite state successfully added.", false);
						if(point == null) {
							pc.sendMessage("kein Platz für CompositeState", true);
							break;
						}
						// csm
						CompositeState compositestate = new CompositeState(point);
						// in die CSM einfügen und Exc. abfangen
						try{
							currentComponent.dropHere(compositestate);
						}catch(ErrTreeNotChanged e){
							pc.sendMessage(e.getMessage(), true);
						}
						// gfx
						state = new CompositeStateGO(compositestate, parentGO, pc);
						((CompositeStateGO) go).dropHere2((CompositeStateGO) state);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						break;
						
					}
					case(GraphicalObject.CHOICESTATE) : {
						CompositeStateGO parentGO = (CompositeStateGO) go;
						currentComponent = parentGO.getSubRegion(point);
						if(currentComponent == null) {
							pc.sendMessage("keine SubRegion gefunden", true);
							break;
						}	
						point = parentGO.getFreePosition(point);
						if(point == null) {
							pc.sendMessage("kein Platz für ChoiceState", true);
							break;
						}
						// csm
						ChoiceState choicestate = new ChoiceState(point);
						// in die CSM einfügen und Exc. abfangen
						try{
							currentComponent.dropHere(choicestate);
							pc.sendMessage("Choice state successfully added.", false);
						}catch(ErrTreeNotChanged e){
							pc.sendMessage(e.getMessage(), true);
						}
						// gfx
						state = new ChoiceStateGO(choicestate, parentGO, pc);
						((CompositeStateGO) go).dropHere2((ChoiceStateGO) state);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						break;
					}
					case(GraphicalObject.FINALSTATE) : {
						CompositeStateGO parentGO = (CompositeStateGO) go;
						currentComponent = parentGO.getSubRegion(point);
						if(currentComponent == null) {
							pc.sendMessage("keine SubRegion gefunden", true);
							break;
						}	
						point = parentGO.getFreePosition(point);
						if(point == null) {
							pc.sendMessage("kein Platz für FinalState", true);
							break;
						}
						// csm
						FinalState finalstate = new FinalState(point);
						// in die CSM einfügen und Exc. abfangen
						try{
							currentComponent.dropHere(finalstate);
							pc.sendMessage("Final state successfully added.", false);
						}catch(ErrTreeNotChanged e){
							pc.sendMessage(e.getMessage(), true);
						}
						// gfx
						state = new FinalStateGO(finalstate, parentGO, pc);
						((CompositeStateGO) go).dropHere2((FinalStateGO) state);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						break;
					}
					default:
						break;
					}
				}
				// Exit-State an FinalState zeichnen
				else {
					if(go.getType() == GraphicalObject.FINALSTATE &&
							type == GraphicalObject.EXITSTATE) {
						FinalStateGO fs = (FinalStateGO) go;
						currentComponent = go.getComponent();
						point = fs.getSidePosition(point);
						ExitState exit = new ExitState(point);
						currentComponent.dropHere(exit);
						pc.sendMessage("Exit state successfully added.", false);
						state = new ExitStateGO(exit, fs, pc);
						undo.push(Undo.ADD, state);
						graphicalObjects.add(state);
						}
				}
			}
			pc.getElementList().refreshTree(csm);
			isChanged = true;
			this.repaint();
		} catch (ErrTreeNotChanged e) {
			pc.sendMessage(e.getMessage(), true);
		}
	}
	
	/**
	 * Ruft ein Pop-Up-Menü an der geklickten Stelle auf,
	 * abhänging vom State, der sich dort befindet.
	 * 
	 */

	public void rightClick(Point point) {
		mousePosition = point;
		popupPosition = point;
		WorkPanelPopup popup = null;
		currentGO = getSelectedObject(point);
		popup = new WorkPanelPopup(this, currentGO);
		popup.show(this, point.x, point.y);
		isChanged = true;
	}
	
	
	public void mousePressed(Point point) {
		mousePosition = (Point) point.clone();	// speichern der Mausposition beim druecken
		if(pc.getAction() == Controller.MOVERESIZE) {
			currentGO = getSelectedObject(point);
			if(currentGO != null) {
				switch(currentGO.getType()) {
				case(GraphicalObject.COMPOSITESTATE): {
					CompositeStateGO CompGO = (CompositeStateGO) currentGO;
					Point center = CompGO.getCenter();
					int dx = Math.abs(point.x-center.x);
					int dy = Math.abs(point.y-center.y);
					if ( (Math.abs(dx-CompGO.getWidth()/2) < 8) ||
							(Math.abs(dy-CompGO.getHeight()/2) < 8)) {
						mousePressedMove = false;
						mousePressedResize = true;
					}
					else {
						mousePressedResize = false;
						mousePressedMove = true;
						this.setCursor(cursor = new Cursor(Cursor.MOVE_CURSOR));
					}
					break;
				}
				case(GraphicalObject.CHOICESTATE): {
					mousePressedMove = true;
					this.setCursor(cursor = new Cursor(Cursor.MOVE_CURSOR));
					break;
				}
				case(GraphicalObject.FINALSTATE): {
					mousePressedMove = true;
					this.setCursor(cursor = new Cursor(Cursor.MOVE_CURSOR));
					break;
				}
				default:
					break;
				}
			}
		}
	}
	
	public void mouseDragged(Point point) {
		if(pc.getAction() == Controller.MOVERESIZE) {
			
			// Vergroessern/verkleinern eines States 
			if(mousePressedResize && currentGO.getType() == GraphicalObject.COMPOSITESTATE) {
				CompositeStateGO CompGO = (CompositeStateGO) currentGO;
				Point center = CompGO.getCenter();
				int dx = Math.abs(point.x-center.x);
				int dy = Math.abs(point.y-center.y);
				if (Math.abs(dx-CompGO.getWidth()/2) < 8) {
					if (Math.abs(dy-CompGO.getHeight()/2) < 8) {
						if (point.x > center.x) {
							if ( point.y > center.y) {
								CompGO.resizeRight(dx-CompGO.getWidth()/2);
								CompGO.resizeBottom(dy-CompGO.getHeight()/2);
							}
							else {
								CompGO.resizeRight(dx-CompGO.getWidth()/2);
								CompGO.resizeTop(dy-CompGO.getHeight()/2);
							}
						}
						else
							if ( point.y > center.y) {
								CompGO.resizeLeft(dx-CompGO.getWidth()/2);
								CompGO.resizeBottom(dy-CompGO.getHeight()/2);
							}
							else {
								CompGO.resizeLeft(dx-CompGO.getWidth()/2);
								CompGO.resizeTop(dy-CompGO.getHeight()/2);
							}
					}
					else{
						if (point.x > center.x)
							CompGO.resizeRight(dx-CompGO.getWidth()/2);
						else
							CompGO.resizeLeft(dx-CompGO.getWidth()/2);
					}
				}
				else if (Math.abs(dy-CompGO.getHeight()/2) < 8) {
					if (point.y > center.y)
						CompGO.resizeBottom(dy-CompGO.getHeight()/2);
					else
						CompGO.resizeTop(dy-CompGO.getHeight()/2);
				}
				this.repaint();
			}
			
			// Verschieben eines States
			if(mousePressedMove && pc.getAction() == Controller.MOVERESIZE) {
				
				int x = point.x - mousePosition.x;
				int y = point.y - mousePosition.y;
				
				if(currentGO.getType() == GraphicalObject.COMPOSITESTATE)
					((CompositeStateGO) currentGO).moveCenter(new Point (x,y));
				else if(currentGO.getType() == GraphicalObject.CHOICESTATE)
					((ChoiceStateGO) currentGO).moveCenter(new Point (x,y));
				else if(currentGO.getType() == GraphicalObject.FINALSTATE)
					((FinalStateGO) currentGO).moveCenter(new Point (x,y));
				this.repaint();
				
				mousePosition.x = point.x;
				mousePosition.y = point.y;
			}
		}
	}
	
	
	public void mouseReleased(Point point) {
		if(mousePressedMove)
			this.setCursor(cursor = new Cursor(Cursor.DEFAULT_CURSOR));
		if(mousePressedMove || mousePressedResize)
			for(int i=0; i<graphicalObjects.size(); i++)
				graphicalObjects.get(i).fitToGrid();
		mousePressedMove = false;
		mousePressedResize = false;
		currentGO = null;
		repaint();
	}
	
	
	private GraphicalObject getSelectedObject(Point point) {
		ListIterator iter = graphicalObjects.listIterator();
		
		GraphicalObject selectedObject;
		int selectedObjectSize = 0;

		// temporaeres Objekt fuer Vergleich in Schleife
		GraphicalObject nextObject;
		int nextObjectSize = 0;
		
		// iteriere, falls Liste nicht leer
		if(iter.hasNext()) {
			// erste Objekt waehlen und Groesse bestimmen, falls dort geklickt
			selectedObject = (GraphicalObject) iter.next();
			selectedObjectSize = selectedObject.sizeIfClicked(point);
	
			nextObjectSize = 0;

			/* Iteriere ueber Liste und finde geklicktes Objekt mit kleinstem
			 * Flaecheninhalt. Naiver Ansatz, um anhand der grafischen
			 * Darstellung das oberste geklickte Objekt zu finden.
			 * Die Implementierung von sizeIfClicked gibt Transitionen Vorrang,
			 * wenn sie sich mit einem anderen GraphicalObject ueberlagern
			 * (da bei Transitionen als Flaecheninhalt maximal 1 ausgegeben
			 * wird).
			 */
			while(iter.hasNext()) {
				nextObject = (GraphicalObject) iter.next();
				nextObjectSize = nextObject.sizeIfClicked(point);
				
				// Verschieben von Transitionen deaktiviert
/*				if(nextObjectSize == 1)
					continue;*/
				
				if(nextObjectSize != 0) {
					if(selectedObjectSize == 0 || nextObjectSize <= selectedObjectSize) {
						selectedObject = nextObject;
						selectedObjectSize = nextObjectSize;
					}
				}
			}
			if(selectedObjectSize > 0)
				return selectedObject;
		}
		/* Wenn Liste leer war oder kein Objekt geklickt wurde,
		 * gib null (=OutermostRegion) zurueck.*/
		 
		return null;
	}
	
	// Gibt eine Liste aller markierten Objekte zurück
	public LinkedList<GraphicalObject> getSelectedObjects() {
		LinkedList<GraphicalObject> temp = new LinkedList<GraphicalObject>();
		for(GraphicalObject go : graphicalObjects) {
			if(go.isMarked())
				temp.add(go);
		}
		return temp;
	}
	
	// Löscht eine Liste von Objekten
	public void delete(LinkedList<GraphicalObject> gos) {
		LinkedList<GraphicalObject> deleted = new LinkedList<GraphicalObject>();
		try {
			if(gos == null || gos.isEmpty()) ;
			else if(gos.size() == 1) {
				GraphicalObject go = gos.getFirst();
				go.getComponent().getParent().remove(go.getComponent());
				deleted.addFirst(go);
				graphicalObjects.remove(go);
				freeGrid(go);
				remove(go.getLabel());
				sendMessage("Object: " + go.getComponent().getName() + " deleted.", false);
			} else {
			LinkedList<GraphicalObject> gos2 = new LinkedList<GraphicalObject>(gos);	
			LinkedList<TransitionGO> trans = new LinkedList<TransitionGO>();
			LinkedList<GraphicalObject> comps = new LinkedList<GraphicalObject>();
			for(GraphicalObject go : gos2) {
				if(go.getType() == GraphicalObject.TRANSITION) {
					trans.add((TransitionGO)go);
					gos.remove(go);
				}
				if(go.getType() == GraphicalObject.COMPOSITESTATE || go.getType() == GraphicalObject.FINALSTATE) {
					comps.add(go);
					gos.remove(go);
				}
			}
			for(TransitionGO tran : trans) {
				tran.getComponent().getParent().remove(tran.getComponent());
				deleted.addFirst(tran);
				graphicalObjects.remove(tran);
				remove(tran.getLabel());
				sendMessage("Transition from " + ((Transition)tran.getComponent()).getSource().getName() + " to " + ((Transition)tran.getComponent()).getTarget().getName() + "deleted.", false);
			}
			for(GraphicalObject go : gos) {
				go.getComponent().getParent().remove(go.getComponent());
				deleted.addFirst(go);
				graphicalObjects.remove(go);
				freeGrid(go);
				remove(go.getLabel());
				sendMessage("State: " + go.getComponent().getName() + " deleted at " + go.getComponent().getAbsolutePosition().x + "/" + go.getComponent().getAbsolutePosition().y, false);
			}
			for(GraphicalObject go : comps) {
				if(go.getType() == GraphicalObject.COMPOSITESTATE) 
					for(SubRegion sub : ((CompositeStateGO)go).getSubRegions()) {
						sub.getParent().remove(sub);
						sendMessage("SubRegion: " + sub.getName() + " deleted at " + sub.getAbsolutePosition().x + "/" + sub.getAbsolutePosition().y, false);
					}
				go.getComponent().getParent().remove(go.getComponent());
				deleted.addFirst(go);
				graphicalObjects.remove(go);
				freeGrid(go);
				remove(go.getLabel());
				sendMessage("State: " + go.getComponent().getName() + " deleted at " + go.getComponent().getAbsolutePosition().x + "/" + go.getComponent().getAbsolutePosition().y, false);
			}
			}
			// undoAction
			pc.getElementList().refreshTree(csm);
			this.repaint();
			isChanged = true;
			
		} catch (ErrTreeNotChanged e) {
			sendMessage(e.getMessage(), true);
		}
	}
	
	private void freeGrid(GraphicalObject go) {
		if(go != null) {
			if(go instanceof ChoiceStateGO) {
				Point position = ((ChoiceStateGO) go).getCenter();
				grid.releaseBlock(position, position);
			}
			else if(go instanceof FinalStateGO) {
				Point position = ((FinalStateGO) go).getCenter();
				grid.releaseBlock(position, position);
			}
			else if(go instanceof CompositeStateGO) {
				CompositeStateGO cs = (CompositeStateGO) go;
				Point upperleft = cs.getCenter();
				Point lowerright = cs.getCenter();
				upperleft.translate(-cs.getWidth()/2, -cs.getHeight()/2);
				lowerright.translate(cs.getWidth()/2, cs.getHeight()/2);
				grid.releaseBlock(upperleft, lowerright);
			}
		}
	}
	
	public void mousePosition(Point point) {
		// fuer das Setzen des passenden Cursors verantwortlich
		if(pc.getAction() == Controller.MOVERESIZE) {
			tempGO = getSelectedObject(point);
			if( tempGO != null) {
				if(tempGO.getType() == GraphicalObject.COMPOSITESTATE) {
					CompositeStateGO CompGO = (CompositeStateGO) tempGO;
					Point center = CompGO.getCenter();
					int dx = Math.abs(point.x-center.x);
					int dy = Math.abs(point.y-center.y);
					if (Math.abs(dx-CompGO.getWidth()/2) < 8) {
						if (Math.abs(dy-CompGO.getHeight()/2) < 8) {
							if (point.x > center.x) {
								if (point.y > center.y)
									this.setCursor(cursor = new Cursor(Cursor.NW_RESIZE_CURSOR));
								else
									this.setCursor(cursor = new Cursor(Cursor.SW_RESIZE_CURSOR));
							}
							else
								if (point.y > center.y)
									this.setCursor(cursor = new Cursor(Cursor.NE_RESIZE_CURSOR));
								else
									this.setCursor(cursor = new Cursor(Cursor.SE_RESIZE_CURSOR));
						}
						else{
							if (point.x > center.x)
								this.setCursor(cursor = new Cursor(Cursor.E_RESIZE_CURSOR));				
							else
								this.setCursor(cursor = new Cursor(Cursor.W_RESIZE_CURSOR));
						}
					}
					else if (Math.abs(dy-CompGO.getHeight()/2) < 8) {
						if (point.y > center.y)
							this.setCursor(cursor = new Cursor(Cursor.N_RESIZE_CURSOR));
						else
							this.setCursor(cursor = new Cursor(Cursor.S_RESIZE_CURSOR));
					}
					else
						this.setCursor(Cursor.getDefaultCursor());
				}
			}
			else
				this.setCursor(Cursor.getDefaultCursor());
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		if(pc.gridOn) {
			// Raster zeichnen
			Graphics2D g2d = (Graphics2D) g;
			Color currentColor = g2d.getColor();
			g2d.setPaint(Color.LIGHT_GRAY);		
			for(int i = 1; i < gridrows; i++) {
				g2d.drawLine(0, i*gridsize, this.getWidth(), i*gridsize);
			}
			for(int i = 1; i < gridcols; i++) {
				g2d.drawLine(i*gridsize, 0, i*gridsize, this.getHeight());
			}
			g2d.setPaint(currentColor);
		}		
		
		
		for(int i=0; i<graphicalObjects.size(); i++)
			((StateGO)graphicalObjects.get(i)).paint(g);
		
		Component[] labels = getComponents();
		for(Component component : labels) {	
			remove(component);
			add(component);
		}
		validate();
	}
	
	
	public void printVariables() {
		//private GraphicalObject currentGO = null, tempGO = null;
		//private GraphicalObject transitionStartGO = null, transitionEndGO = null;
		System.out.println("\nVARIABLES:");
		if(mousePosition != null)
			System.out.println("mousePosition : " + mousePosition.toString());
		if(transitionStart != null)
			System.out.println("transitionStart : " + transitionStart.toString());
		if(popupPosition != null)
			System.out.println("popupPosition : " + popupPosition.toString());
		if(isTransition)
			System.out.println("isTransition : true");
		else
			System.out.println("isTransition : false");
		if(mousePressedMove)
			System.out.println("mousePressedMove : true");
		else
			System.out.println("mousePressedMove : false");
		if(mousePressedResize)
			System.out.println("mousePressedResize : true");
		else
			System.out.println("mousePressedResize : false");
		if(isChanged)
			System.out.println("isChanged : true");
		else
			System.out.println("isChanged : false");
		System.out.println("cursor : " + cursor.getName());
		System.out.println();
	}
	
	public CoreStateMachine getCSM() {
		return this.csm;
	}
	
	public void setCSM(CoreStateMachine csm) {
		this.csm = csm;
		outermost = csm.region;
		new GuiLoader(csm, this, pc);
		this.repaint();
	}
	
	public void addSubregion() {
		((CompositeStateGO) currentGO).addSubregion(mousePosition);		
		pc.getElementList().refreshTree(csm);
		this.repaint();
	}
	
	public void addObject(GraphicalObject go) {
		this.graphicalObjects.add(go);
	}
	
	public GraphicalObject getGraphicalObjectByComponent(CSMComponent component) {
		for(GraphicalObject go : graphicalObjects) {
			if(component.equals(go.getComponent()))
				return go;
		}
		return null;
	}
	
	public boolean isValidName(String name) {
		for(GraphicalObject go: graphicalObjects)
			if (go.getName().equals(name))
				return false;
		return true;
	}
	
	public void changeElementList() {
		pc.getElementList().refreshTree(csm);
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void sendMessage(String message, boolean error) {
		pc.sendMessage(message, error);
	}
	
	public String getCSMName() {
		return csmName;
	}
	
	public void setCSMName(String name) {
		csmName = name;
		pc.setCSMName(name);
	}
	
	public File getFile() {
		return file;
	}
	
	public int getGridSize() {
		return gridsize;
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public LinkedList<GraphicalObject> getGOs() {
		return new LinkedList<GraphicalObject>(graphicalObjects);
	}
}
