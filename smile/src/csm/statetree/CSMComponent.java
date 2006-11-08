package csm.statetree;

import java.awt.Point;
import java.util.Observable;

import csm.exceptions.ErrTreeNotChanged;

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
 * zu m�ssen, und zweitens, die komplette Komponenten-Hierarchie in der View
 * spiegeln zu m�ssen)
 * 
 * @author hsi
 */
public abstract class CSMComponent extends Observable {
	// TODO parents benachrichtigen bei �nderungen (observable)
	private CSMComponent parent;

	/**
	 * Name oder Kommentar, mu� nicht eindeutig sein, kann auch null sein
	 */
	public String nameComment;

	/**
	 * Die Position relativ zur Position der parent-Komponente
	 * <p>
	 * Die Konstruktoren und die Setter-Methode stellen sicher, dass position
	 * niemals null ist.
	 */
	private Point position;

	/**
	 * Jede Komponente hat eine Position, deshalb stellen wir keinen
	 * Default-Konstruktor zur Verf�gung.
	 */
	CSMComponent(Point position) {
		setPosition(position);
	}

	/**
	 * die Funktion parent gem�� Definition 1 des Skripts
	 * 
	 * @return die CSM-Komponente, in der diese Komponente enthalten ist
	 */
	public final CSMComponent getParent() {
		return this.parent;
	}

	/**
	 * Der Komponente wird eine parent-Komponente zugewiesen
	 * <p>
	 * nach setParent muss der Aufrufer daf�r sorgen, dass diese Komponente auch
	 * in der Child-Liste von parent eingetragen wird.
	 * <p>
	 * wegen dieser Anforderung ist setParent nur f�r Package-internen Gebrauch
	 * 
	 * @throws ErrTreeNotChanged
	 *             wenn die Komponente bereits eine parent-Komponente besitzt,
	 *             oder wenn versucht wird, sie zu ihrer eigenen Unterkomponente
	 *             zu machen
	 */
	final void setParent(CSMComponent parent) throws ErrTreeNotChanged {
		if (this.parent != null)
			throw new ErrTreeNotChanged("component is already in use");
		// throw new ErrDoubleInsert()
		;
		if (parent.isComponentOf(this))
			// Kreis erzeugt
			throw new ErrTreeNotChanged("tried to create circular dependency");
		;
		this.parent = parent;
	}

	/**
	 * Der Komponente wird als parent-Komponente null zugewiesen, wenn sie die
	 * direkte Unterkomponente der als Parameter angegebenen Komponente ist.
	 * <p>
	 * nach setParent muss der Aufrufer daf�r sorgen, dass diese Komponente auch
	 * aus der Child-Liste von parent ausgetragen wird.
	 * <p>
	 * wegen dieser Anforderung ist unsetParent nur f�r Package-internen
	 * Gebrauch
	 * 
	 * @throws ErrTreeNotChanged
	 *             wenn die Komponente keine direkte Unterkomponente der
	 *             �bergebenen Komponente ist.
	 */
	final void unsetParent(CSMComponent parent) throws ErrTreeNotChanged {
		// TODO unterscheidung null - falsches parent
		if (this.parent != parent)
			throw new ErrTreeNotChanged("no parent to delete from");
		this.parent = null;
	}

	/**
	 * die Funktion > gem�� Definition 1 des Skripts, also die transitive H�lle
	 * der parent-Funktion
	 * 
	 * @return true, wenn dieses Objekt ein Unterobjekt des Parameters ist
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
	 * die Funktion >= gem�� Def. 1 des Skriptes, also die reflexiv-transitive
	 * H�lle der parent-Funktion
	 */
	public final boolean isComponentOf(CSMComponent possibleParent) {
		return possibleParent == this || isComponentOf(possibleParent);
	}

	/** die Accept-Methode des Visitor-Patterns */
	abstract void accept(Visitor visitor);

	/**
	 * ruft die accept-Methode aller Child-Komponenten mit dem angegebenen
	 * Visitor auf
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
	 * @return die Position relativ zur �u�ersten (root)-Komponente
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
