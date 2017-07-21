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
import csm.statetree.Transition;

/**
 * TransitionGO erweitert StateGO und repräsentiert die grafischen Objekte für
 * Transitionen.
 * 
 * @author sek
 * 
 */

public class TransitionGO extends StateGO {

	private GraphicalObject sourceGO, destGO;

	private int shapeSource = 0, shapeDest = 0;

	private Point source, destination;

	private boolean isComposite = false;

	private boolean isChoice = false;

	private Point[] startToEnd;

	public static int count = -1;

	// grafische Darstellung der Zustaende als Bitmuster codiert:
	private static final int SOURCECIRCLE = 1; // Source: Entry, Exit, Final

	private static final int SOURCEDIAMOND = 2; // Source: Choice

	private static final int SOURCERECTANGLE = 4; // Source: Composite

	private static final int DESTCIRCLE = 8; // Destination: Entry, Exit,
												// Final

	private static final int DESTDIAMOND = 16; // Destination: Choice

	private static final int DESTRECTANGLE = 32; // Destination: Composite

	/**
	 * default Konstruktor
	 * 
	 */
	public TransitionGO(Controller pc) {
		super(pc);
		type = GraphicalObject.TRANSITION;
	}

	/**
	 * Konstruktor (mit Name)
	 * 
	 * @param component -
	 *            zugeordneter CSMComponent (Transition)
	 * @param name -
	 *            Name
	 * @param sourceGO -
	 *            Startzustand der Transition
	 * @param destGO -
	 *            Endzustand der Transition
	 */
	public TransitionGO(CSMComponent component, String name,
			GraphicalObject sourceGO, GraphicalObject destGO, Controller pc) {
		super(pc);
		type = GraphicalObject.TRANSITION;

		this.sourceGO = sourceGO;
		this.destGO = destGO;

		this.component = component;
		this.component.setName(name);
		count++;
		setLabel();
	}

	/**
	 * Konstruktor (ohne Name)
	 * 
	 * @param component -
	 *            zugeordneter CSMComponent (Transition)
	 * @param sourceGO -
	 *            Startzustand der Transition
	 * @param destGO -
	 *            Endzustand der Transition
	 */
	public TransitionGO(CSMComponent component, GraphicalObject sourceGO,
			GraphicalObject destGO, Controller pc) {
		super(pc);
		type = GraphicalObject.TRANSITION;

		this.sourceGO = sourceGO;
		this.destGO = destGO;

		this.component = component;
		count++;
		generateName();
		setLabel();

	}

	// Methode zur automatischen Namenserzeugung
	protected void generateName() {
		this.component.setName("t" + count);
	}

	/*
	 * public void setSource(Point s) { this.source = s; }
	 * 
	 * public void setDestination(Point d) { this.destination = d; }
	 * 
	 * public Point getSource() { return this.source; }
	 * 
	 * public Point getDestination() { return this.destination; }
	 */

	// Zeichenmethode
	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		if (mark) {
			g2d.setPaint(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(stroke + 1));
		} else {
			g2d.setStroke(new BasicStroke(stroke));
			g2d.setPaint(this.color);
		}

		// Wichtige Punkte für die Zeichenmethoden
		startToEnd = getStartAndEndPoint();

		// Berechnung der Pfeilspitze der Transition
		// Point[] coords = this.arrowheadCoords(startToEnd[0], startToEnd[1]);

