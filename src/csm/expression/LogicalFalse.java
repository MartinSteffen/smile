package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class LogicalFalse extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return false;
	}

	@Override
	public String prettyprint() {
		return "false";
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
