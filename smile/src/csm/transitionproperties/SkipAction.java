package csm.transitionproperties;

import csm.VarAssignment;
import csm.actions.Action;


public final class SkipAction extends Action {

	@Override
	VarAssignment doAction(VarAssignment pre) {
		return pre;
	}
}
