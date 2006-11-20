package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public abstract class CompareGuard extends Guard {

	public final Term left;
	public final Term right;

	abstract boolean binaryOp(int left, int right);

	CompareGuard(Term left, Term right) {
		this.left = left;
		this.right = right;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		return binaryOp(this.left.evaluate(va), this.right.evaluate(va));
	}

	@Override
	public final void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.left.noUndefinedVars(variables);
		this.right.noUndefinedVars(variables);
	}

}
