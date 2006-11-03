package csm.guards;

import csm.VarAssignment;


public final class TrueGuard extends Guard {

	@Override
	public boolean evalGuard(VarAssignment va) {
		return true;
	}
}
