/**
 * 
 */
package csm;

import csm.statetree.AbstractRegion;
import csm.statetree.CSMTraversal;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;

/**
 * @author hs
 * 
 */
public class StateCounter implements CSMTraversal {

	private int count = 0;

	static int countStates(AbstractRegion r) {
		final StateCounter c = new StateCounter();
		r.traverseCSM(c);
		return c.count;
	}

	public boolean enterCompositeState(CompositeState scom) {
		this.count++;
		return true;
	}

	public boolean enterFinalState(FinalState sfin) {
		this.count++;
		return true;
	}

	public boolean enterRegion(AbstractRegion region) {
		return true;
	}

	public void exitCompositeState(CompositeState scom) {
	}

	public void exitFinalState(FinalState sfin) {
	}

	public void exitRegion(AbstractRegion region) {
	}

	public void visitChoiceState(ChoiceState schoice) {
		this.count++;
	}

	public void visitEntryState(EntryState sentry) {
		this.count++;
	}

	public void visitExitState(ExitState sexit) {
		this.count++;
	}

}
