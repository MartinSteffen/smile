package csm.expression;


public class CompareLt extends CompareExpression {

	public CompareLt(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l < r;
	}

	@Override
	String opString() {
		return "<";
	}

}
