package expression;

import csm.VarAssignment;


public abstract class Term {

	/**
	 * wertet den Term aus
	 * 
	 * @param varAssignment eine Variablenbelegung
	 * @return der Wert des Terms unter der gegebenen Variablenbelegung
	 */
	abstract public int evaluate(VarAssignment varAssignment);

	/**
	 * das Gegenst�ck zum Parser
	 * 
	 * @return eine textuelle Repr�sentation des Terms
	 */
	abstract public String prettyprint();
}
