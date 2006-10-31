package csm.transitionproperties;


public class AddTerm extends Term {
	Term l;

	Term r;
	
	AddTerm(Term l, Term r) {
		this.l=l;
		this.r=r;
	}
	
	@Override
	int evaluate(Object vb) {
		return this.l.evaluate(vb)+this.r.evaluate(vb);
	}
}
