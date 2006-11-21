package csm.expression;

public class CompareLeq extends CompareExpression {

	public CompareLeq(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l <= r;
	}

	@Override
	String opString() {
		return "<=";
	}

}
