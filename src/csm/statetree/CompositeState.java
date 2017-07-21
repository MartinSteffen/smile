/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Set;

import csm.Event;
import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;
import csm.expression.Action;
import csm.expression.DoSkip;
import csm.expression.parser.ExpressionParser;


/**
 * im Paper als S_com bekannt
 * 
 * @author hsi
 */
public final class CompositeState extends InternalState {

	private Action doAction = DoSkip.skipAction;

	private LinkedList<String> deferredEvents = new LinkedList<String>();

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
	final public void add(ConnectionPoint child) throws ErrTreeNotChanged {
		addAnyChild(child);
		announceChanges();
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
		announceChanges();
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
	 * Setzt die DoAction. Ist dieser State Teil einer CSM, dann wird
	 * sichergestellt, dass alle in der doAction verwendeten Variablen
	 * in der CSM definiert sind.
	 * 
	 * @param action die dem State zugeordnete DoAction. Muss ungleich
	 *            null sein.
	 * @throws ErrUndefinedElement wenn die Action auf Variablen
	 *             verweist, die in der zugeordneten CSM nicht definiert
	 *             sind
	 */
	final public void setDoAction(Action action) throws ErrUndefinedElement {
		assert action != null : "use DoSkip";

		if (isRooted()) {
			String undefName = action.firstUndefinedVar(getCSM().variables
					.getKeys());
			if (undefName != null)
				throw new ErrUndefinedElement(undefName);
		}
		this.doAction = action;
		announceChanges();
	}

	/**
	 * @param action die dem State zugeordnete DoAction oder null, wenn
	 *            der State keine DoAction enthaelt ("skip").
	 * @throws ErrUndefinedElement wenn die Action auf Variablen
	 *             verweist, die in der zugeordneten CSM nicht definiert
	 *             sind
	 */
	final public void setDoAction(String action)
			throws ErrUndefinedElement, ErrSyntaxError {
		setDoAction(ExpressionParser.parseAction(action));
	}

	/**
	 * gibt die in diesem State als deferred markierten Events zurueck
	 * 
	 * @return eine neue Kopie der Eventliste
	 */
	@Deprecated
	final public LinkedList<Event> getDeferredEvents() {
		assert isRooted();
		LinkedList<Event> el = new LinkedList<Event>();
		for (String es : this.deferredEvents)
			try {
				el.add(getCSM().events.get(es));
			} catch (ErrUndefinedElement e) {
				assert false : "oops, there are unknown events!";
			}
		return el;
	}

	/**
	 * gibt die in diesem State als deferred markierten Events zurueck
	 * 
	 * @return eine neue Kopie der Eventliste
	 */
	final public LinkedList<String> getDeferredEventNames() {
		return new LinkedList<String>(this.deferredEvents);
	}

	/**
	 * Setzt die Liste der in diesem State als deferred markierten
	 * Events. Wenn dieser State Teil einer CSM ist, dann wird
	 * sichergestellt, dass alle Events in dieser definiert sind.
	 * 
	 * @param events eine Liste von Events
	 * @throws ErrUndefinedElement wenn auf der Liste ein Event ist, der
	 *             noch nicht in der CSM definiert ist
	 */
	final public void setDeferredEventNames(LinkedList<String> events)
			throws ErrUndefinedElement {
		if (isRooted())
			for (final String i : events)
				getCSM().events.mustContain(i);

		this.deferredEvents = new LinkedList<String>(events);
		announceChanges();
	}

	/**
	 * Setzt die Liste der in diesem State als deferred markierten
	 * Events. Wenn dieser State Teil einer CSM ist, dann wird
	 * sichergestellt, dass alle Events in dieser definiert sind.
	 * 
	 * @param events eine Liste von Events
	 * @throws ErrUndefinedElement wenn auf der Liste ein Event ist, der
	 *             noch nicht in der CSM definiert ist
	 */
	@Deprecated
	final public void setDeferredEvents(LinkedList<Event> events)
			throws ErrUndefinedElement {
		if (isRooted())
			for (final Event i : events)
				getCSM().events.mustContain(i);

		this.deferredEvents = new LinkedList<String>();
		for (Event e : events)
			this.deferredEvents.add(e.getName());
		announceChanges();
	}

	@Override
	final CSMComponent transitionLocation(State target) {
		assert target != null;
		if (this == target)
			return this;
		return null;
	}

	@Override
	public void dropHere(CSMComponent child) throws ErrTreeNotChanged {
		if (child instanceof ConnectionPoint)
			add((ConnectionPoint) child);
		else if (child instanceof SubRegion)
			add((SubRegion) child);
		else
			dropToParent(child);
	}

	@Override
	final public String firstUndefinedVar(Set<String> definedVars) {
		String av = this.doAction.firstUndefinedVar(definedVars);
		if (av != null)
			return av;
		return firstUndefinedChildVar(definedVars);
	}

	@Override
	final public String firstUndefinedEvent(Set<String> definedEvents) {
		String av = this.doAction.firstUndefinedEvent(definedEvents);
		if (av != null)
			return av;
		for (String e : this.deferredEvents)
			if (!definedEvents.contains(e))
				return e;
		return firstUndefinedChildEvent(definedEvents);
	}

}
