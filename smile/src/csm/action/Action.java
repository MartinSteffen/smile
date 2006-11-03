package csm.action;

import csm.VarAssignment;


public abstract class Action {

	/**
	 * eine Aktion auf einer Variablenbelegung durchf�hren und die neue
	 * Variablenbelegung zur�ckgeben
	 * 
	 * @returns neue Variablenbelegung
	 */
	abstract public VarAssignment doAction(VarAssignment pre);
}
