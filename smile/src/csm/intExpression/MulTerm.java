package csm.intExpression;

public final class MulTerm extends BinaryTerm {

	@Override
	final int binaryOp(int l, int r) {
		return l * r;
	}

	public MulTerm(Term l, Term r) {
		super(l, r);
	}

	@Override
	public String prettyprint() {
		return pp_bin("*");
	}
	
	
}
