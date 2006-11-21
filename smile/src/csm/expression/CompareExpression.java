package csm.expression;

public abstract class CompareExpression extends
		BinaryExpression<Boolean, Integer> {

	CompareExpression(Expression<Integer> left,
			Expression<Integer> right) {
		super(left, right);
	}

	@Override
	int precedence() {
		return 4;
	}
	
}
