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

	private final OutermostRegion region;

	/**
	 * 
	 */
	public CoreStateMachine(OutermostRegion region) {
		assert region != null;
		this.region = region;
	}

}
