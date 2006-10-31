package transitionproperties;

import csm.VarAssignment;

public class CompareEqGuard extends Guard {
	Term l;

	Term r;

	CompareEqGuard(Term l, Term r) {
		this.l = l;
		this.r = r;
	}

	@Override
	boolean evalGuard(VarAssignment va) {
		// TODO Auto-generated method stub
		return l.evaluate(va) == r.evaluate(va);
	}
}
