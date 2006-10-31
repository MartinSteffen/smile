package csm.guard;

import csm.VarAssignment;


public abstract class Guard {

	abstract public boolean evalGuard(VarAssignment va);

	// XXX abstract String show();
}
