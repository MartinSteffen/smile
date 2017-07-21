package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class IntConstant extends Expression<Integer> {

	public final int value;

	public IntConstant(int value) {
		this.value = value;
	}

	@Override
	public Integer evaluate(ExpressionEnvironment varAssignment) {
		return this.value;
	}

	@Override
	public String prettyprint() {
		return String.valueOf(this.value);
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
