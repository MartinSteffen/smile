package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class DoSkip extends Action {

	@Override
	public void doAction(VarAssignment pre) {
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