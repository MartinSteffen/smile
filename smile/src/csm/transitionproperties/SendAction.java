package csm.transitionproperties;

import csm.VarAssignment;
import csm.actions.Action;

public final class SendAction extends Action {
	String event;

	Term value;

	public SendAction(String e, Term v) {
		this.event = e;
		this.value = v;
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		final int y=this.value.evaluate(pre);
		// TODO event, y ausgeben
		System.out.println("event "+this.event+" "+y);
		return pre;
	}

}
