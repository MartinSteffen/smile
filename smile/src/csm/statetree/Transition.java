package csm.statetree;

import java.awt.Point;

import csm.CoreStateMachine;
import csm.NamedObject;
import csm.action.Action;
import csm.exceptions.ErrUndefinedElement;
import csm.guards.Guard;


public final class Transition extends CSMComponent {

	// TODO als Methode
	final private CoreStateMachine owner;

	final State source;
	final State target;

	// TODO  Getter und Setter mit Strings
	private NamedObject event;
	private Guard guard;
	private Action action;

	Transition(Point location, State source, State target) {
		super(location);
		assert source != null;
		assert target != null;
		
		// stattdessen Exception werfen
		assert source.transitionLocation(target) != null;

		// TODO in Connectionlocation eintragen
		//this.owner = owner;
		this.source = source;
		this.target = target;
	}

	public final Action getAction() {
		return this.action;
	}

	/**
	 * @param action the action to set
	 * @throws ErrUndefinedElement wenn in der Action undefinierte
	 *             Variablen referenziert werden
	 */
	public final void setAction(Action action) throws ErrUndefinedElement {
		// TODO checken, ob Variablen existieren
		this.action = action;
	}

	public final NamedObject getEvent() {
		return this.event;
	}

	/**
	 * @param event the event to set
	 * @throws ErrUndefinedElement wenn der Event nicht definiert ist
	 */
	public final void setEvent(NamedObject event) throws ErrUndefinedElement {
		// TODO checken, ob Event existiert
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

	@Override
	void visitChildren(Visitor visitor) {
		// eine Transition hat keine Children
		
	}
}
