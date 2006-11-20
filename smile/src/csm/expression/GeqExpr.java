package expression;

public class GeqExpr extends CompareExpression {

	public GeqExpr(Expression<Integer> left, Expression<Integer> right) {
		super(left, right);
	}

	@Override
	Boolean binaryOp(Integer l, Integer r) {
		return l >= r;
	}

	@Override
	String opString() {
		return ">=";
	}

}
