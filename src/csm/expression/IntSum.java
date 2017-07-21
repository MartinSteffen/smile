package csm.expression;


public final class IntSum extends BinaryExpression<Integer, Integer> {

	@Override
	final Integer binaryOp(Integer l, Integer r) {
		return l + r;
	}

	public IntSum(Expression<Integer> l, Expression<Integer> r) {
		super(l, r);
	}

	@Override
	String opString() {
		return "+";
	}

	@Override
	protected int precedence() {
		return 6;
	}

}
