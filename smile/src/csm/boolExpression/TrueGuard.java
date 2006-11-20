package csm.boolExpression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class TrueGuard extends Guard {

	@Override
	public boolean evalGuard(VarAssignment va) {
		return true;
	}

	@Override
	public String prettyprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}
}
