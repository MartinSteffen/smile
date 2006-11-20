package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class TrueExpr extends Expression<Boolean> {

	@Override
	public Boolean evaluate(VarAssignment va) {
		return true;
	}

	@Override
	public String prettyprint() {
		return "true";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}
}
