package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class AssignAction extends Action {

	final public String varname;
	final public Term term;

	AssignAction(String varname, Term term) {
		this.varname = varname;
		this.term = term;
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}
}
