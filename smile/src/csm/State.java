/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * Abstrakte Oberklasse aller States
 * <p>
 * besitzt die im Paper unter Definition 1 angegebenen Funktionen stateOf und
 * regionOf
 * 
 * @author hsi
 */
public abstract class State extends CSMComponent {

	//
	// Semantik ***********************************

	public abstract State stateOf();

	public final AbstractRegion regOf() {
		return (AbstractRegion) stateOf().parent();
	}

	//
	// Konstruktion *******************************

	public State(Point position) {
		super(position);
	}

	//
	// Connections ********************************

	/**
	 * @param target
	 *            das potenzielle Ziel einer Transition, die von diesem State
	 *            ausgeht
	 * 
	 * @return true, wenn von diesem State eine Transition zu dem State target
	 *         gehen darf; false, wenn nicht.
	 */

	public abstract boolean mayConnectTo(State target);

	protected abstract boolean mayConnectFromEntryState(EntryState source);

	protected abstract boolean mayConnectFromExitState(ExitState source);

	protected abstract boolean mayConnectFromChoiceState(ChoiceState source);
}
