package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class IntVariable extends Expression<Integer> {

	String varname;

	public IntVariable(String varname) {
		assert varname != null;
		this.varname = varname;
	}

	@Override
	public Integer evaluate(VarAssignment va) {
		return va.lookupVar(this.varname);
	}

	@Override
	public String prettyprint() {
		return varname;
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables) throws ErrUndefinedElement {
		variables.mustContain(varname);
	}

	@Override
	int precedence() {
		return 10;
	}
}
