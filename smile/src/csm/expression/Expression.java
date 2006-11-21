package csm.expression;

import csm.Dictionary;
import csm.ExpressionEnvironment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public abstract class Expression<Result> {

	/**
	 * wertet den Term aus
	 * 
	 * @param varAssignment eine Variablenbelegung
	 * @return der Wert des Terms unter der gegebenen Variablenbelegung
	 */
	abstract public Result evaluate(ExpressionEnvironment varAssignment);

	/**
	 * das Gegenstück zum Parser
	 * 
	 * @return eine textuelle Repräsentation des Terms
	 */
	abstract public String prettyprint();

	/**
	 * stellt sicher, dass alle in dieser Expression verwendeten
	 * Variablen im übergebenen Dictionary enthalten sind. Andernfalls
	 * wird eine Exception geworfen, die auf eine nicht enthaltene
	 * Variable hinweist.
	 * 
	 * @param variables das Dictionary, in dem alle verwendeten
	 *            Variablen vorkommen müssen
	 * @throws ErrUndefinedElement wenn der Term eine undefinierte
	 *             Variable enthält
	 */
	abstract public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement;

	/**
	 * alle Operatoren sind linksassoziativ <br>
	 * 10 0-ary var const true false<br>
	 * 9 unary - <br>
	 * 7 * / <br>
	 * 6 + - <br>
	 * 4 == != <= >= < > <br>
	 * 3 unary ! <br>
	 * 2 & <br>
	 * 1 | <br>
	 * 0 action
	 */
	abstract int precedence();
}
