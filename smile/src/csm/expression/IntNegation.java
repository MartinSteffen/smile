package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


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
		else
			return "- " + this.term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.term.noUndefinedVars(variables);
	}

	@Override
	protected int precedence() {
		return 9;
	}
}
