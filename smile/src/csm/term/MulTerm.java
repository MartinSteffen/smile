package csm.term;

abstract class MulTerm extends BinaryTerm {

	@Override
	final int binaryOp(int l, int r) {
		return l * r;
	}

	MulTerm(Term l, Term r) {
		super(l, r);
	}
}
