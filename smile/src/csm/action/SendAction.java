package csm.action;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;
import csm.expression.Expression;


public final class SendAction extends Action {

	public final String event;
	public final Expression<Integer> value;

	public SendAction(String event, Expression<Integer> value) {
		assert event != null;
		assert value != null;

		this.event = event;
		this.value = value;
	}

	@Override
	public VarAssignment doAction(VarAssignment pre) {
		pre.sendEventName = this.event;
		pre.sendEventValue = this.value.evaluate(pre);

		System.out.print("event ");
		System.out.print(pre.sendEventName);
		System.out.print(' ');
		System.out.println(pre.sendEventValue);
		return pre;
	}

	@Override
	public String prettyprint() {
		return "send(" + this.event + ", " + this.value.prettyprint()
				+ ')';
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		this.value.noUndefinedVars(dict);

	}

}
