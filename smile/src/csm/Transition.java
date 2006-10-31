package csm;

import java.util.HashMap;

import csm.action.Action;
import csm.guards.Guard;
import csm.statetree.State;

public final class Transition {

	State source;
	Event event;
	Guard guard;
	Action action;
	State target;
	HashMap x;

	Transition(State s, Event e, Guard g, Action a, State t) {
		assert s.mayConnectTo(t);
		this.source = s;
		this.event = e;
		this.guard = g;
		this.action = a;
		this.target = t;
	}
}
