package csm.guards;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class OrGuard extends Guard {

	Guard left;
	Guard right;

	public OrGuard(Guard left, Guard right) {
		this.left = left;
		this.right = right;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		final boolean l = this.left.evalGuard(va);
		// shortcut evaluation
		return l || this.right.evalGuard(va);
	}

	@Override
	public String prettyprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		left.noUndefinedVars(variables);
		right.noUndefinedVars(variables);
	}
}
