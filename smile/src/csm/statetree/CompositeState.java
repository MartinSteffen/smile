/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

import csm.CoreStateMachine;
import csm.Event;
import csm.action.*;
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

	/**
	 *  Jeder CompositeState enthält doAction,
	 *  das ein Objekt vom Typ csm.action.Action enthält
	 *  @return die doAktion oder null, wenn der 
	 *  Zustand keine Do-Action hat
	*/
	final public Action getDoAction() {
		return doAction;
	}

/**
 * 
 * @param action
 * @throws ErrUndefinedElement
 */
	final public void setDoAction(Action action)
			throws ErrUndefinedElement {
		// TODO checken, ob Variablen existieren
		assert action!= null;
		if(action instanceof SkipAction)
			this.doAction = action;
		if(action instanceof AssignAction)
		{
		if (this.getCSM().variables.contains(((AssignAction) action).varname)))
		this.doAction = action;
		else throw new ErrUndefinedElement("variable is undefined");
		}
		if(action instanceof RandomAction)
		{
		if (this.getCSM().variables.contains(((RandomAction) action).varname)))
		this.doAction = action;
		else throw new ErrUndefinedElement("variable is undefined");
		}
		//TODO checken falls action vom type SendAction ob die variablen ihres termes vorhanden sind
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
