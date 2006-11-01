package csm;

import csm.action.Action;
import csm.guards.Guard;
import csm.statetree.State;

public final class Transition {

	final State source;
	final State target;
	public Event event;
	public Guard guard;
	public Action action;
	Transition(State s, State t) {
		assert s.mayConnectTo(t);
		this.source = s;
		this.target = t;
	}
}
