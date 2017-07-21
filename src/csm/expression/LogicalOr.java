package csm.expression;



public final class LogicalOr extends BinaryExpression<Boolean, Boolean> {

	public LogicalOr(Expression<Boolean> left, Expression<Boolean> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Boolean l, Boolean r) {
		return l || r;
	}

	@Override
	String opString() {
		return "|";
	}

	@Override
	protected int precedence() {
		return 1;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.ASSOC;
	}

}
