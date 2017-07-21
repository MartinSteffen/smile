package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class IntNegation extends Expression<Integer> {

	public final Expression<Integer> term;

	public IntNegation(Expression<Integer> term) {
		assert term != null;
		this.term = term;
	}

	@Override
	public Integer evaluate(ExpressionEnvironment va) {
		return -this.term.evaluate(va);
	}

	@Override
	public String prettyprint() {
		if (precedence() > this.term.precedence())
			return "- (" + this.term.prettyprint() + ')';
		return "- " + this.term.prettyprint();
	}

	public String firstUndefinedVar(Set<String> variables) {
		return this.term.firstUndefinedVar(variables);
	}

	@Override
	protected int precedence() {
		return 9;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.ASSOC;
	}

}
