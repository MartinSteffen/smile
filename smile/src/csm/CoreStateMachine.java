/**
 * 
 */
package csm;

import csm.statetree.OutermostRegion;


/**
 * @author hs
 * 
 */
public final class CoreStateMachine {

	public final OutermostRegion region = new OutermostRegion();

	public final VariableList variableList = new VariableList();

	public CoreStateMachine() {
	}

}
