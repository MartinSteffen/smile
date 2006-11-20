package expression;

public final class AndExpr extends BinaryExpression<Boolean, Boolean> {

	public AndExpr(Expression<Boolean> left, Expression<Boolean> right) {
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
}
