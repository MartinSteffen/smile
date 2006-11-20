package expression;

import csm.VarAssignment;


public final class ConstTerm extends Term {

	public final int value;

	public ConstTerm(int value) {
		this.value = value;
	}

	@Override
	public int evaluate(VarAssignment varAssignment) {
		return this.value;
	}

	@Override
	public String prettyprint() {
		return String.valueOf(value);
	}

}
