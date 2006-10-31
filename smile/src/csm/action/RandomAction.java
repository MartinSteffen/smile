package csm.action;

import java.util.List;

import csm.VarAssignment;


public final class RandomAction extends Action {

	final public String varname;

	// XXX possibleValues auf Gültigkeit prüfen

	List<Integer> possibleValues;

	RandomAction(String varname, List<Integer> pv) {
		this.varname = varname;
		this.possibleValues = pv;
		// TODO Werte im Bereich?
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}

}
