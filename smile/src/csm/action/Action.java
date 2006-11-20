package csm.action;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


/*
 * ein abstrakter Syntaxbaum, der einen Action-Ausdruck darstellt.
 */
public abstract class Action {

	// TODO die Methode doAction hier entfernen? Sie ist innerhalb von
	// csm.* nicht implementierbar.
	/**
	 * eine Aktion auf einer Variablenbelegung durchführen und die neue
	 * Variablenbelegung zurückgeben
	 * 
	 * @return neue Variablenbelegung
	 */
	@Deprecated
	abstract public VarAssignment doAction(VarAssignment pre);

	/**
	 * @return eine String-Repräsentation der Aktion, die wieder die
	 *         Aktion selbst ergibt, wenn sie von der Methode
	 *         Transition#setAction(String) geparst wird.
	 */
	abstract public String prettyprint();

	/**
	 *  stellt sicher, dass alle in diesem Term erwähnten Variablen definiert sind.
	 *  
	 *  @param dict das Dictionary, in dem alle Variablen definiert sein sollen
	 *  @throws ErrUndefinedElement wenn eine undefinierte Variable gefunden wurde
	 */
	abstract public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement;

}
