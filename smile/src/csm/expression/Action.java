package csm.expression;

import csm.ExpressionEnvironment;

/**
 * eine Aktion ist ein Ausdruck, der einen Systemzustand (ein VarAssignment) auf
 * einen neuen Systemzustand abbildet
 */
public abstract class Action extends Expression<ExpressionEnvironment> {

	abstract void doAction(ExpressionEnvironment pre);

	@Override
	public final ExpressionEnvironment evaluate(ExpressionEnvironment pre) {
		ExpressionEnvironment post = new ExpressionEnvironment(pre);
		doAction(post);
		return post;
	}

	@Override
	final int precedence() {
		return 0;
	}

}
