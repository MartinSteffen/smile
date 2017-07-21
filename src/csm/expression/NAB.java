package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class NAB extends Expression<Boolean> {

	@Override
	public Boolean evaluate(ExpressionEnvironment va) {
		return va.nab;
	}

	@Override
	public String prettyprint() {
		return "nab";
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
