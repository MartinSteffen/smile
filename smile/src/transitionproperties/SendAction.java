package transitionproperties;

import csm.VarAssignment;

public final class SendAction extends Action {
	String event;

	Term value;

	public SendAction(String e, Term v) {
		event = e;
		value = v;
	}

	@Override
	VarAssignment doAction(VarAssignment pre) {
		int y=value.evaluate(pre);
		// TODO event, y ausgeben
		System.out.println("event "+event+" "+y);
		return pre;
	}

}
