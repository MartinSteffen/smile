package csm.term;

import csm.VarAssignment;


public final class VarTerm extends Term {

	String varname;

	public VarTerm(String varname) {
		assert varname != null;
		this.varname = varname;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return va.lookup(this.varname);
	}

	@Override
	public String prettyprint() {
		return varname;
	}
}
