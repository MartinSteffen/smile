package csm.intExpression;

import csm.VarAssignment;


public final class VarTerm extends Term {

	String varname;

	public VarTerm(String varname) {
		assert varname != null;
		this.varname = varname;
	}

	@Override
	public int evaluate(VarAssignment va) {
		return va.lookupVar(this.varname);
	}

	@Override
	public String prettyprint() {
		return varname;
	}
}
