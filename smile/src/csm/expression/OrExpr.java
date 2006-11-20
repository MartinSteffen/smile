package expression;

public final class OrExpr extends BinaryExpression<Boolean, Boolean> {

	public OrExpr(Expression<Boolean> left, Expression<Boolean> right) {
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

}
