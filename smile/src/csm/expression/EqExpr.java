package expression;

public class EqExpr extends CompareExpression {

	public EqExpr(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l == r;
	}

	@Override
	String opString() {
		return "==";
	}

}
