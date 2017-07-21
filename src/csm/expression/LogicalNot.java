package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


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
		return "! " + this.guard.prettyprint();
	}

	public String firstUndefinedVar(Set<String> variables) {
		return this.guard.firstUndefinedVar(variables);
	}

	@Override
	protected int precedence() {
		return 3;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.ASSOC;
	}

}
