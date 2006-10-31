package transitionproperties;

import csm.VarAssignment;

public final class AssignAction extends Action {

	Variable var;
	Term term;

	AssignAction(Variable v, Term t) {
		var = v;
		this.term = t;
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}
}
