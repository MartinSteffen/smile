package csm.expression;

import csm.ExpressionEnvironment;


/**
 * eine Aktion ist ein Ausdruck, der einen Systemzustand (ein
 * ExpressionEnvironment) auf einen neuen Systemzustand abbildet
 */
public abstract class Action extends Expression<ExpressionEnvironment> {

	abstract void doAction(ExpressionEnvironment pre);

	@Override
	public final ExpressionEnvironment evaluate(
			ExpressionEnvironment pre) {
		final ExpressionEnvironment post = new ExpressionEnvironment(
				pre);
		doAction(post);
		return post;
	}

	@Override
	protected final int precedence() {
		return 0;
	}

}
