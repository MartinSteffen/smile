package csm.guards;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NotGuard extends Guard {

	Guard guard;

	public NotGuard(Guard guard) {
		this.guard = guard;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		boolean g = this.guard.evalGuard(va);
		return !g;
	}

	@Override
	public String prettyprint() {
		return "(! " + this.guard.prettyprint() + ")";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables) throws ErrUndefinedElement {
		noUndefinedVars(variables);
	}
}
