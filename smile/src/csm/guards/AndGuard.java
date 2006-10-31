package csm.guard;

import csm.VarAssignment;


public final class AndGuard extends Guard {

	public final Guard left;
	public final Guard right;

	AndGuard(Guard l, Guard r) {
		this.left = l;
		this.right = r;

	}

	@Override
	boolean evalGuard(VarAssignment va) {
		return this.left.evalGuard(va) && this.right.evalGuard(va);
	}

}
