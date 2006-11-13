/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

import csm.Event;
import csm.action.Action;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;


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

	/**
	 * Fügt den Child-States dieser Komponente einen ConnectionPoint
	 * hinzu
	 * 
	 * @param child der hinzuzufügende State
	 * @throws ErrTreeNotChanged wenn der ConnectionPoint schon das
	 *             Child irgendeiner Komponente ist
	 */
	final public void add(ConnectionPoint child)
			throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	/**
	 * Fügt den Child-States dieser Komponente eine SubRegion hinzu
	 * 
	 * @param child die hinzuzufügende SubRegion
	 * @throws ErrTreeNotChanged wenn 1. die Region schon das Child
	 *             irgendeiner Komponente ist, oder wenn 2. versucht
	 *             wurde, eine Komponente zu ihrer eigenen
	 *             Unterkomponente zu machen
	 */
	final public void add(SubRegion child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	// TODO kommentieren
	final public Action getDoAction() {
		return doAction;
	}

	// TODO kommentieren
	final public void setDoAction(Action action)
			throws ErrUndefinedElement {
		// TODO checken, ob Variablen existieren
	}

	/**
	 * gibt die in diesem State als deferred markierten Events zurück
	 * 
	 * @return eine neue Kopie der Eventliste
	 */
	final public LinkedList<Event> getDeferredEvents() {
		return new LinkedList<Event>(deferredEvents);
	}

	/**
	 * setzt die Liste der in diesem State als deferred markierten Events
	 * 
	 * @param events eine Liste von Events 
	 * @throws ErrUndefinedElement wenn auf der Liste ein Event ist, der noch nicht in der CSM definiert ist 
	 */
	final public void setDeferredEvents(LinkedList<Event> events)
			throws ErrUndefinedElement {
		for (Event i : deferredEvents)
			getCSM().events.mustContain(i);
		this.deferredEvents = new LinkedList<Event>(events);
	}

	@Override
	final CSMComponent transitionLocation(State target) {
		assert target != null;
		if (this == target)
			return this;
		else
			return null;
	}

}
