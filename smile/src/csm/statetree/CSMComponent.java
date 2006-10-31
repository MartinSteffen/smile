package csm.statetree;

import java.awt.Point;


/**
 * CSMComponent ist die Oberklasse der States und Regions. Sie besitzt die im
 * Paper unter Definition 1 angegebenen Funktionen parent und '>' (hier:
 * isComponentOf), durch die die Baumstruktur der CSM definiert ist.
 * <p>
 * Daneben besitzt jede CSM-Komponente eine zweidimensionale Koordinate
 * position, die die Position der Komponente relativ zu ihrer parent-Komponente
 * angibt, sowie eine Funktion getAbsolutePosition, die die Position relativ zur
 * root-Komponente angibt. Wie diese Position zu interpretieren ist, ist Sache
 * der GUI.
 * <p>
 * (Anmerkung: Damit entscheiden wir, dass die Position der Komponenten ein Teil
 * des Modells ist. Das erspart uns erstens, die Objekte automatisch plazieren
 * zu müssen, und zweitens, die komplette Komponenten-Hierarchie in der View
 * spiegeln zu müssen)
 * 
 * @author hsi
 */
public abstract class CSMComponent {

	/**
	 * Die Position relativ zur Position der parent-Komponente
	 */
	private Point position;

	//
	// Semantik ***********************************

	/**
	 * die Funktion parent gemäß Definition 1 des Skripts
	 * 
	 * @return die CSM-Komponente, in der diese Komponente enthalten ist
	 */
	public abstract CSMComponent parent();

	/**
	 * die Funktion > gemäß Definition 1 des Skripts, also die transitive Hülle
	 * der parent-Funktion
	 * 
	 * @return true, wenn dieses Objekt ein Unterobjekt des Parameters ist
	 */
	public final boolean isComponentOf(CSMComponent possibleParent) {
		// durchsuche von this ausgehend die Kette der Parents, bis
		// entweder das Argument oder das Ende der Kette erreicht ist
		CSMComponent p = this;
		do {
			p = p.parent();
			if (p == possibleParent)
				return true;
		} while (p != null);
		return false;
	}

	//
	// Konstruktion *******************************

	/**
	 * Jede Komponente hat eine Position, deshalb stellen wir keinen
	 * Default-Konstruktor zur Verfügung.
	 * 
	 * @param position
	 *            die Position der Komponente (darf nicht null sein!)
	 */
	CSMComponent(Point position) {
		setPosition(position);
	}

	//
	// Visitor-Pattern ****************************

	abstract void visitMe(CSMVisitor visitor);

	abstract void visitChildren(CSMVisitor visitor);

	//
	// GUI ****************************************

	/**
	 * @return die Position relativ zur parent-Komponente (niemals null)
	 */
	public final Point getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            die neue Position der Komponente (darf nicht null sein!)
	 */
	public void setPosition(Point position) {
		assert position != null;
		this.position = position;
	}

	/**
	 * @return die Position relativ zur äußersten (root)-Komponente
	 */
	public final Point getAbsolutePosition() {
		int x = 0;
		int y = 0;
		CSMComponent p = this;
		for (p = this; p != null; p = p.parent()) {
			x += p.getPosition().x;
			y += p.getPosition().y;
		}
		return new Point(x, y);
	}
}
