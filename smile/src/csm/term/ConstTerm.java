package csm.term;

import csm.VarAssignment;


public class ConstTerm extends Term {

	public final int value;

	public ConstTerm(int value) {
		this.value = value;
	}

	@Override
	public int evaluate(VarAssignment varAssignment) {
		return this.value;
	}

}
