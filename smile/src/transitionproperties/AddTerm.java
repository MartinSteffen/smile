package transitionproperties;


public class AddTerm extends Term {
	Term l;

	Term r;
	
	AddTerm(Term l, Term r) {
		this.l=l;
		this.r=r;
	}
	
	int evaluate(Object vb) {
		return l.evaluate(vb)+r.evaluate(vb);
	}
}
