package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Set;

import csm.ContainsVarsAndEvents;
import csm.ModelNode;
import csm.exceptions.ErrTreeNotChanged;


/**
 * CSMComponent ist die Oberklasse der States und Regions. Sie besitzt
 * die im Paper unter Definition 1 angegebenen Funktionen parent und '>'
 * (hier: isComponentOf), durch die die Baumstruktur der CSM definiert
 * ist.
 * <p>
 * Als Gegenstueck zum Parent-Attribut besitzt jede Komponente eine
 * Liste ihrer Child-Komponenten -- also derjenigen Komponenten, deren
 * Parent-Attribut auf diese Komponente verweist. Welche Typen von
 * Komponenten als Children einer Komponente eingetragen werden duerfen,
 * bestimmen die add-Methoden dieser Komponente. Beim Eintragen einer
 * Komponente muss man also deren konkreten Typ angeben.
 * <p>
 * Die entsprechende remove-Methode testet, ob die zu entfernende
 * Komponente tatsaechlich eine Child-Komponente ist.
 * <p>
 * Daneben besitzt jede CSM-Komponente eine zweidimensionale Koordinate
 * position, die die Position der Komponente relativ zu ihrer
 * parent-Komponente angibt, sowie eine Funktion getAbsolutePosition,
 * die die Position relativ zur root-Komponente angibt. Wie diese
 * Position zu interpretieren ist, ist Sache der GUI.
 * <p>
 * (Anmerkung: Damit entscheiden wir, dass die Position der Komponenten
 * ein Teil des Modells ist. Das erspart uns erstens, die Objekte
 * automatisch plazieren zu muessen, und zweitens, die komplette
 * Komponenten-Hierarchie in der View spiegeln zu muessen)
 * 
 * @author hsi
 */
