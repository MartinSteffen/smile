package csm.term;

abstract class AddTerm extends BinaryTerm {

	@Override
	final int binaryOp(int l, int r) {
		return l + r;
	}

	public AddTerm(Term l, Term r) {
		super(l, r);
	}
}