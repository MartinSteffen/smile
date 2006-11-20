package csm.intExpression;

import csm.VarAssignment;


public final class NegTerm extends Term {

	public final Term term;

	public NegTerm(Term term) {
		assert term != null;
		this.term = term;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return -this.term.evaluate(va);
	}

	@Override
	public String prettyprint() {
		return "- " + term.prettyprint();
	}

}
