package csm.statetree;

import java.awt.Point;


/**
 * Abstrakte Oberklasse aller States
 * 
 * @author hsi
 */
public abstract class State extends CSMComponent {

	//
	// Semantik ***********************************

	/**
	 * gem�� Paper, Definition 1
	 * 
	 * @return f�r einen ConnectionPoint der enthaltende InternalState, f�r
	 *         einen InternalState diesen selbst
	 */
	public abstract InternalState stateOf();

	/**
	 * gem�� Paper, Definition 1: stateOf(parent(this))
	 * 
	 * @return die umgebende Region dieses States
	 */
	public final AbstractRegion regOf() {
		return (AbstractRegion) stateOf().parent();
	}

	//
	// Konstruktion *******************************

	protected State(Point position) {
		super(position);
	}

	/**
	 * Connections *******************************
	 * 
	 * Definition 4 des Papers gibt an, welche States durch Transitionen
	 * verbunden werden d�rfen. Daraus ergibt sich folgende Tabelle, die in der
	 * Methode mayConnectTo implementiert ist:
	 * 
	 * source, target, Bedingung
	 * 
	 * Sfin, *, false
	 * 
	 * Scom, *, source == target
	 * 
	 * Sexit, Sexit, stateOf(stateOf(source)) == stateOf(target)
	 * 
	 * Sexit, {Sentry, Sfinal, Schoice}, regOf(target)==regOf(source)
	 * 
	 * Sexit, Scom, false
	 * 
	 * Sentry, *, stateOf(source) == stateOf(stateOf(target))
	 * 
	 * Schoice, *, regOf(target)==regOf(source)
	 * 
	 * {Sentry, Schoice}, {Sexit, Scom}, false
	 * 
	 * {Sentry, Schoice}, {Sentry, Sfinal, Schoice}, true
	 * 
	 * 
	 */

	/**
	 * gibt an, ob von diesem State eine Transition zum Target-State ausgehen
	 * darf. Gem�� Def. 4 des Papers
	 * 
	 * @return true, wenn von diesem State eine Transition zu dem State target
	 *         gehen darf; false, wenn nicht.
	 */
	public abstract boolean mayConnectTo(State target);

	abstract boolean mayConnectFromEntryState(EntryState source);

	abstract boolean mayConnectFromExitState(ExitState source);

	abstract boolean mayConnectFromChoiceState(ChoiceState source);
}
