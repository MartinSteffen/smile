package csm.guard;

import csm.term.Term;


public class CompareGeqGuard extends CompareGuard {

	public CompareGeqGuard(Term left, Term right) {
		super(left, right);
	}

	@Override
	boolean binaryOp(int l, int r) {
		return l >= r;
	}
}
