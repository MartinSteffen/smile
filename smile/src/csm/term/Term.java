package csm.term;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public abstract class Term {

	/**
	 * wertet den Term aus
	 * 
	 * @param varAssignment eine Variablenbelegung
	 * @return der Wert des Terms unter der gegebenen Variablenbelegung
	 */
	abstract public int evaluate(VarAssignment varAssignment);

	/**
	 * @return eine String-Repräsentation des Terms, die wieder den Term
	 *         selbst ergibt, wenn sie von der Methode
	 *         Transition#setAction(String) oder
	 *         Transition#setGuard(String) geparst wird.
	 */
	abstract public String prettyprint();

	abstract public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement;
}
