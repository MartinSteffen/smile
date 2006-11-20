package csm.term;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


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
		return "- " + this.term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		this.term.noUndefinedVars(dict);
	}

}
