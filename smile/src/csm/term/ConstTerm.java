package csm.term;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class ConstTerm extends Term {

	public final int value;

	public ConstTerm(int value) {
		this.value = value;
	}

	@Override
	public int evaluate(VarAssignment varAssignment) {
		return this.value;
	}

	@Override
	public String prettyprint() {
		return String.valueOf(value);
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict) throws ErrUndefinedElement {
	}

}
