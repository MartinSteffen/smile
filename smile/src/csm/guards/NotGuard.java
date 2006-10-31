package csm.guard;

import csm.VarAssignment;


public final class NotGuard extends Guard {

	Guard g;

	NotGuard(Guard g) {
		this.g = g;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return !this.g.evalGuard(va);
	}
}
