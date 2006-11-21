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
	int precedence() {
		return 1;
	}

}
