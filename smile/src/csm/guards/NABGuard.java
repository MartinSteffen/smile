package csm.guards;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class NABGuard extends Guard {

	@Override
	public boolean evalGuard(VarAssignment va) {
		// TODO nab-guard implementieren
		return false;
	}

	@Override
	public String prettyprint() {
		return "nab";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables) throws ErrUndefinedElement {
	}

}
