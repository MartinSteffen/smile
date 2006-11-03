package csm.guards;

import csm.VarAssignment;


public abstract class Guard {

	abstract public boolean evalGuard(VarAssignment va);

}
