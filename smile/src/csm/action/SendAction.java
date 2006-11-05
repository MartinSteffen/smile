package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class SendAction extends Action {

	public final String event;
	public final Term value;

	public SendAction(String event, Term value) {
		assert event != null;
		assert value != null;
		
		this.event = event;
		this.value = value;
	}

	// XXX Event wird nur auf System.out ausgegeben
	@Override
	public VarAssignment doAction(VarAssignment pre) {
		final int y = this.value.evaluate(pre);
		System.out.println("event ");
		System.out.print(this.event);
		System.out.print(' ');
		System.out.println(y);
		return pre;
	}

	@Override
	public String prettyprint() {
		return "send(" + event + ", " + value.prettyprint() + ')';
	}

}
