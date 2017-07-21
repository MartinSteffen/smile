package semantics;

import java.util.LinkedList;

import csm.expression.DoSkip;
import csm.statetree.CompositeState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.State;
import csm.statetree.Transition;


public class CompoundTransition extends LinkedList<Transition> {

	private static final long serialVersionUID = 1L;

	CompoundTransition(CompoundTransition t) {
		super(t);
	}

	public CompoundTransition() {
		// keine Initialisierung notwendig
	}

	/**
	 * Testet, ob diese CoTransition Element der Menge Enable ist. Die
	 * Parameter, die Enable im Paper übergeben bekommt, sind hier zu
	 * Event und CSMConfiguration zusammengefaßt.
	 */
	public boolean isEnabled(String event, // oder null
			CSMConfiguration c) {
		for (final Transition t : this) {
			if (!c.active.contains(t.getSource().stateOf()))
				return false;
			if (t.getEventName() != null && !t.getEventName().equals(event))
				return false;

			/*
			 * System.out.println("guard: " + t.getGuard().prettyprint() + "
			 * in " + c.varAssignment);
			 */

			// nach Def. von eval
			final State h = c.history.get(t.getTarget().regOf());
			c.varAssignment.wla = h == t.getTarget().stateOf();
			c.varAssignment.nab = h == null || h instanceof FinalState;
			boolean guard = t.getGuard().evaluate(c.varAssignment);
			c.varAssignment.wla = false;
			c.varAssignment.nab = false;
			if (!guard)
				return false;

			if (isExitCP(t)) {
				if (!(c.doActions.get(t.getSource().stateOf()) instanceof DoSkip))
					return false;
			}
		}

		return true;
	}

	boolean conflictsWith(CompoundTransition T2) {
		for (final Transition t1 : this)
			for (final Transition t2 : T2)
				if (t1.getSource().stateOf() == t2.getSource().stateOf())
					return true;

		return false;
	}

	boolean hasPriorityOver(CompoundTransition T2) {
		return prBelow(T2) && prStrBelow(T2);
	}

	boolean prBelow(CompoundTransition T2) {
		outer: for (final Transition t1 : this)
			if (isExitPR(t1) || t1.getSource() instanceof CompositeState) {
				for (final Transition t2 : T2) {
					if (!(isExitPR(t2) || t2.getSource() instanceof CompositeState))
						continue;
					if (t2.getSource().stateOf().isComponentOf(
							t1.getSource().stateOf()))
						continue outer;
				}
				return false;
			}
		return true;
	}

	boolean prStrBelow(CompoundTransition T2) {
		outer: for (final Transition t2 : T2)
			if (isExitPR(t2) || t2.getSource() instanceof CompositeState) {
				for (final Transition t1 : this) {
					if (!(isExitPR(t1) || t1.getSource() instanceof CompositeState))
						continue;
					if (!t1.getSource().stateOf().isComponentOf(
							t2.getSource().stateOf()))
						continue outer;
				}
				return false;
			}
		return true;
	}

	// transition \elem S_exit^PR
	private boolean isExitPR(Transition transition) {
		return isExitOfKind(transition, ExitState.KindOfExitstate.PR);
	}

	// transition \elem S_exit^CP
	private boolean isExitCP(Transition transition) {
		return isExitOfKind(transition, ExitState.KindOfExitstate.CP);
	}

	private boolean isExitOfKind(Transition transition,
			ExitState.KindOfExitstate kind) {
		return (transition.getSource() instanceof ExitState && ((ExitState) transition
				.getSource()).getKindOfExitstate().equals(kind));
	}

	/**
	 * stellt fest, ob die Transitionen in dieser CoTransition
	 * unterschiedlichen Events zugeordnet sind. Falls ja, ist diese
	 * Transition niemals enabled.
	 */
	boolean hasDifferentEvents() {
		String e = null;
		for (final Transition t : this) {
			if (e == null)
				e = t.getEventName();
			else if (t.getEventName() != null
				&& !t.getEventName().equals(e))
				return true;
		}
		return false;
	}

	boolean isTInt() {
		if (size() != 1)
			return false;
		if (get(0).getSource() instanceof CompositeState)
			return true;
		return false;
	}
}
