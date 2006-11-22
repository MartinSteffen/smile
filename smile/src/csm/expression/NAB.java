package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NAB extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return va.nab;
	}

	@Override
	public String prettyprint() {
		return "nab";
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
