package csm.transitionproperties;

import csm.VarAssignment;

public final class AndGuard extends Guard {
	Guard l;

	Guard r;

	AndGuard(Guard l, Guard r) {
		this.l = l;
		this.r = r;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return this.l.evalGuard(va) && this.r.evalGuard(va);
	}

}
