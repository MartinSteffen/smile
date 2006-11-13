/**
 * 
 */
package csm;

import java.util.Observable;

import csm.statetree.OutermostRegion;


/**
 * @author hs
 */
public final class CoreStateMachine extends Observable {

	public final OutermostRegion region = new OutermostRegion(this);

	public final Dictionary<Event> events = new Dictionary<Event>();

	public final Dictionary<Variable> variables = new Dictionary<Variable>();

	public CoreStateMachine() {
	}

}
