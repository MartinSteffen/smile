package csm.transitionproperties;

import csm.VarAssignment;
import csm.Variable;
import csm.actions.Action;

public final class AssignAction extends Action {

	Variable var;
	Term term;

	AssignAction(Variable v, Term t) {
		this.var = v;
		this.term = t;
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}
}
