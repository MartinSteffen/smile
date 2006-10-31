package csm.transitionproperties;

import csm.Variable;

public final class VarTerm extends Term {
	Variable var; 
	VarTerm(Variable v) {
		this.var = v;
	}
	
	@Override
	int evaluate(Object Variablenbelegung) {
		// TODO Auto-generated method stub
		return 0;
	}
}