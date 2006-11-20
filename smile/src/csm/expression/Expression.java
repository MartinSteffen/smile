package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public abstract class Expression<Result> {

	/**
	 * wertet den Term aus
	 * 
	 * @param varAssignment eine Variablenbelegung
	 * @return der Wert des Terms unter der gegebenen Variablenbelegung
	 */
	abstract public Result evaluate(VarAssignment varAssignment);

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
}
