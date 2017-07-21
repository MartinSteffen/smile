package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


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
	public String prettyprint() {
		return this.varname + " := " + this.term.prettyprint();
	}

	public String firstUndefinedVar(Set<String> dict) {
		if (dict.contains(this.varname))
			return this.term.firstUndefinedVar(dict);
		return this.varname;
	}

	@Override
	public final ExpressionEnvironment[] evaluate(ExpressionEnvironment pre) {
		ExpressionEnvironment[] result = new ExpressionEnvironment[1];
		result[0] = new ExpressionEnvironment(pre);
		result[0].setVar(this.varname, this.term.evaluate(pre));
		return result;
	}

}
