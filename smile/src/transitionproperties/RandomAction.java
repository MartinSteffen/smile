package transitionproperties;

import java.util.List;

import csm.VarAssignment;

public final class RandomAction extends Action {

	Variable var;
	List<Integer> possibleValues;
	
	RandomAction(Variable v, List<Integer> pv) {
		var=v;
		possibleValues=pv;
		// TODO Werte im Bereich?
	}
	
	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}

}
