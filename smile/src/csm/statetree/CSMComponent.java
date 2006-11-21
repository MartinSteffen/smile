package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Observable;

import csm.CoreStateMachine;
import csm.exceptions.ErrStillConnected;
import csm.exceptions.ErrTreeNotChanged;

/**
 * CSMComponent ist die Oberklasse der States und Regions. Sie besitzt die im
 * Paper unter Definition 1 angegebenen Funktionen parent und '>' (hier:
 * isComponentOf), durch die die Baumstruktur der CSM definiert ist.
 * <p>
 * Als Gegenstück zum Parent-Attribut besitzt jede Komponente eine Liste ihrer
 * Child-Komponenten -- also derjenigen Komponenten, deren Parent-Attribut auf
 * diese Komponente verweist. Welche Typen von Komponenten als Children einer
 * Komponente eingetragen werden dürfen, bestimmen die add-Methoden dieser
 * Komponente. Beim Eintragen einer Komponente muss man also deren konkreten Typ
 * angeben.
 * <p>
 * Die entsprechende remove-Methode testet, ob die zu entfernende Komponente
 * tatsächlich eine Child-Komponente ist.
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
public abstract class CSMComponent extends Observable {

	// TODO parents benachrichtigen bei änderungen (observable)
	private CSMComponent parent;

	private final LinkedList<CSMComponent> children = new LinkedList<CSMComponent>();

	private String nameComment;

	/**
	 * Die Position relativ zur Position der parent-Komponente
	 * <p>
	 * Die Konstruktoren und die Setter-Methode stellen sicher, dass position
	 * niemals null ist.
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
	 * ermittelt die CSM, in der diese Komponente verbaut ist.
	 * 
	 * @return entweder die CoreStateMachine oder null, wenn die Komponente in
	 *         keiner CSM verbaut ist
	 */
	// ist nur in OutermostRegion überschrieben
	public CoreStateMachine getCSM() {
		if (this.parent != null)
			return this.parent.getCSM();
		else
			return null;
	}

	/**
	 * die Funktion parent gemäß Definition 1 des Skripts
	 * 
	 * @return die CSM-Komponente, in der diese Komponente enthalten ist
	 */
	public final CSMComponent getParent() {
		return this.parent;
	}

	final void addAnyUncheckedChild(CSMComponent child) {
		child.parent = this;
		this.children.add(child);

	}

	final void addAnyChild(CSMComponent child) throws ErrTreeNotChanged {
		assert child != null;

		/*
		 * Bisher wird die Child-Komponente auch dann eingetragen, wenn sie
		 * Transitionen mit undefinierten Variablen enthält. Dieser Fall kann
		 * erst dann eintreten, wenn die GUI eine Copy&Paste-Funktionalität
		 * bereitstellt und entweder Variablen/Events aus der CSM gelöscht
		 * werden können oder mehrere CSMs gleichzeitig bearbeitet werden.
		 * Letzteres ist nicht absehbar, für ersteres wird die selbe
		 * Funktionalität benötigt wie zur Behebung dieses Bugs. Daraus folgt:
		 * Solange die Spezifikation nicht vorsieht, dass Variablen aus der
		 * Variablenliste der CSM gelöscht werden, ist dieser Bug keiner und
		 * kann daher ignoriert werden.
		 */

		if (child.parent != null)
			throw new ErrTreeNotChanged("component is already in use");
		if (isComponentOf(child))
			throw new ErrTreeNotChanged("tried to create circular dependency");
		addAnyUncheckedChild(child);
	}

	// TODO kommentieren
	final public void remove(CSMComponent child) throws ErrTreeNotChanged,
			ErrStillConnected {
		assert child != null;
		if (child.parent == null)
			throw new ErrTreeNotChanged("no parent to delete from");
		if (child.parent != this)
			throw new ErrTreeNotChanged("wrong parent to delete from");
		// TODO checken, ob noch connected:
		Transition firstConnection = firstOuterConnection(child);
		if (firstConnection != null)
			throw new ErrStillConnected(firstConnection);
		child.parent = null;
		this.children.remove(child);
	}

	Transition firstOuterConnection(CSMComponent child) {
	// TODO implementieren
		// Liste der direkten Subkomponenten durchgehen
		// wenn dabei eine Transition gefunden wurde, die in child
		// hinein/herausführt, diese zurückgeben
		// wenn nicht, bei parent weitersuchen
		return null;
	}

	/**
	 * die Funktion > gemäß Definition 1 des Skripts, also die transitive Hülle
	 * der parent-Funktion
	 * 
	 * @param possibleParent
	 *            die Komponente, in der möglicherweise this enthalten ist
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
	 * die Funktion >= gemäß Def. 1 des Skriptes, also die reflexiv-transitive
	 * Hülle der parent-Funktion
	 * 
	 * @param possibleParent
	 *            die Komponente, in der möglicherweise this enthalten ist
	 * @return true, wenn dieses Objekt ein Unterobjekt von possibleParent oder
	 *         possibleParent selbst ist
	 */
	public final boolean isComponentOf(CSMComponent possibleParent) {
		return possibleParent == this || isSubComponentOf(possibleParent);
	}

	/** die Accept-Methode des Visitor-Patterns */
	abstract void accept(Visitor visitor);

	/**
	 * ruft die accept-Methode aller Child-Komponenten mit dem angegebenen
	 * Visitor auf
	 */
	final void visitChildren(Visitor visitor) {
		for (final CSMComponent s : this.children)
			s.accept(visitor);
	}

	public final String getName() {
		return this.nameComment;
	}

	/**
	 * Name oder Kommentar, muß nicht eindeutig sein, kann auch null sein
	 * 
	 * @param name
	 *            irgendein String oder null
	 */
	public final void setName(String name) {
		this.nameComment = name;
	}

	public final Point getPosition() {
		return this.position;
	}

	/**
	 * setzt die Koordinate dieser Komponente
	 * 
	 * @param position
	 *            muss ungleich null sein
	 */
	public final void setPosition(Point position) {
		assert position != null;
		this.position = position;
	}

	/**
	 * ermittelt die Position relativ zur äußersten (root)-Komponente
	 * 
	 * @return die Position
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
