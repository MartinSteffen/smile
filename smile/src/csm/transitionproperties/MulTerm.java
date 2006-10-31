package csm.transitionproperties;


public final class MulTerm extends Term {
	Term l;

	Term r;
	
	MulTerm(Term l, Term r) {
		this.l=l;
		this.r=r;
	}
	
	@Override
	int evaluate(Object vb) {
		return this.l.evaluate(vb)*this.r.evaluate(vb);
	}

}
