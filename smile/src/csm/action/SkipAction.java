package csm.action;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class SkipAction extends Action {

	@Override
	public VarAssignment doAction(VarAssignment pre) {
		pre.sendEventName = null;
		return pre;
	}

	@Override
	public String prettyprint() {
		return "skip";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
	}

}
