package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class IntNegation extends Expression<Integer> {

	public final Expression<Integer> term;

	public IntNegation(Expression<Integer> term) {
		assert term != null;
		this.term = term;
	}

	@Override
	public Integer evaluate(VarAssignment va) {
		return -this.term.evaluate(va);
	}

	@Override
	public String prettyprint() {
		if (precedence() > term.precedence())
			return "- (" + term.prettyprint() + ")";
		else
			return "- " + term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		term.noUndefinedVars(variables);
	}

	@Override
	int precedence() {
		return 9;
	}
}
