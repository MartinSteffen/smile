package csm.guard;

import csm.VarAssignment;


public final class OrGuard extends Guard {

	Guard left;
	Guard right;

	public OrGuard(Guard left, Guard right) {
		this.left = left;
		this.right = right;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		final boolean l = this.left.evalGuard(va);
		// shortcut evaluation
		return l || this.right.evalGuard(va);
	}
}
