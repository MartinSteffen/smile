package transitionproperties;


public final class NegTerm extends Term {

	Term t;
	
	NegTerm(Term t) {
		this.t=t;
	}
	
	@Override
	int evaluate(Object vb) {
		return - t.evaluate(vb);
	}

}
