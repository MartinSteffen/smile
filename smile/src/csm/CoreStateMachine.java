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
	abstract public boolean containsVar(Variable v);

	abstract public boolean containsVar(String vname);

}
