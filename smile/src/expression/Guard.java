package expression;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public abstract class Guard {

	abstract public boolean evalGuard(VarAssignment va);

	/**
	 * @return eine String-Repräsentation des Guards, die wieder den
	 *         Guard selbst ergibt, wenn sie von der Methode
	 *         Transition#setGuard(String) geparst wird.
	 */
	abstract public String prettyprint();

	abstract public void noUndefinedVars(Dictionary<Variable> variables)
			throws ErrUndefinedElement;
}
