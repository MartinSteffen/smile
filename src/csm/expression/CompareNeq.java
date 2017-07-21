package csm.expression;

public class CompareNeq extends CompareExpression {

	public CompareNeq(Expression<Integer> left,
			Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		// geht hier auch l != r?
		return l.intValue() != r.intValue();
	}

	@Override
	String opString() {
		return "!=";
	}

}
