package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class SendAction extends Action {

	public final String event;
	public final Term value;

	public SendAction(String event, Term value) {
		this.event = event;
		this.value = value;
	}

	// XXX Event wird nur auf System.out ausgegeben
	@Override
	VarAssignment doAction(VarAssignment pre) {
		final int y = this.value.evaluate(pre);
		System.out.println("event " + this.event + " " + y);
		return pre;
	}

}
