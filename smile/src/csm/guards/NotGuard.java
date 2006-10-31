package csm.guard;

import csm.VarAssignment;


public final class NotGuard extends Guard {

	Guard guard;

	public NotGuard(Guard guard) {
		this.guard = guard;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		boolean g = this.guard.evalGuard(va);
		return !g;
	}
}
