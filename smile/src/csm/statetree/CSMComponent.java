package csm.statetree;

import java.awt.Point;


/**
 * CSMComponent ist die Oberklasse der States und Regions. Sie besitzt
 * die im Paper unter Definition 1 angegebenen Funktionen parent und '>'
 * (hier: isComponentOf), durch die die Baumstruktur der CSM definiert
 * ist.
 * <p>
 * Daneben besitzt jede CSM-Komponente eine zweidimensionale Koordinate
 * position, die die Position der Komponente relativ zu ihrer
 * parent-Komponente angibt, sowie eine Funktion getAbsolutePosition,
 * die die Position relativ zur root-Komponente angibt. Wie diese
 * Position zu interpretieren ist, ist Sache der GUI.
 * <p>
 * (Anmerkung: Damit entscheiden wir, dass die Position der Komponenten
 * ein Teil des Modells ist. Das erspart uns erstens, die Objekte
 * automatisch plazieren zu müssen, und zweitens, die komplette
 * Komponenten-Hierarchie in der View spiegeln zu müssen)
 * 
 * @author hsi
 */
public abstract class CSMComponent {

	private CSMComponent parent;

	/**
	 * Die Position relativ zur Position der parent-Komponente
	 * <p>
	 * Die Konstruktoren und die Setter-Methode stellen sicher, dass
	 * position niemals null ist.
	 */
	private Point position;

	/**
	 * Jede Komponente hat eine Position, deshalb stellen wir keinen
	 * Default-Konstruktor zur Verfügung.
	 */
	CSMComponent(Point position) {
		setPosition(position);
	}

	/**
	 * die Funktion parent gemäß Definition 1 des Skripts
	 * 
	 * @return die CSM-Komponente, in der diese Komponente enthalten ist
	 */
	public final CSMComponent getParent() {
		return this.parent;
	}

	public final void setParent(CSMComponent parent) {
		if (this.parent != null)
			// throw new ErrDoubleInsert()
			;
		if (this.isComponentOf(parent))
			// Kreis erzeugt
			// throw new ErrDoubleInsert()
			;
		this.parent = parent;
	}
	
	public final void unsetParent(CSMComponent parent) {
		if (this.parent != parent)
			// TODO throw error
			;
		this.parent = null;
	}

	/**
	 * die Funktion > gemäß Definition 1 des Skripts, also die
	 * transitive Hülle der parent-Funktion
	 * 
	 * @return true, wenn dieses Objekt ein Unterobjekt des Parameters
	 *         ist
	 */
	public final boolean isSubComponentOf(CSMComponent possibleParent) {
		// durchsuche von this ausgehend die Kette der Parents, bis
		// entweder das Argument oder das Ende der Kette erreicht ist
		CSMComponent p = this;
		do {
			p = p.getParent();
			if (p == possibleParent)
				return true;
		} while (p != null);
		return false;
	}

	/**
	 * die Funktion >= gemäß Def. 1 des Skriptes, also die
	 * reflexiv-transitive Hülle der parent-Funktion
	 */
	public final boolean isComponentOf(CSMComponent possibleParent) {
		return possibleParent == this || isComponentOf(possibleParent);
	}

	/** die Accept-Methode des Visitor-Patterns */
	abstract void accept(Visitor visitor);

	/**
	 * ruft die accept-Methode aller Child-Komponenten mit dem
	 * angegebenen Visitor auf
	 */
	abstract void visitChildren(Visitor visitor);

	public final Point getPosition() {
		return this.position;
	}

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
		for (p = this; p != null; p = p.getParent()) {
			x += p.getPosition().x;
			y += p.getPosition().y;
		}
		return new Point(x, y);
	}
}
