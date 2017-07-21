package csm.expression;


public final class LogicalAnd extends
		BinaryExpression<Boolean, Boolean> {

	public LogicalAnd(Expression<Boolean> left,
			Expression<Boolean> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Boolean l, Boolean r) {
		return l && r;
	}

	@Override
	String opString() {
		return "&";
	}

	@Override
	protected int precedence() {
		return 2;
	}
}
