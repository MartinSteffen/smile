package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class WLA extends Expression<Boolean> {

	@Override
	public Boolean evaluate(VarAssignment va) {
		// TODO wla-guard implementieren
		return false;
	}

	@Override
	public String prettyprint() {
		return "wla";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}

	@Override
	int precedence() {
		return 10;
	}

}
