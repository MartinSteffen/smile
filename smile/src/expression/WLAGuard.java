package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class WLAGuard extends Guard {

	@Override
	public boolean evalGuard(VarAssignment va) {
		// TODO wla-guard implementieren
		return false;
	}

	@Override
	public String prettyprint() {
		return "wla";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}

}
