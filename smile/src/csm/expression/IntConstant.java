package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class IntConstant extends Expression<Integer> {

	public final int value;

	public IntConstant(int value) {
		this.value = value;
	}

	@Override
	public Integer evaluate(ExpressionEnvironment varAssignment) {
		return this.value;
	}

	@Override
	public String prettyprint() {
		return String.valueOf(this.value);
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
	}

	@Override
	protected int precedence() {
		return 10;
	}

}
