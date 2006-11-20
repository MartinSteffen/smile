package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class ConstExpr extends Expression<Integer> {

	public final int value;

	public ConstExpr(int value) {
		this.value = value;
	}

	@Override
	public Integer evaluate(VarAssignment varAssignment) {
		return this.value;
	}

	@Override
	public String prettyprint() {
		return String.valueOf(value);
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}

}
