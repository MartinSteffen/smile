package csm;

import transitionproperties.Action;
import transitionproperties.Guard;
import csm.statetree.State;

public final class Transition {
	State source;
	Event event;
	Guard guard;
	Action action;
	State target;
	
	Transition(State s, Event e, Guard g, Action a, State t) {
		assert s.mayConnectTo(t);
		source = s;
		event = e;
		guard =g;
		action=a;
		target=t; 
	}
}
