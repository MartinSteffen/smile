package csm.expression;

import csm.VarAssignment;


/**
 * eine Aktion ist ein Ausdruck, der einen Systemzustand (ein
 * VarAssignment) auf einen neuen Systemzustand abbildet
 */
public abstract class Action extends Expression<VarAssignment> {

	abstract void doAction(VarAssignment pre);
	
	@Override
	public final VarAssignment evaluate(VarAssignment pre) {
		pre.sendEventName=null;
		doAction(pre);
		return pre;
	}
	
	@Override
	final int precedence() {
		return 0;
	}

}
