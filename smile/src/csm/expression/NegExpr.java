package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NegExpr extends Expression<Integer> {

	public final Expression<Integer> term;

	public NegExpr(Expression<Integer> term) {
		assert term != null;
		this.term = term;
	}

	@Override
	public Integer evaluate(VarAssignment va) {
		return -this.term.evaluate(va);
	}

	@Override
	public String prettyprint() {
		return "- " + term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		term.noUndefinedVars(variables);
	}
}
