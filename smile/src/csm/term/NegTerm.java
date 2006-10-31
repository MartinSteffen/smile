package csm.term;

import csm.VarAssignment;


public final class NegTerm extends Term {

	public final Term term;

	public NegTerm(Term term) {
		this.term = term;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return -this.term.evaluate(va);
	}

}
