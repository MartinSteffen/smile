package csm.guard;

import csm.term.Term;


public class CompareEqGuard extends CompareGuard {

	public CompareEqGuard(Term left, Term right) {
		super(left, right);
	}

	@Override
	boolean binaryOp(int l, int r) {
		return l == r;
	}
}
