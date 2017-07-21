package csm.statetree;

import java.awt.Point;
import java.util.Set;

import csm.Event;
import csm.exceptions.ErrMayNotConnect;
import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;
import csm.expression.Action;
import csm.expression.DoSkip;
import csm.expression.Expression;
import csm.expression.LogicalTrue;
import csm.expression.parser.ExpressionParser;


public final class Transition extends CSMComponent {

	final State source;

	final State target;

	private String event;

	private Expression<Boolean> guard = new LogicalTrue();

	private Action action = DoSkip.skipAction;

	/**
	 * Erzeugt eine neue Transition und traegt sie im Komponentenbaum
	 * ein. Wenn es nicht moeglich ist, Source- und Target-State zu
	 * verbinden, wird eine Exception geworfen, und der Komponentenbaum
	 * bleibt unveraendert.
	 * 
	 * @param source der Source-State dieser Transition
	 * @param target der Target-State dieser Transition
	 * @throws ErrMayNotConnect wenn Source- und Traget-State nicht
	 *             durch eine Transition verbunden werden duerfen.
	 */
	public Transition(Point location, State source, State target)
			throws ErrMayNotConnect {
		super(location);
		assert source != null;
		assert target != null;

		/*
		 * ob eine Transition erlaubt ist, wird nur in der Methode
		 * transitionLocation entschieden. Dort wird auch gleich
		 * bestimmt, in welchen parent-State die Transition eingetragen
		 * wird. In der Regel sollte die innerste gemeinsame Komponente
		 * von Source und Traget sein.
		 */
		final CSMComponent loc = source.transitionLocation(target);
		if (loc == null)
			throw new ErrMayNotConnect();

		/*
		 * in transitionLocation eintragen. -- dafuer verwenden wir
		 * keine explizite add-Methode, da das Eintragen von
		 * Transitionen nur im Konstruktor der jeweiligen Transition
		 * geschieht.
		 */
		try {
			loc.addAnyChild(this);
		} catch (ErrTreeNotChanged e) {
			/*
			 * Dieser Fall sollte nie eintreten, weil hier eine neue
			 * Transition eingetragen wird, die erstens noch kein
			 * Parent-Objekt hat und zweitens nicht Root-Objekt
			 * desjenigen Objekts sein kann, in das sie eingetragen
			 * wird.
			 */
			assert false;
		}
		this.source = source;
		this.target = target;
		announceChanges();
	}

	public final Action getAction() {
		return this.action;
	}

	/**
	 * Setzt die dieser Transition zugeordnete Aktion. Ist diese
	 * Transition Teil einer CSM und enthaelt die Aktion Verweise auf
	 * Variablen, die in dieser CSM nicht definiert sind, dann bleibt
	 * die Transition unveraendert, und es wird eine Exception geworfen.
	 * 
	 * @param action eine Aktion. Muss ungleich null sein.
	 * @throws ErrUndefinedElement wenn in der Action undefinierte
	 *             Variablen referenziert werden
	 */
	public final void setAction(Action action) throws ErrUndefinedElement {
		assert action != null : "use DoSkip";
		if (isRooted()) {
			String undefName = action.firstUndefinedVar(getCSM().variables
					.getKeys());
			if (undefName != null)
				throw new ErrUndefinedElement(undefName);
		}
		this.action = action;
		announceChanges();
	}

	/**
	 * die Action parsen und eintragen
	 * 
	 * @param ein Action-Ausdruck oder null fuer "skip"
	 * @see Transition#setAction(Action)
	 */
	public final void setAction(String action) throws ErrUndefinedElement,
			ErrSyntaxError {
		setAction(ExpressionParser.parseAction(action));
	}

	/**
	 * @return der dieser Transition zugeordnete Event oder null, wenn
	 *         kein Event zugeordnet ist
	 */
	@Deprecated
	public final Event getEvent() {
		assert isRooted();
		if (this.event == null)
			return null;
		try {
			return getCSM().events.get(this.event);
		} catch (ErrUndefinedElement e) {
			// sollte niemals passieren
			return null;
		}
	}

	/**
	 * @return der dieser Transition zugeordnete Event oder null, wenn
	 *         kein Event zugeordnet ist
	 */
	public final String getEventName() {
		return this.event;
	}

	/*
	 * ermittelt, ob dieser Transition ein Event zugeordnet ist
	 */
	public final boolean hasEvent() {
		return this.event != null;
	}

	/**
	 * Setzt den dieser Transition zugeordneten Event. Ist diese
	 * Transition Teil einer CSM, in der dieser Event nicht definiert
	 * ist, dann bleibt die Transition unveraendert, und es wird eine
	 * Exception geworfen.
	 * 
	 * @param event ein String oder null, falls dieser Transition kein
	 *            Event zugeordnet ist
	 * @throws ErrUndefinedElement wenn der Event nicht definiert ist
	 */
	public final void setEvent(String eventname) throws ErrUndefinedElement {
		if (eventname != null && isRooted()) {
			if (!getCSM().events.contains(eventname))
				throw new ErrUndefinedElement(eventname);
		}
		this.event = eventname;
		announceChanges();
	}

	public final Expression<Boolean> getGuard() {
		return this.guard;
	}

	/**
	 * Setzt den Guard der Transition. Wenn diese Transition Teil einer
	 * CSM ist, dann wird sichergestellt, dass alle Variablen, die
	 * dieser Guard referenziert, in dieser CSM definiert sind.
	 * 
	 * @param guard der Guard-Ausdruck dieser Transition. Muss ungleich
	 *            null sein.
	 * @throws ErrUndefinedElement wenn im Guard undefinierte Variablen
	 *             referenziert werden
	 */
	public final void setGuard(Expression<Boolean> guard)
			throws ErrUndefinedElement {
		assert guard != null : "use LogicalTrue";
		if (isRooted()) {
			String undefName = guard.firstUndefinedVar(getCSM().variables
					.getKeys());
			if (undefName != null)
				throw new ErrUndefinedElement(undefName);
		}
		this.guard = guard;
		announceChanges();
	}

	/**
	 * den Guard parsen und eintragen
	 * 
	 * @param guard ein logischer Ausdruck oder null fuer true
	 * @throws ErrUndefinedElement wenn im Guard undefinierte Variablen
	 *             referenziert werden
	 */
	public final void setGuard(String guard) throws ErrUndefinedElement,
			ErrSyntaxError {
		setGuard(ExpressionParser.parseGuard(guard));
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitTransition(this);
	}

	public final State getSource() {
		return this.source;
	}

	public final State getTarget() {
		return this.target;
	}

	@Override
	public void dropHere(CSMComponent child) throws ErrTreeNotChanged {
		dropToParent(child);
	}

	@Override
	public String firstUndefinedEvent(Set<String> definedEvents) {
		if (!definedEvents.contains(this.event))
			return this.event;
		String v = this.guard.firstUndefinedEvent(definedEvents);
		if (v != null)
			return v;
		v = this.action.firstUndefinedEvent(definedEvents);
		if (v != null)
			return v;
		return firstUndefinedChildEvent(definedEvents);
	}

	@Override
	public String firstUndefinedVar(Set<String> definedVars) {
		String v = this.guard.firstUndefinedVar(definedVars);
		if (v != null)
			return v;
		v = this.action.firstUndefinedVar(definedVars);
		if (v != null)
			return v;

		return firstUndefinedChildVar(definedVars);
	}
}
