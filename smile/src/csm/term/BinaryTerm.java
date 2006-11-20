package csm.term;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


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

	protected String pp_bin(String op) {
		return '(' + this.left.prettyprint() + ' ' + op + ' '
				+ this.right.prettyprint() + ')';
	}

	@Override
	public final void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		this.left.noUndefinedVars(dict);
		this.right.noUndefinedVars(dict);
	}

}
