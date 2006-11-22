/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

import csm.Event;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;
import csm.expression.Action;


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
	 * Fuegt den Child-States dieser Komponente einen ConnectionPoint
	 * hinzu
	 * 
	 * @param child der hinzuzufuegende State
	 * @throws ErrTreeNotChanged wenn der ConnectionPoint schon das
	 *             Child irgendeiner Komponente ist
	 */
	final public void add(ConnectionPoint child)
			throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	/**
	 * Fuegt den SubRegions dieser Komponente eine SubRegion hinzu. Die
	 * Stelle, an der sie zwischen den anderen SubRegions eingefuegt
	 * wird, ist dabei nicht festgelegt. Es ist Sache der einfuegenden
	 * Methode, die Positionen der SubRegions zu setzen.
	 * 
	 * @param child die hinzuzufuegende SubRegion
	 * @throws ErrTreeNotChanged wenn 1. die Region schon das Child
	 *             irgendeiner Komponente ist, oder wenn 2. versucht
	 *             wurde, eine Komponente zu ihrer eigenen
	 *             Unterkomponente zu machen
	 */
	final public void add(SubRegion child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	/**
	 * Jeder CompositeState enthaelt doAction, das ein Objekt vom Typ
	 * csm.action.Action enthaelt
	 * 
	 * @return die doAktion oder null, wenn der Zustand keine Do-Action
	 *         hat
	 */
	final public Action getDoAction() {
		return this.doAction;
	}

	/**
	 * @param action die dem State zugeordnete DoAction oder null, wenn
	 *            der State keine DoAction enthaelt.
	 * @throws ErrUndefinedElement wenn die Action auf Variablen
	 *             verweist, die in der zugeordneten CSM nicht definiert
	 *             sind
	 */
	final public void setDoAction(Action action)
			throws ErrUndefinedElement {
		assert action != null;
		action.noUndefinedVars(getCSM().variables);
		this.doAction = action;
	}

	/**
	 * gibt die in diesem State als deferred markierten Events zurueck
	 * 
	 * @return eine neue Kopie der Eventliste
	 */
	final public LinkedList<Event> getDeferredEvents() {
		return new LinkedList<Event>(this.deferredEvents);
	}

	/**
	 * setzt die Liste der in diesem State als deferred markierten
	 * Events
	 * 
	 * @param events eine Liste von Events
	 * @throws ErrUndefinedElement wenn auf der Liste ein Event ist, der
	 *             noch nicht in der CSM definiert ist
	 */
	final public void setDeferredEvents(LinkedList<Event> events)
			throws ErrUndefinedElement {
		for (final Event i : this.deferredEvents)
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
