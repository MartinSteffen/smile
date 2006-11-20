package expression;

import csm.intExpression.Term;


public class CompareGeqGuard extends CompareGuard {

	public CompareGeqGuard(Term left, Term right) {
		super(left, right);
	}

	@Override
	boolean binaryOp(int l, int r) {
		return l >= r;
	}

	@Override
	public String prettyprint() {
		// TODO Auto-generated method stub
		return null;
	}

}
