package expression;

import csm.intExpression.Term;


public class CompareEqGuard extends CompareGuard {

	public CompareEqGuard(Term left, Term right) {
		super(left, right);
	}

	@Override
	boolean binaryOp(int l, int r) {
		return l == r;
	}

	@Override
	public String prettyprint() {
		// TODO Auto-generated method stub
		return null;
	}
}
