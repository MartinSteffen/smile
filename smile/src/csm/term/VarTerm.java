package csm.term;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


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

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict) throws ErrUndefinedElement {
		dict.mustContain(varname); 
	}
}
