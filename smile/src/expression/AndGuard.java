package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class AndGuard extends Guard {

	public final Guard left;
	public final Guard right;

	public AndGuard(Guard l, Guard r) {
		this.left = l;
		this.right = r;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		final boolean l = this.left.evalGuard(va);
		// shortcut evaluation
		return l && this.right.evalGuard(va);
	}

	@Override
	public String prettyprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.left.noUndefinedVars(variables);
		this.right.noUndefinedVars(variables);
	}
}
