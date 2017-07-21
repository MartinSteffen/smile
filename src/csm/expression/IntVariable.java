package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class IntVariable extends Expression<Integer> {

	final public String varname;

	public IntVariable(String varname) {
		assert varname != null;
		this.varname = varname;
	}

	@Override
	public Integer evaluate(ExpressionEnvironment va) {
		return va.lookupVar(this.varname);
	}

	@Override
	public String prettyprint() {
		return this.varname;
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		variables.mustContain(this.varname);
	}

	@Override
	protected int precedence() {
		return 10;
	}
}
