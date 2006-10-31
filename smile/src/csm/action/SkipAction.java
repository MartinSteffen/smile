package csm.action;

import csm.VarAssignment;


public final class SkipAction extends Action {

	@Override
	public VarAssignment doAction(VarAssignment pre) {
		return pre;
	}
}
