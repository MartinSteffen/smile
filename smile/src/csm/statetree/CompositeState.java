/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

import csm.Event;
import csm.action.Action;
import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrTreeNotChanged;


/**
 * im Paper als S_com bekannt
 * 
 * @author hsi
 */
public final class CompositeState extends InternalState {

	private Action doAction;

	private LinkedList<Event> deferredEvents;

	public CompositeState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitCompositeState(this);
	}

	final public void add(EntryState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	final public void add(SubRegion child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	final public void add(ExitState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	final public Action getDoAction() {
		return doAction;
	}

	final public void setDoAction(Action action) {
		// TODO checken, ob Variablen existieren
	}

	/**
	 * die in diesem State als deferred markierten Events
	 * 
	 * @return eine neue Kopie der Eventliste
	 */
	final public LinkedList<Event> getDeferredEvents() {
		return new LinkedList<Event>(deferredEvents);
	}

	final public void setDeferredEvents(LinkedList<Event> events)
			throws ErrAlreadyDefinedElement {
		for (Event i : deferredEvents)
			getCSM().events.mayNotContain(i);
		this.deferredEvents = events;
	}

	@Override
	final public CSMComponent transitionLocation(State target) {
		assert target != null;
		if (this == target)
			return this;
		else
			return null;
	}

}
