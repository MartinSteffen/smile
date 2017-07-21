package csm.expression;

import java.util.Set;

import csm.ContainsVarsAndEvents;
import csm.ExpressionEnvironment;


public abstract class Expression<Result> implements ContainsVarsAndEvents {

	protected enum ASSOCIATIVITY {
		ASSOC, LEFTASSOC, RIGHTASSOC, NONASSOC
	}

	/**
	 * wertet den Term aus
	 * 
	 * @param varAssignment eine Variablenbelegung
	 * @return der Wert des Terms unter der gegebenen Variablenbelegung
	 */
	abstract public Result evaluate(ExpressionEnvironment varAssignment);

	/**
	 * das Gegenstueck zum Parser
	 * 
	 * @return eine textuelle Repraesentation des Terms
	 */
	abstract public String prettyprint();

	/**
	 * alle Operatoren sind linksassoziativ <br>
	 * 10 0-ary var const<br>
	 * 9 unary - <br>
	 * 7 * / <br>
	 * 6 + - <br>
	 * 4 == != <= >= < > true false <br>
	 * 3 unary ! <br>
	 * 2 & <br>
	 * 1 | <br>
	 * 0 action
	 */
	abstract protected int precedence();

	abstract ASSOCIATIVITY assoc();

	final public String firstUndefinedEvent(Set<String> definedEvents) {
		return null;
	}
}
