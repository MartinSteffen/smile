/**
 * 
 */
package csm;

import java.util.Observable;

import csm.statetree.OutermostRegion;
import csm.statetree.State;
import csm.statetree.Transition;


/**
 * @author hs
 */
public final class CoreStateMachine extends Observable {

	public final OutermostRegion region = new OutermostRegion();

	public final Dictionary events = new Dictionary<Event>();

	public final Dictionary variables = new Dictionary<Variable>();

	public CoreStateMachine() {
	}

	public Transition connect(State source, State target) {
		assert source != null;
		assert target != null;
		// TODO connect implementieren
		// in Konstruktor von Transition checken
		return null;
	}

	public void disconnect(Transition t) {
		// TODO disconnect implementieren
		// als Methode von Transition
	}

}