public abstract class CSMComponent extends ModelNode<CSMComponent>
		implements ContainsVarsAndEvents {

	private final LinkedList<CSMComponent> children = new LinkedList<CSMComponent>();

	private String nameComment;

	/**
	 * Die Position relativ zur Position der parent-Komponente
	 * <p>
	 * Die Konstruktoren und die Setter-Methode stellen sicher, dass
	 * position niemals null ist.
	 */
	private Point position;

	private int uniqueId;

	/**
	 * Jede Komponente hat eine Position, deshalb stellen wir keinen
	 * Default-Konstruktor zur Verfuegung.
	 */
	CSMComponent(Point position) {
		setPosition(position);
	}

	final void addAnyChild(CSMComponent child) throws ErrTreeNotChanged {
		assert child != null;

		if (isRooted()) {
			Set<String> definedVars = getCSM().variables.getKeys();
			String s = child.firstUndefinedChildVar(definedVars);
			// XXX besser: Unterklasse von ErrUndefinedElement
			if (s != null)
				throw new ErrTreeNotChanged("undefined var " + s);
			Set<String> definedEvents = getCSM().events.getKeys();
			s = child.firstUndefinedChildVar(definedEvents);
			// XXX besser: Unterklasse von ErrUndefinedElement
			if (s != null)
				throw new ErrTreeNotChanged("undefined event " + s);
		}

		child.setParent(this);
		this.children.add(child);
	}

	/**
	 * Entfernt die Komponente child aus dieser CSM-Komponente. Dabei
	 * wird getestet, ob Child überhaupt in this eingetragen war, und ob
	 * die Komponente child noch durch Transitionen mit ihrer Umgebung
	 * verbunden ist.
	 * 
	 * @param child die zu löschende Komponente. Muss ungleich null
	 *            sein.
	 * @return null, wenn die Komponente gelöscht wurde; wenn die
	 *         Komponente nicht gelöscht werden konnte, weil sie noch
	 *         mit der Außenwelt durch Transitionen verbunden ist, dann
	 *         eine der Transitionen, die das Löschen verhindert haben.
	 * @throws ErrTreeNotChanged wenn child nicht in dieser Komponente
	 *             eingetragen war (diesen Fall sollte die GUI
	 *             ausschließen)
	 */
	final public Transition removeIfDisconnected(CSMComponent child)
			throws ErrTreeNotChanged {
		assert child != null;
		final Transition firstConnection = this.firstConnectionWith(child);
		if (firstConnection != null)
			return firstConnection;

		remove(child);
		return null;
	}

	/**
	 * Entfernt die Komponente child aus dieser CSM-Komponente. Dabei
	 * wird getestet, ob Child überhaupt in this eingetragen war, und ob
	 * die Komponente child noch durch Transitionen mit ihrer Umgebung
	 * verbunden ist.
	 * 
	 * @param child die zu löschende Komponente. Muss ungleich null
	 *            sein.
	 * @throws ErrTreeNotChanged wenn child nicht in dieser Komponente
	 *             eingetragen war (diesen Fall sollte die GUI
	 *             ausschließen), oder wenn child noch mit seiner
	 *             Umgebung verbunden ist. Um zu wissen, welche
	 *             Transition das Löschen verhindert hat, sollte man
	 *             daher lieber removeIfDisconnected verwenden.
	 */
	final public void remove(CSMComponent child) throws ErrTreeNotChanged {
		assert child != null;
		if (child.getParent() != this)
			throw new ErrTreeNotChanged("wrong parent to delete from");

		if (child.getParent().firstConnectionWith(child) != null)
			throw new ErrTreeNotChanged(
					"cannot delete: component has transitions");

		child.unsetParent();
		this.children.remove(child);
		announceChanges();
	}

	/**
	 * @return null oder eine Transition, die die Komponente transloc
	 *         mit ihrer Aussenwelt verbindet und in dieser Komponente
	 *         einer ihrer parents eingetrragen ist.
	 */
	Transition firstConnectionWith(CSMComponent transloc) {
		// wenn Conn. in dieser Komponente dann Conn. zurueckgeben
		for (CSMComponent child : this.children)
			if (child instanceof Transition) {
				Transition ct = (Transition) child;
				if (ct.source.isComponentOf(transloc))
					return ct;
				else if (ct.target.isComponentOf(transloc))
					return ct;
			}
		// keine gefunden? dann in parents weitersuchen
		if (getParent() != null)
			return getParent().firstConnectionWith(transloc);
		return null;
	}

	/** die Accept-Methode des Visitor-Patterns */
	abstract void accept(Visitor visitor);

	/**
	 * ruft die accept-Methode aller Child-Komponenten mit dem
	 * angegebenen Visitor auf
	 */
	final void visitChildren(Visitor visitor) {
		for (final CSMComponent s : this.children)
			s.accept(visitor);
	}

	public final String getName() {
		return this.nameComment;
	}

	/**
	 * Name oder Kommentar, muss nicht eindeutig sein, kann auch null
	 * sein
	 * 
	 * @param name irgendein String oder null
	 */
	public final void setName(String name) {
		this.nameComment = name;
		announceChanges();
	}

	public final Point getPosition() {
		return this.position;
	}

	/**
	 * setzt die Koordinate dieser Komponente
	 * 
	 * @param position muss ungleich null sein
	 */
	public final void setPosition(Point position) {
		assert position != null;
		this.position = position;
		announceChanges();
	}

	/**
	 * ermittelt die Position relativ zur aeussersten (root)-Komponente
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

	/**
	 * Damit beim Laden der CSM die Zuordnung der Connections zu ihren
	 * Source- und Target-States erhalten bleibt, erhaelt jeder State
	 * eine eindeutige ID. Diese ID wird beim Modifizieren von States
	 * nicht geupdated. Deshalb wird der Statetree bei jedem Speichern
	 * neu durchnummeriert.
	 * <p>
	 * Eine weitere Verwendung dieser ID ist die Zuordnung von
	 * Gui-Metadaten zu CSM-Komponenten. Auch hier wird die Id nur
	 * intern verwendet (vergleiche die Klasse GuiMetadata).
	 * <p>
	 * <i>Wer die uniqueId ausserhalb des csm-Pakets verwendet, ist
	 * selbst schuld.</i>
	 * 
	 * @return die uniqueId dieser Komponente
	 */
	public int getUniqueId() {
		return this.uniqueId;
	}

	protected void setUniqueId(int id) {
		this.uniqueId = id;
	}

	/**
	 * Wenn möglich, wird das Argument child dieser Komponente
	 * hinzugefügt. Wenn das nicht möglich ist, wird versucht, child der
	 * Parent-Komponente dieser Komponente hinzuzufügen. Wenn die
	 * parent-Komponente null ist, oder wenn die äußerste Region
	 * erreicht ist, wird eine Exception geworfen.
	 * 
	 * @param child die einzufügende Komponente
	 * @throws ErrTreeNotChanged wenn die Komponente child hier nicht
	 *             abgeworfen werden konnte
	 */
	abstract public void dropHere(CSMComponent child)
			throws ErrTreeNotChanged;

	/**
	 * wird von dropHere aufgerufen, um child an die parent-Komponente
	 * weiterzureichen
	 * 
	 * @param child
	 * @throws ErrTreeNotChanged
	 */
	protected void dropToParent(CSMComponent child)
			throws ErrTreeNotChanged {
		if (getParent() == null)
			throw new ErrTreeNotChanged("drop failed");
		getParent().dropHere(child);
	}

	public String firstUndefinedVar(Set<String> definedVars) {
		return firstUndefinedChildVar(definedVars);
	}

	final public String firstUndefinedChildVar(Set<String> definedVars) {
		for (CSMComponent c : this.children) {
			String v = c.firstUndefinedVar(definedVars);
			if (v != null)
				return v;
		}
		return null;
	}

	public String firstUndefinedEvent(Set<String> definedEvents) {
		return firstUndefinedChildEvent(definedEvents);
	}

	final public String firstUndefinedChildEvent(Set<String> definedEvents) {
		for (CSMComponent c : this.children) {
			String v = c.firstUndefinedEvent(definedEvents);
			if (v != null)
				return v;
		}
		return null;
	}

}
