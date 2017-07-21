package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


abstract public class BinaryExpression<Result, Arg> extends
		Expression<Result> {

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
		if (assoc() == ASSOCIATIVITY.RIGHTASSOC
			|| assoc() == ASSOCIATIVITY.NONASSOC)
			if (precedence() == this.left.precedence())
				ls = '(' + ls + ')';
		String rs = this.right.prettyprint();
		if (precedence() > this.right.precedence())
			rs = '(' + rs + ')';
		if (assoc() == ASSOCIATIVITY.LEFTASSOC
			|| assoc() == ASSOCIATIVITY.NONASSOC)
			if (precedence() == this.right.precedence())
				rs = '(' + rs + ')';
		return ls + ' ' + opString() + ' ' + rs;
	}

	public final String firstUndefinedVar(Set<String> definedVars) {
		String l = this.left.firstUndefinedVar(definedVars);
		if (l == null)
			l = this.right.firstUndefinedVar(definedVars);
		return l;
	}

}
