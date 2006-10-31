/**
 * 
 */
package csm;

import java.util.LinkedList;

import transitionproperties.Variable;

import csm.statetree.OutermostRegion;

/**
 * @author hs
 * 
 */
public final class CoreStateMachine {

	private final OutermostRegion region;

	LinkedList<Variable> varlist;

	/**
	 * 
	 */
	public CoreStateMachine(OutermostRegion region) {
		assert region != null;
		this.region = region;
	}

	// TODO hier haben wir aufgehört
	public boolean containsVar(Variable v) {
		return false;
		// TODO implementieren
	}

	public boolean containsVar(String vname){
		return false;
		// TODO implementieren
	}

}
