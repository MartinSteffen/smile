/**
 * 
 */
package csm;

import csm.statetree.OutermostRegion;
import csm.statetree.State;


/**
 * @author hs
 */
public final class CoreStateMachine {

	public final OutermostRegion region = new OutermostRegion();

	public final Dictionary events = new Dictionary<Event>();

	public final Dictionary variables = new Dictionary<Variable>();

	public CoreStateMachine() {
	}

	public  Transition connect(State source, State target) {
		// TODO connect implementieren
		return null;
	}
	
	public void disconnect(Transition t) {
		// TODO disconnect implementieren
	}  
}
