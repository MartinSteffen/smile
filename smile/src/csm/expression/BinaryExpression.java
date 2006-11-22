package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
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
	 * die Operation, die die beiden Argumente verknuepft
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
	public final Result evaluate(ExpressionEnvironment va) {
		final Arg l = this.left.evaluate(va);
		final Arg r = this.right.evaluate(va);
		return binaryOp(l, r);
	}

	@Override
	public final String prettyprint() {
		String ls = this.left.prettyprint();
		if (precedence() > this.left.precedence())
			ls = '(' + ls + ')';
		String rs = this.right.prettyprint();
		if (precedence() >= this.right.precedence())
			rs = '(' + rs + ')';
		return ls + ' ' + opString() + ' ' + rs;
	}

	@Override
	public final void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement {
		this.left.noUndefinedVars(variables);
		this.right.noUndefinedVars(variables);
	}

}
