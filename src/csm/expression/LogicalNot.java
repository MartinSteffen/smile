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
		if (precedence() > this.guard.precedence())
			return "! (" + this.guard.prettyprint() + ')';
		else
			return "! " + this.guard.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.guard.noUndefinedVars(variables);
	}

	@Override
	protected int precedence() {
		return 3;
	}
}
