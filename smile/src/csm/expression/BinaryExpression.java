package csm.expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


abstract class BinaryExpression<Result, Arg> extends Expression<Result> {

	/**
	 * der linke Subterm
	 */
	public final Expression<Arg> left;

	/**
	 * der rechte Subterm
	 */
	public final Expression<Arg> right;

	/**
	 * die Operation, die die beiden Argumente verknüpft
	 */
	abstract Result binaryOp(Arg l, Arg r);

	/**
	 * der Operator als String, wird vom Prettyprinter benutzt
	 */
	abstract String opString();

	BinaryExpression(Expression<Arg> left, Expression<Arg> right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public final Result evaluate(VarAssignment va) {
		final Arg l = this.left.evaluate(va);
		final Arg r = this.right.evaluate(va);
		return binaryOp(l, r);
	}

	@Override
	public final String prettyprint() {
		return '(' + left.prettyprint() + ' ' + opString() + ' '
				+ right.prettyprint() + ')';
	}

	@Override
	public final void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.left.noUndefinedVars(variables);
		this.right.noUndefinedVars(variables);
	}

}
