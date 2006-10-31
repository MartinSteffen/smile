package transitionproperties;

import csm.VarAssignment;

public final class OrGuard extends Guard {
	Guard l;

	Guard r;

	OrGuard(Guard l, Guard r) {
		this.l = l;
		this.r = r;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return l.evalGuard(va) || r.evalGuard(va);
	}
}
