package csm.term;

import csm.VarAssignment;


public final class NegTerm extends Term {

	public final Term term;

	NegTerm(Term term) {
		this.term = term;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return -this.term.evaluate(va);
	}

}
