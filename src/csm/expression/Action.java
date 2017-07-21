package csm.expression;

import csm.ExpressionEnvironment;


/**
 * eine Aktion ist ein Ausdruck, der einen Systemzustand (ein
 * ExpressionEnvironment) auf einen neuen Systemzustand abbildet
 */
public abstract class Action extends Expression<ExpressionEnvironment[]> {

	@Override
	public abstract ExpressionEnvironment[] evaluate(
			ExpressionEnvironment pre);

	@Override
	protected final int precedence() {
		return 0;
	}

	@Override
	csm.expression.Expression.ASSOCIATIVITY assoc() {
		return ASSOCIATIVITY.NONASSOC;
	}

}
