package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class WLA extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return va.wla;
	}

	@Override
	public String prettyprint() {
		return "wla";
	}

	public String firstUndefinedVar(Set<String> variables) {
		return null;
	}

	@Override
	protected int precedence() {
		return 10;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.NONASSOC;
	}

}
