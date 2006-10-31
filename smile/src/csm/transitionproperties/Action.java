package csm.transitionproperties;

import csm.VarAssignment;

public abstract class Action {
// TODO subklassen \alpha
	
	/*
	 * 
	 * @returns neue Variablenbelegung
	 */
	// TODO Ausgabe-Events einbauen
	abstract VarAssignment doAction(VarAssignment pre);
}
