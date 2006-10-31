package csm.guard;

import csm.VarAssignment;
import csm.term.Term;


public abstract class CompareGuard extends Guard {

	public final Term left;
	public final Term right;

	abstract boolean binaryOp(int l, int r);

	CompareGuard(Term left, Term right) {
		this.left = left;
		this.right = right;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return binaryOp(this.left.evaluate(va), this.right.evaluate(va));
	}
}
