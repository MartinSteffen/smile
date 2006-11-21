package csm.expression;

public class CompareEq extends CompareExpression {

	public CompareEq(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l.intValue() == r.intValue();
	}

	@Override
	String opString() {
		return "==";
	}


}
