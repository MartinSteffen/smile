package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class AssignAction extends Action {

	final public String varname;
	final public Term term;

	public AssignAction(String varname, Term term) {
		this.varname = varname;
		this.term = term;
	}

	@Override
	public VarAssignment doAction(VarAssignment pre) {
		// TODO assignment ausführen
		return null;
	}
}
