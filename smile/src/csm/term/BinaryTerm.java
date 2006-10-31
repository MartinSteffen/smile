package csm.term;

import csm.VarAssignment;


abstract class BinaryTerm extends Term {

	public final Term left;
	public final Term right;

	abstract int binaryOp(int l, int r);

	BinaryTerm(Term l, Term r) {
		this.left = l;
		this.right = r;
	}

	@Override
	public final int evaluate(VarAssignment va) {
		final int l = this.left.evaluate(va);
		final int r = this.right.evaluate(va);
		return binaryOp(l, r);
	}
}
