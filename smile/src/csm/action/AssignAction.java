package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class AssignAction extends Action {

	final public String varname;
	final public Term term;

	public AssignAction(String varname, Term term) {
		assert varname != null;
		assert term != null;

		this.varname = varname;
		this.term = term;
	}

	@Override
	public VarAssignment doAction(VarAssignment pre) {
		// TODO assignment ausführen
		return null;
	}

	@Override
	public String prettyprint() {
		return varname + " := " + term.prettyprint();  
		}
}
