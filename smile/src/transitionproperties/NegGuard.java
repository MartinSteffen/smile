package transitionproperties;

import csm.VarAssignment;

public final class NegGuard extends Guard {
	Guard g;

	NegGuard(Guard g) {
		this.g = g;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return !g.evalGuard(va);
	}
}
