package csm.expression;

public abstract class CompareExpression extends
		BinaryExpression<Boolean, Integer> {

	CompareExpression(Expression<Integer> left,
			Expression<Integer> right) {
		super(left, right);
	}

	@Override
	protected int precedence() {
		return 4;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.NONASSOC;
	}

}
