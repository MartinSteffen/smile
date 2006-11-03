package csm.action;

import csm.VarAssignment;


public abstract class Action {

	/**
	 * eine Aktion auf einer Variablenbelegung durchführen und die neue
	 * Variablenbelegung zurückgeben
	 * 
	 * @returns neue Variablenbelegung
	 */
	abstract public VarAssignment doAction(VarAssignment pre);
}
