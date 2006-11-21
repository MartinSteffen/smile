package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class DoSkip extends Action {

	@Override
	public void doAction(ExpressionEnvironment pre) {
	}

	@Override
	public String prettyprint() {
		return "skip";
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
	}

}
