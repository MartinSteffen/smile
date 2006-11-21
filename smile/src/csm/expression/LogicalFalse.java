package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class LogicalFalse extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return false;
	}

	@Override
	public String prettyprint() {
		return "false";
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
