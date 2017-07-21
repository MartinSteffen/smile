package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


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

	public String firstUndefinedVar(Set<String> variables) {
		if (!variables.contains(this.varname))
			return this.varname;
		return null;
	}

	@Override
	protected int precedence() {
		return 10;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.NONASSOC;
	}

}
