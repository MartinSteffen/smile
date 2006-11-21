package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class DoAssign extends Action {

	final public String varname;
	final public Expression<Integer> term;

	public DoAssign(String varname, Expression<Integer> term) {
		assert varname != null;
		assert term != null;

		this.varname = varname;
		this.term = term;
	}

	@Override
	final void doAction(ExpressionEnvironment pre) {
		int i = this.term.evaluate(pre);
		pre.setVar(this.varname, i);
	}

	@Override
	public String prettyprint() {
		return this.varname + " := " + this.term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		dict.mustContain(this.varname);
		this.term.noUndefinedVars(dict);
	}


}
