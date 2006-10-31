package csm.guards;

import csm.VarAssignment;


public final class AndGuard extends Guard {

	public final Guard left;
	public final Guard right;

	public AndGuard(Guard l, Guard r) {
		this.left = l;
		this.right = r;

	}

	@Override
	public boolean evalGuard(VarAssignment va) {
		final boolean l = this.left.evalGuard(va);
		// shortcut evauation
		return l && this.right.evalGuard(va);
	}
}
