package csm.guards;

import csm.VarAssignment;

// TODO Parser f�r guards und actions
public abstract class Guard {

	abstract public boolean evalGuard(VarAssignment va);

}
