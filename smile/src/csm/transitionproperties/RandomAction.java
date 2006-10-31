package csm.transitionproperties;

import java.util.List;

import csm.VarAssignment;
import csm.Variable;
import csm.actions.Action;

public final class RandomAction extends Action {

	Variable var;
	List<Integer> possibleValues;
	
	RandomAction(Variable v, List<Integer> pv) {
		this.var=v;
		this.possibleValues=pv;
		// TODO Werte im Bereich?
	}
	
	@Override
	VarAssignment doAction(VarAssignment pre) {
		// TODO Auto-generated method stub
		return null;
	}

}