		if (isComposite) {
			CompositeStateGO cs = (CompositeStateGO) sourceGO;
			int x = cs.getCenter().x - (cs.getWidth() / 2) - 10;
			int y = cs.getCenter().y + (cs.getHeight() / 2) - 10;

			destination = new Point(x, y);

			// Bogen für CompositeState zu sich selbst
			g2d.drawArc(x, y, 20, 20, 0, -270);

			// Pfeilspitze, wahlweise gefüllt oder nicht
			g2d.drawLine(x + 10, y - 1, x + 3, y - 1);
			g2d.drawLine(x + 10, y - 1, x + 5, y + 7);

			// int[] xPoints = {x+10, x+3, x+5};
			// int[] yPoints = {y-1, y-1, y + 7};
			// g2d.fillPolygon(xPoints, yPoints, 3);

			isComposite = false;

		} else if (isChoice) {
			ChoiceStateGO cs = (ChoiceStateGO) sourceGO;
			int x = cs.getCenter().x - 9;
			int y = cs.getCenter().y + cs.getRadius() - 9;

			destination = new Point(x, y);

			// Bogen für CompositeState zu sich selbst
			g2d.drawArc(x, y, 20, 20, 40, -265);

			// Pfeilspitze, wahlweise gefüllt oder nicht
			g2d.drawLine(x + 1, y + 2, x - 5, y + 6);
			g2d.drawLine(x + 1, y + 2, x + 3, y + 10);

			// int[] xPoints = {x+1, x-5, x+3};
			// int[] yPoints = {y+2, y+6, y +10};
			// g2d.fillPolygon(xPoints, yPoints, 3);

			isChoice = false;

		} else {
			// Standardpfeile
			Point[] coords = this.arrowheadCoords(startToEnd[0], startToEnd[1]);
			g2d.drawLine(startToEnd[0].x, startToEnd[0].y, startToEnd[1].x,
					startToEnd[1].y);

			// Standardpfeilspitzen
			g2d.drawLine(startToEnd[1].x, startToEnd[1].y, coords[0].x,
					coords[0].y);
			g2d.drawLine(startToEnd[1].x, startToEnd[1].y, coords[1].x,
					coords[1].y);

			// int[] xPoints = {startToEnd[1].x, coords[0].x, coords[1].x};
			// int[] yPoints = {startToEnd[1].y, coords[0].y, coords[1].y};
			// g2d.fillPolygon(xPoints, yPoints, 3);
		}
		paintLabel();
	}

	/**
	 * Statt "Kurven" werden Kantenzüge für einige Transitionen benutzt, diese
	 * Methode errechnet die Anzahl der nötigen Stützpunkte. edit:
	 * wahrscheinlich überflüssig :(
	 * 
	 * @return Anzahl der nötigen Punkte zum Zeichnen
	 */
	private int getNPoints() {

		// Standardtransition besitzt zwei Punkte, source und target.
		int nPoints = 2;

		// bei gleichem Start und Ziel verwende Kantenzug mit 5 Punkten.
		// trifft für Composite- und ChoiceStates zu.
		if (sourceGO.getComponent() == destGO.getComponent()) {
			nPoints = 5;
		}
		// Start und Ziel sind Entry- und Exitstate am selben Compositestate.
		if (sourceGO.getType() == GraphicalObject.EXITSTATE
				&& destGO.getType() == GraphicalObject.ENTRYSTATE
				&& destGO.getComponent().getParent() == sourceGO.getComponent()
						.getParent()) {
			nPoints = 6;
		}
		return nPoints;

	}

	/*
	 * Testen ob Objekt bei Mausklick angewaehlt Rueckgabe: Flaeche des
	 * grafischen Objektes (bei Transitionen := 1)
	 */
	public int sizeIfClicked(Point p) {
		if (sourceGO.equals(destGO)) {
			if (sourceGO instanceof CompositeStateGO) {
				if ((p.x - destination.x) > 0 && (p.x - destination.x) < 10
						&& (p.y - destination.y) > 0
						&& (p.y - destination.y) < 20)
					return 1;
				if ((p.x - destination.x) > 0 && (p.x - destination.x) < 20
						&& (p.y - destination.y) > 10
						&& (p.y - destination.y) < 20)
					return 1;
			} else { // ChoiceStateGO
				if ((p.x - destination.x) > 0 && (p.x - destination.x) < 20
						&& (p.y - destination.y) > 5
						&& (p.y - destination.y) < 20)
					return 1;
			}

		} else {
			int dist_source = (int) Math.floor(Math.sqrt(Math.pow(p.x
					- source.x, 2)
					+ Math.pow(p.y - source.y, 2)));
			int dist_dest = (int) Math.floor(Math.sqrt(Math.pow(p.x
					- destination.x, 2)
					+ Math.pow(p.y - destination.y, 2)));
			int length = (int) Math.ceil(Math.sqrt(Math.pow(source.x
					- destination.x, 2)
					+ Math.pow(source.y - destination.y, 2)));

			if ((dist_source + dist_dest) <= length + 1)
				return 1;
		}
		return 0;
	}

	/* Berechnung der Pfeilspitzen der Transition */
	private Point[] arrowheadCoords(Point start, Point end) {
		Point[] ret = new Point[2];
		ret[0] = new Point(0, 0);
		ret[1] = new Point(0, 0);

		double ankathete = (double) (end.x - start.x);
		double gegenkathete = (double) (end.y - start.y);
		double hypothenuse = Math.sqrt(ankathete * ankathete + gegenkathete
				* gegenkathete);
		double alpha = Math.asin(gegenkathete / hypothenuse);
		double offset = Math.PI / 3.0;

		if (end.x < start.x) {
			ret[0].x = new Double((double) end.x - 10.0
					* Math.sin(alpha - offset)).intValue();
			ret[0].y = new Double((double) end.y - 10.0
					* Math.cos(alpha - offset)).intValue();
			ret[1].x = new Double((double) end.x + 10.0
					* Math.sin(alpha + offset)).intValue();
			ret[1].y = new Double((double) end.y + 10.0
					* Math.cos(alpha + offset)).intValue();
		} else {
			ret[0].x = new Double((double) end.x - 10.0
					* Math.sin(alpha + offset)).intValue();
			ret[0].y = new Double((double) end.y + 10.0
					* Math.cos(alpha + offset)).intValue();
			ret[1].x = new Double((double) end.x + 10.0
					* Math.sin(alpha - offset)).intValue();
			ret[1].y = new Double((double) end.y - 10.0
					* Math.cos(alpha - offset)).intValue();
		}

		return ret;
	}

	/*
	 * In Abhangigkeit von der grafischen Darstellung des Start- und
	 * Endzustandes die genauen Start- und Endpunkte fuer das Zeichnen der
	 * Transitionen berechnen
	 */
	private Point[] getStartAndEndPoint() {

		Point start = new Point();
		Point end = new Point();
		Point ret[] = new Point[2];
		int radiusStart = 0;
		int radiusEnd = 0;
		int sourceWidth = 0, sourceHeight = 0, destWidth = 0, destHeight = 0;

		// Ueberpruefung des Startzustandes
		switch (sourceGO.getType()) {
		case (GraphicalObject.ENTRYSTATE): {
			EntryStateGO sourceEntryGO = (EntryStateGO) sourceGO;
			radiusStart = sourceEntryGO.getRadius();
			start = (Point) sourceEntryGO.getCenter().clone();
			shapeSource = SOURCECIRCLE;
			break;
		}
		case (GraphicalObject.EXITSTATE): {
			ExitStateGO sourceExitGO = (ExitStateGO) sourceGO;
			radiusStart = sourceExitGO.getRadius();
			start = (Point) sourceExitGO.getCenter().clone();
			shapeSource = SOURCECIRCLE;
			break;
		}
			// Finalstate ist niemals source -> kann gelöscht werden
		case (GraphicalObject.FINALSTATE): {
			FinalStateGO sourceFinalGO = (FinalStateGO) sourceGO;
			radiusStart = sourceFinalGO.getRadius();
			start = (Point) sourceFinalGO.getCenter().clone();
			shapeSource = SOURCECIRCLE;
			break;
		}
		case (GraphicalObject.CHOICESTATE): {
			ChoiceStateGO sourceChoiceGO = (ChoiceStateGO) sourceGO;
			radiusStart = sourceChoiceGO.getRadius();
			start = (Point) sourceChoiceGO.getCenter().clone();

			shapeSource = SOURCEDIAMOND;

			break;
		}
			// CompositeState kann nur mit sich selber verbunden sein.
		case (GraphicalObject.COMPOSITESTATE): {
			CompositeStateGO sourceCompGO = (CompositeStateGO) sourceGO;
			sourceWidth = sourceCompGO.getWidth();
			sourceHeight = sourceCompGO.getHeight();

			start = (Point) sourceCompGO.getCenter().clone();
			start.x = start.x - (sourceWidth / 2 + 8);
			start.y = start.y + sourceHeight / 2 - 8;
			isComposite = true;
			shapeSource = SOURCERECTANGLE;
			break;
		}
		default:
			break;
		}

		// Ueberpruefung des Endzustandes
		switch (destGO.getType()) {
		case (GraphicalObject.ENTRYSTATE): {
			EntryStateGO destEntryGO = (EntryStateGO) destGO;
			radiusEnd = destEntryGO.getRadius();
			end = (Point) destEntryGO.getCenter().clone();
			shapeDest = DESTCIRCLE;
			break;
		}
		case (GraphicalObject.EXITSTATE): {
			ExitStateGO destExitGO = (ExitStateGO) destGO;
			radiusEnd = destExitGO.getRadius();
			end = (Point) destExitGO.getCenter().clone();
			shapeDest = DESTCIRCLE;
			break;
		}
		case (GraphicalObject.FINALSTATE): {
			FinalStateGO destFinalGO = (FinalStateGO) destGO;
			radiusEnd = destFinalGO.getRadius();
			end = (Point) destFinalGO.getCenter().clone();
			shapeDest = DESTCIRCLE;
			break;
		}
		case (GraphicalObject.CHOICESTATE): {
			ChoiceStateGO destChoiceGO = (ChoiceStateGO) destGO;
			radiusEnd = destChoiceGO.getRadius();
			end = (Point) destChoiceGO.getCenter().clone();
			shapeDest = DESTDIAMOND;
			break;
		}
		case (GraphicalObject.COMPOSITESTATE): {
			CompositeStateGO destCompGO = (CompositeStateGO) destGO;
			destWidth = destCompGO.getWidth();
			destHeight = destCompGO.getHeight();
			end = (Point) destCompGO.getCenter().clone();

			shapeDest = DESTRECTANGLE;
			break;
		}
		default:
			break;
		}

		int dx = 0, dy = 0, abs_dx = 0, abs_dy = 0;

		/*
		 * Berechnung der genauen Punkte, je nach grafischer Darstellung des
		 * Start- und Endzustandes
		 */
		switch (shapeSource) {
		case (SOURCECIRCLE): {
			switch (shapeDest) {
			case (DESTCIRCLE): {
				double ankathete = (double) (end.x - start.x);
				double gegenkathete = (double) (end.y - start.y);
				double hypothenuse = Math.sqrt(ankathete * ankathete
						+ gegenkathete * gegenkathete);

				double sin = gegenkathete / hypothenuse;
				double cos = ankathete / hypothenuse;

				start.x = (int) (start.x + cos * radiusStart);
				start.y = (int) (start.y + sin * radiusStart);
				end.x = (int) (end.x - cos * radiusEnd);
				end.y = (int) (end.y - sin * radiusEnd);

				break;
			}
			case (DESTDIAMOND): {
				dx = end.x - start.x;
				dy = end.y - start.y;
				abs_dx = Math.abs(dx);
				abs_dy = Math.abs(dy);

				if (-dy >= abs_dx) {
					end.y = end.y + radiusEnd;
				} else if (-dx > abs_dy) {
					end.x = end.x + radiusEnd;
				} else if (dy >= abs_dx) {
					end.y = end.y - radiusEnd;
				} else if (dx >= abs_dy) {
					end.x = end.x - radiusEnd;
				}

				double ankathete = (double) (end.x - start.x);
				double gegenkathete = (double) (end.y - start.y);
				double hypothenuse = Math.sqrt(ankathete * ankathete
						+ gegenkathete * gegenkathete);

				double sin = gegenkathete / hypothenuse;
				double cos = ankathete / hypothenuse;

				start.x = (int) (start.x + cos * radiusStart);
				start.y = (int) (start.y + sin * radiusStart);
				break;
			}
			case (DESTRECTANGLE): {
				dx = end.x - start.x;
				dy = end.y - start.y;
				abs_dx = Math.abs(dx);
				abs_dy = Math.abs(dy);

				if (-dy >= abs_dx) {
					end.y = end.y + destHeight / 2;
				} else if (-dx > abs_dy) {
					end.x = end.x + destWidth / 2;
				} else if (dy >= abs_dx) {
					end.y = end.y - destHeight / 2;
				} else if (dx >= abs_dy) {
					end.x = end.x - destWidth / 2;
				}

				double ankathete = (double) (end.x - start.x);
				double gegenkathete = (double) (end.y - start.y);
				double hypothenuse = Math.sqrt(ankathete * ankathete
						+ gegenkathete * gegenkathete);
				double sin = gegenkathete / hypothenuse;
				double cos = ankathete / hypothenuse;

				start.x = (int) (start.x + cos * radiusStart);
				start.y = (int) (start.y + sin * radiusStart);
				break;
			}
			default:
				break;
			}
			break;
		}

		case (SOURCEDIAMOND): {
			switch (shapeDest) {
			case (DESTCIRCLE): {
				dx = end.x - start.x;
				dy = end.y - start.y;
				abs_dx = Math.abs(dx);
				abs_dy = Math.abs(dy);

				// choice relativ zu final:
				// unten -> start an oberer Spitze
				if (-dy >= abs_dx) {
					start.y = start.y - radiusStart;
					if (dx == 0) {
						end.y = end.y + radiusEnd;
					}
				}
				// rechts -> Start an linker Spitze
				else if (-dx > abs_dy) {
					start.x = start.x - radiusStart;
				}
				// oben -> start an unterer Spitze
				else if (dy >= abs_dx) {
					start.y = start.y + radiusStart;
					if (dx == 0) {
						end.y = end.y - radiusEnd;
					}
				}
				// links -> Start an rechter Spitze
				else if (dx >= abs_dy) {
					start.x = start.x + radiusStart;
				}
				double ankathete = (double) (end.x - start.x);
				double gegenkathete = (double) (end.y - start.y);
				double hypothenuse = Math.sqrt(ankathete * ankathete
						+ gegenkathete * gegenkathete);
				if (end.x != start.x) {
					double sin = gegenkathete / hypothenuse;
					double cos = ankathete / hypothenuse;
					end.x = (int) (end.x - cos * radiusEnd);
					end.y = (int) (end.y - sin * radiusEnd);
				}

				break;
			}
			case (DESTDIAMOND): {
				dx = end.x - start.x;
				dy = end.y - start.y;
				abs_dx = Math.abs(dx);
				abs_dy = Math.abs(dy);

				if (abs_dx + abs_dy < 25) {
					isChoice = true;
					break;
				}

				if (-dy >= abs_dx) {
					start.y = start.y - radiusStart;
					end.y = end.y + radiusEnd;
				} else if (-dx > abs_dy) {
					start.x = start.x - radiusStart;
					end.x = end.x + radiusEnd;
				} else if (dy >= abs_dx) {
					start.y = start.y + radiusStart;
					end.y = end.y - radiusEnd;
				} else if (dx >= abs_dy) {
					start.x = start.x + radiusStart;
					end.x = end.x - radiusEnd;
				}
				break;
			}
			case (DESTRECTANGLE): {
				dx = end.x - start.x;
				dy = end.y - start.y;
				abs_dx = Math.abs(dx);
				abs_dy = Math.abs(dy);

				if (-dy >= abs_dx) {
					start.y = start.y - radiusStart;
					end.y = end.y + destHeight / 2;
				} else if (-dx > abs_dy) {
					start.x = start.x - radiusStart;
					end.x = end.x + destWidth / 2;
				} else if (dy >= abs_dx) {
					start.y = start.y + radiusStart;
					end.y = end.y - destHeight / 2;
				} else if (dx >= abs_dy) {
					start.x = start.x + radiusStart;
					end.x = end.x - destWidth / 2;
				}
				break;
			}
			default:
				break;
			}
			break;
		}

			// case(SOURCERECTANGLE): {
			// switch(shapeDest) {
			// case(DESTCIRCLE): {
			// dx = end.x - start.x;
			// dy = end.y - start.y;
			// abs_dx = Math.abs(dx);
			// abs_dy = Math.abs(dy);
			//				
			// if( -dy >= abs_dx) {
			// start.y = start.y - sourceHeight/2;
			// }
			// else if( -dx > abs_dy) {
			// start.x = start.x - sourceWidth/2;
			// }
			// else if( dy >= abs_dx) {
			// start.y = start.y + sourceHeight/2;
			// }
			// else if( dx >= abs_dy) {
			// start.x = start.x + sourceWidth/2;
			// }
			//				
			// double ankathete = (double) (end.x - start.x);
			// double gegenkathete = (double) (end.y - start.y);
			// double hypothenuse = Math.sqrt(ankathete*ankathete +
			// gegenkathete*gegenkathete);
			// double sin = gegenkathete / hypothenuse;
			// double cos = ankathete / hypothenuse;
			//				
			// end.x = (int) (end.x - cos * radiusEnd);
			// end.y = (int) (end.y - sin * radiusEnd);
			// break;
			// }
			// case(DESTDIAMOND): {
			// dx = end.x - start.x;
			// dy = end.y - start.y;
			// abs_dx = Math.abs(dx);
			// abs_dy = Math.abs(dy);
			//				
			// if( -dy >= abs_dx) {
			// start.y = start.y - sourceHeight/2;
			// end.y = end.y + radiusEnd;
			// }
			// else if( -dx > abs_dy) {
			// start.x = start.x - sourceWidth/2;
			// end.x = end.x + radiusEnd;
			// }
			// else if( dy >= abs_dx) {
			// start.y = start.y + sourceHeight/2;
			// end.y = end.y - radiusEnd;
			// }
			// else if( dx >= abs_dy) {
			// start.x = start.x + sourceWidth/2;
			// end.x = end.x - radiusEnd;
			// }
			// break;
			// }
			// case(DESTRECTANGLE): {
			// dx = end.x - start.x;
			// dy = end.y - start.y;
			// abs_dx = Math.abs(dx);
			// abs_dy = Math.abs(dy);
			//				
			// if( -dy >= abs_dx) {
			// start.y = start.y - sourceHeight/2;
			// end.y = end.y + destHeight/2;
			// }
			// else if( -dx > abs_dy) {
			// start.x = start.x - sourceWidth/2;
			// end.x = end.x + destWidth/2;
			// }
			// else if( dy >= abs_dx) {
			// start.y = start.y + sourceHeight/2;
			// end.y = end.y - destHeight/2;
			// }
			// else if( dx >= abs_dy) {
			// start.x = start.x + sourceWidth/2;
			// end.x = end.x - destWidth/2;
			// }
			// break;
			// }
			// default:
			// break;
			// }
			// break;
			// }
		default:
			break;
		}

		ret[0] = start;
		ret[1] = end;

		source = (Point) start.clone();
		destination = (Point) end.clone();

		return ret;
	}

	public void fitToGrid() {
		;
	}
	
	public void delete() {
		;
	}

	protected void paintLabel() {
		label.setText(component.getName() + ": "
				+ ((Transition) component).getAction().prettyprint() + "/"
				+ ((Transition) component).getEventName() + "/"
				+ ((Transition) component).getGuard().prettyprint());
		label.setToolTipText(component.getName() + ":"
				+ ((Transition) component).getAction().prettyprint() + "/"
				+ ((Transition) component).getEventName() + "/"
				+ ((Transition) component).getGuard().prettyprint());
		int x, y;
		if (isComposite) {
			CompositeStateGO cs = (CompositeStateGO) sourceGO;
			x = cs.getCenter().x - (cs.getWidth() / 2) - 10 - 50;
			y = cs.getCenter().y + (cs.getHeight() / 2) + 10;
		} else if (isChoice) {
			ChoiceStateGO cs = (ChoiceStateGO) sourceGO;
			x = cs.getCenter().x - 9 - 50;
			y = cs.getCenter().y + cs.getRadius() + 9;
		} else {
			x = (startToEnd[0].x + startToEnd[1].x) / 2;
			y = (startToEnd[0].y + startToEnd[1].y) / 2;
		}
		label.setBounds(x, y, 100, fontSize + 2);
		label.setVisible(pc.ops.tL);
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