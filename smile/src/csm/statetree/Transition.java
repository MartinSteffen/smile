package csm.statetree;

import java.awt.Point;

import csm.Event;
import csm.NamedObject;
import csm.action.Action;
import csm.exceptions.ErrMayNotConnect;
import csm.exceptions.ErrUndefinedElement;
import csm.guards.Guard;


public final class Transition extends CSMComponent {

	final State source;
	final State target;

	// TODO Getter und Setter mit Strings
	private NamedObject event;
	private Guard guard;
	private Action action;

	/**
	 * @param source der Source-State dieser Transition
	 * @param target der Target-State dieser Transition
	 * @throws ErrMayNotConnect wenn Source- und Traget-State nicht
	 *             durch eine Transition verbunden werden d�rfen.
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
		CSMComponent loc = source.transitionLocation(target);
		if (loc == null)
			throw new ErrMayNotConnect();

		/*
		 * in transitionLocation eintragen. -- daf�r verwenden wir keine
		 * explizite add-Methode, da das Eintragen von Transitionen nur
		 * im Konstruktor der jeweiligen Transition geschieht. Wir
		 * wissen, dass this eine unbenutzte Komponente ist, und dass
		 * loc kein Substate von this ist. Daher k�nnen wir hier
		 * addUncheckedChild verwenden.
		 */
		loc.addAnyUncheckedChild(this);
		this.source = source;
		this.target = target;
	}

	public final Action getAction() {
		return this.action;
	}

	/**
	 * @param action eine Aktion oder null, falls dieser Transition
	 *            keine Aktion zugeordnet ist
	 * @throws ErrUndefinedElement wenn in der Action undefinierte
	 *             Variablen referenziert werden
	 */
	public final void setAction(Action action)
			throws ErrUndefinedElement {
		// TODO checken, ob Variablen existieren
		this.action = action;
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
		this.getCSM().events.mustContain(event);
		this.event = event;
	}

	public final Guard getGuard() {
		return this.guard;
	}

	/**
	 * Setzt den Guard der Transition.
	 * 
	 * @param guard the guard to set
	 * @throws ErrUndefinedElement wenn im Guard undefinierte Variablen
	 *             referenziert werden
	 */
	public final void setGuard(Guard guard) throws ErrUndefinedElement {
		// TODO checken, ob Variablen existieren
		this.guard = guard;
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitTransition(this);
	}
}
