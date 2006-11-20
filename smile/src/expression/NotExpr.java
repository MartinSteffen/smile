package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NotExpr extends Expression<Boolean> {

	Expression<Boolean> guard;

	public NotExpr(Expression<Boolean> guard) {
		this.guard = guard;

	}

	@Override
	public Boolean evaluate(VarAssignment va) {
		Boolean g = this.guard.evaluate(va);
		return !g;
	}

	@Override
	public String prettyprint() {
		return "(! " + this.guard.prettyprint() + ")";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		guard.noUndefinedVars(variables);
	}
}
