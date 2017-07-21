package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class WLA extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return va.wla;
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
	protected int precedence() {
		return 10;
	}

}
