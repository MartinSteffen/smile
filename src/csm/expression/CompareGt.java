package csm.expression;

public class CompareGt extends CompareExpression {

	public CompareGt(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l > r;
	}

	@Override
	String opString() {
		return ">";
	}

}
