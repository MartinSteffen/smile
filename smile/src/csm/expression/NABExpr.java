package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NABExpr extends Expression<Boolean> {

	@Override
	public Boolean evaluate(VarAssignment va) {
		// TODO nab-guard implementieren
		return false;
	}

	@Override
	public String prettyprint() {
		return "nab";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}

}
