package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class LogicalNot extends Expression<Boolean> {

	Expression<Boolean> guard;

	public LogicalNot(Expression<Boolean> guard) {
		this.guard = guard;

	}

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		Boolean g = this.guard.evaluate(va);
		return !g;
	}

	@Override
	public String prettyprint() {
		if (precedence() > guard.precedence())
			return "! (" + guard.prettyprint() + ')';
		else
			return "! "+guard.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		guard.noUndefinedVars(variables);
	}

	@Override
	int precedence() {
		return 3;
	}
}
