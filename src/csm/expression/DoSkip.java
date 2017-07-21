package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class DoSkip extends Action {

	public final static DoSkip skipAction = new DoSkip();

	private DoSkip() {
		// Singleton-Pattern: der private Konstruktor soll verhindern,
		// dass eine zweite Instanz von DoSkip erzeugt wird
	}

	@Override
	public String prettyprint() {
		return "skip";
	}

	public String firstUndefinedVar(Set<String> dict) {
		return null;
	}

	@Override
	public final ExpressionEnvironment[] evaluate(ExpressionEnvironment pre) {
		ExpressionEnvironment[] result = new ExpressionEnvironment[1];
		result[0] = pre;
		return result;
	}

}
