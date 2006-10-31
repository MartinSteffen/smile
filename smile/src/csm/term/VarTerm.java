package csm.term;

import csm.VarAssignment;


public final class VarTerm extends Term {

	String varname;

	public VarTerm(String varname) {
		this.varname = varname;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return va.lookup(this.varname);
	}
}
