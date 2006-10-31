package transitionproperties;

import csm.VarAssignment;


public final class SkipAction extends Action {

	@Override
	VarAssignment doAction(VarAssignment pre) {
		return pre;
	}
}
