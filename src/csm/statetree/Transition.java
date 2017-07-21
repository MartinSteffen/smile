package csm.statetree;

import java.awt.Point;

import csm.Event;
import csm.NamedObject;
import csm.exceptions.ErrMayNotConnect;
import csm.exceptions.ErrUndefinedElement;
import csm.expression.Action;
import csm.expression.Expression;


public final class Transition extends CSMComponent {

	final State source;
	final State target;

	private NamedObject event;
	private Expression<Boolean> guard;
	private Action action;

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
		 * geschieht. Wir wissen, dass this eine unbenutzte Komponente
		 * ist, und dass loc kein Substate von this ist. Daher koennen
		 * wir hier addUncheckedChild verwenden.
		 */
		loc.addAnyUncheckedChild(this);
		this.source = source;
		this.target = target;
	}

	public final Action getAction() {
		return this.action;
	}

	/**
	 * Setzt die dieser Transition zugeordnete Aktion. Enthaelt die
	 * Aktion Verweise auf Variablen, die in der CSM, zu der diese
	 * Transition gehoert, nicht definiert sind, dann bleibt die
	 * Transition unveraendert, und es wird eine Exception geworfen.
	 * 
	 * @param action eine Aktion oder null, falls dieser Transition
	 *            keine Aktion zugeordnet ist
	 * @throws ErrUndefinedElement wenn in der Action undefinierte
	 *             Variablen referenziert werden
	 */
	public final void setAction(Action action)
			throws ErrUndefinedElement {
		action.noUndefinedVars(getCSM().variables);
		this.action = action;
	}

	/**
	 * die Action parsen und eintragen
	 * 
	 * @see Transition#setAction(Action)
	 */
	public final void setAction(String action)
			throws ErrUndefinedElement {
		// TODO parsen
	}

	public final NamedObject getEvent() {
		return this.event;
	}

	/**
	 * @param event ein Event oder null, falls dieser Transition kein
	 *            Event zugeordnet ist
	 * @throws ErrUndefinedElement wenn der Event nicht definiert ist
	 */
	public final void setEvent(Event event) throws ErrUndefinedElement {
		getCSM().events.mustContain(event);
		this.event = event;
	}

	public final Expression<Boolean> getGuard() {
		return this.guard;
	}

	/**
	 * Setzt den Guard der Transition.
	 * 
	 * @param guard the guard to set
	 * @throws ErrUndefinedElement wenn im Guard undefinierte Variablen
	 *             referenziert werden
	 */
	public final void setGuard(Expression<Boolean> guard)
			throws ErrUndefinedElement {
		guard.noUndefinedVars(getCSM().variables);
		this.guard = guard;
	}

	/**
	 * den Guard parsen und eintragen
	 * 
	 * @param guard the guard to set
	 * @throws ErrUndefinedElement wenn im Guard undefinierte Variablen
	 *             referenziert werden
	 */
	public final void setGuard(String guard) throws ErrUndefinedElement {
		// TODO parsen
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitTransition(this);
	}
}
