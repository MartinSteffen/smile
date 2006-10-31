package csm.action;

import csm.VarAssignment;


public abstract class Action {

	/*
	 * eine Aktion auf einer Variablenbelegung durchführen und die neue
	 * Variablenbelegung zurückgeben
	 * 
	 * XXX Ausgabe-Events werden nicht behandelt
	 * 
	 * @returns neue Variablenbelegung
	 */
	abstract VarAssignment doAction(VarAssignment pre);
}
