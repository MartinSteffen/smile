package csm.expression;

public final class IntProduct extends
		BinaryExpression<Integer, Integer> {

	@Override
	final Integer binaryOp(Integer l, Integer r) {
		return l * r;
	}

	public IntProduct(Expression<Integer> l, Expression<Integer> r) {
		super(l, r);
	}

	@Override
	String opString() {
		return "*";
	}

	@Override
	int precedence() {
		return 7;
	}

}
