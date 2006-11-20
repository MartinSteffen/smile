package csm.expression;

public final class AddExpr extends BinaryExpression<Integer, Integer> {

	@Override
	final Integer binaryOp(Integer l, Integer r) {
		return l + r;
	}

	public AddExpr(Expression<Integer> l, Expression<Integer> r) {
		super(l, r);
	}

	@Override
	String opString() {
		return "+";
	}

}
