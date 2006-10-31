package csm.guard;

import csm.term.Term;


public class CompareGeqGuard extends CompareGuard {

	CompareGeqGuard(Term left, Term right) {
		super(left, right);
	}

	@Override
	boolean binaryOp(int l, int r) {
		return l >= r;
	}
}
