package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class LogicalTrue extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return true;
	}

	@Override
	public String prettyprint() {
		return "true";
	}

	public String firstUndefinedVar(Set<String> variables) {
		return null;
	}

	@Override
	protected int precedence() {
		return 4;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.NONASSOC;
	}

}
