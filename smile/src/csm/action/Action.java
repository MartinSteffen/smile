package csm.action;

import csm.VarAssignment;


/*
 * ein abstrakter Syntaxbaum, der einen Action-Ausdruck darstellt.    
 */
public abstract class Action {

	/**
	 * eine Aktion auf einer Variablenbelegung durchführen und die neue
	 * Variablenbelegung zurückgeben
	 * 
	 * @returns neue Variablenbelegung
	 */
	abstract public VarAssignment doAction(VarAssignment pre);

	abstract public String prettyprint();
	
}
