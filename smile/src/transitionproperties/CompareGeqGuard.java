package transitionproperties;

import csm.VarAssignment;

public final class CompareGeqGuard extends Guard {
	Term l;

	Term r;

	CompareGeqGuard(Term l, Term r) {
		this.l = l;
		this.r = r;
	}

	@Override
	boolean evalGuard(VarAssignment va) {
		// TODO Auto-generated method stub
		return l.evaluate(va)>=r.evaluate(va);
	}
	
	
}
